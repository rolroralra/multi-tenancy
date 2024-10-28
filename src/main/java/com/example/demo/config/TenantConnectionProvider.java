package com.example.demo.config;

import com.example.demo.constants.TenantConstants;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private DataSource datasource;

    public TenantConnectionProvider(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.debug("Get connection for tenant {}", tenantIdentifier);
        final Connection connection = getAnyConnection();
        connection.createStatement()
            .execute(String.format("SET SCHEMA \"%s\";", tenantIdentifier));
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.debug("Release connection for tenant {}", tenantIdentifier);
        connection.createStatement()
            .execute(String.format("SET SCHEMA \"%s\";", TenantConstants.DEFAULT_TENANT_ID));
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> unwrapType) {
        return null;
    }
}