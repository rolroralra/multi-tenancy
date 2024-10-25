package com.example.demo.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT_ID.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT_ID.get();
    }

    public static void clear() {
        CURRENT_TENANT_ID.remove();
    }
}
