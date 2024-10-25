package com.example.demo.filter;

import com.example.demo.context.TenantContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TenantFilter implements Filter {
    private static final String X_TENANT_ID_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String tenantName = req.getHeader(X_TENANT_ID_HEADER);
        TenantContext.setTenantId(tenantName);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            TenantContext.clear();
        }

    }
}
