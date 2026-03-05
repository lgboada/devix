package com.devix.security.company;

import java.util.Optional;

public final class CompanyContextHolder {

    private static final ThreadLocal<Long> CURRENT_COMPANY_ID = new ThreadLocal<>();

    private CompanyContextHolder() {}

    public static void setCurrentCompanyId(Long companyId) {
        CURRENT_COMPANY_ID.set(companyId);
    }

    public static Optional<Long> getCurrentCompanyId() {
        return Optional.ofNullable(CURRENT_COMPANY_ID.get());
    }

    public static void clear() {
        CURRENT_COMPANY_ID.remove();
    }
}
