package com.example.demo.config;

import com.example.demo.config.TenantProperties.TenantProperty;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchemaConfig {
    private final DataSource masterDataSource;

    private final TenantProperties tenantProperties;

    @PostConstruct
    public void initSchema() {
        tenantProperties.tenants().stream()
            .map(TenantProperty::name)
            .forEach(this::createSchemaIfNotExists);
    }

    private void createSchemaIfNotExists(String tenantId) {
        createSchemaIfNotExists(tenantId, masterDataSource);
    }

    private void createSchemaIfNotExists(String tenantId, DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String checkSchemaQuery = "SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name = ?";

        Integer count = jdbcTemplate.queryForObject(checkSchemaQuery, new Object[]{tenantId}, Integer.class);
        if (count != null && count == 0) {
            String createSchemaQuery = "CREATE SCHEMA " + tenantId;
            jdbcTemplate.execute(createSchemaQuery);
            log.info("스키마 " + tenantId + " 생성됨.");
        } else {
            log.info("스키마 " + tenantId + " 이미 존재함.");
        }
    }
}
