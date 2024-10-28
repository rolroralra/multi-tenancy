package com.example.demo.config;

import com.example.demo.context.TenantContext;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantRoutingDataSource extends AbstractRoutingDataSource {

    private final AtomicBoolean initialized = new AtomicBoolean();

    @Override
    protected DataSource determineTargetDataSource() {
        if (this.initialized.compareAndSet(false, true)) {
            this.afterPropertiesSet();
        }

        return super.determineTargetDataSource();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenantId();
    }
}
