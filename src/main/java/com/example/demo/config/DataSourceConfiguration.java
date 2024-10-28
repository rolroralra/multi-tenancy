package com.example.demo.config;

import com.example.demo.config.TenantProperties.TenantProperty;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSourceConfiguration {

    private final TenantProperties tenantProperties;

    @Value("${default-tenant}")
    private String defaultTenant;

    @Bean
    @Qualifier("masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource-master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @Primary
    public DataSource multitenantRoutingDataSource() {
        log.info("defaultTenant: {}", defaultTenant);
        log.info("tenantProperties: {}", tenantProperties);

        Map<Object, Object> resolvedDataSources = tenantProperties.tenants().stream()
            .collect(Collectors.toMap(TenantProperty::name, this::buildDataSourceFromTenantProperty));

        AbstractRoutingDataSource routingDataSource = new MultitenantRoutingDataSource();
        routingDataSource.setTargetDataSources(resolvedDataSources);

        // 기본 DataSource 설정 (optional)
        routingDataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));

        resolvedDataSources.forEach((tenantId, dataSource) -> initSchema((DataSource) dataSource));

        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    @Bean
    List<DataSource> tenantDataSources(DataSource dataSource) {
        return tenantProperties.tenants().stream()
            .map(this::buildDataSourceFromTenantProperty)
            .toList();
    }

    private DataSource buildDataSourceFromTenantProperty(TenantProperty tenant) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
            .driverClassName(tenant.driverClassName())
            .url(tenant.url())
            .username(tenant.username())
            .password(tenant.password());
        return dataSourceBuilder.build();
    }

    private void initSchema(DataSource dataSource) {
        ClassPathResource schemaResource = new ClassPathResource("sql/mysql/schema.sql");

        ResourceDatabasePopulator resourceDatabasePopulator
            = new ResourceDatabasePopulator(schemaResource);

        resourceDatabasePopulator.execute(dataSource);

        log.info("Schema initialized for dataSource: {}", dataSource);
    }
}
