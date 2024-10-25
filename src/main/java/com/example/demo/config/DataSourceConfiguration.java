package com.example.demo.config;

import com.example.demo.config.TenantProperties.TenantProperty;
import com.example.demo.repository.MultitenantRoutingDataSource;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final TenantProperties tenantProperties;

    @Value("${default-tenant}")
    private String defaultTenant;

    @Bean
    public DataSource dataSource() {
        System.out.println(defaultTenant);
        System.out.println(tenantProperties);

        Map<Object, Object> resolvedDataSources = tenantProperties.tenants().stream()
            .collect(Collectors.toMap(TenantProperty::name, this::buildDataSource));

        AbstractRoutingDataSource routingDataSource = new MultitenantRoutingDataSource();
        routingDataSource.setTargetDataSources(resolvedDataSources);

        // 기본 DataSource 설정 (optional)
        routingDataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));

        return routingDataSource;
    }

    private DataSource buildDataSource(TenantProperty tenant) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
            .driverClassName(tenant.driverClassName())
            .url(tenant.url())
            .username(tenant.username())
            .password(tenant.password());
        return dataSourceBuilder.build();
    }
}
