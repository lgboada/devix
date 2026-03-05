package com.devix.web.filter;

import com.devix.security.company.CompanyContextService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

public class CompanyContextFilter extends OncePerRequestFilter {

    private final CompanyContextService companyContextService;

    public CompanyContextFilter(CompanyContextService companyContextService) {
        this.companyContextService = companyContextService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (!requiresCompanyContext(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional<Long> companyId = companyContextService.resolveCurrentUserCompanyId(
                request.getHeader(CompanyContextService.COMPANY_HEADER)
            );
            companyId.ifPresent(companyContextService::setCurrentCompanyId);
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        } finally {
            companyContextService.clear();
        }
    }

    private boolean requiresCompanyContext(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            return false;
        }
        return !"/api/account".equals(path) && !"/api/authenticate".equals(path) && !"/api/account/companies".equals(path);
    }
}
