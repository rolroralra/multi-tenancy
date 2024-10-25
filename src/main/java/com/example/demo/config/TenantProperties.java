package com.example.demo.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "multitenancy")
public record TenantProperties(
    List<TenantProperty> tenants
) {
    public record TenantProperty(
        String name,
        DataSourceProperties datasource
    ) {
        public record DataSourceProperties(
            String url,
            String username,
            String password,
            String driverClassName
        ) {

        }

        public String url() {
            return datasource.url();
        }

        public String username() {
            return datasource.username();
        }

        public String password() {
            return datasource.password();
        }

        public String driverClassName() {
            return datasource.driverClassName();
        }
    }


}
