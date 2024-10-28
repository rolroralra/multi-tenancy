package com.example.demo.config;

import com.example.demo.config.TenantProperties.TenantProperty;
import com.example.demo.constants.TenantConstants;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

//    private final TenantProperties tenantProperties;
//
//    @Bean
//    public Flyway flyway(DataSource dataSource) {
//        String[] schemas = tenantProperties.tenants().stream()
//            .map(TenantProperty::name)
//            .toArray(String[]::new);
//
//        Flyway flyway = Flyway.configure()
//            .locations("db/migration/default")
//            .dataSource(dataSource)
//            .schemas(TenantConstants.DEFAULT_TENANT_ID)
//            .load();
//        flyway.migrate();
//        return flyway;
//    }
}
