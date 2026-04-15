package com.devix.security.company;

import com.devix.security.AuthoritiesConstants;
import com.devix.security.SecurityUtils;
import com.devix.service.criteria.CompaniaCriteria;
import com.devix.service.criteria.UsuarioCentroCriteria;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.jhipster.service.filter.LongFilter;

@Aspect
@Component
public class CompanyCriteriaEnforcementAspect {

    private final CompanyContextService companyContextService;

    public CompanyCriteriaEnforcementAspect(CompanyContextService companyContextService) {
        this.companyContextService = companyContextService;
    }

    @Around(
        "execution(* com.devix.service.*QueryService.findByCriteria(..)) || execution(* com.devix.service.*QueryService.countByCriteria(..))"
    )
    public Object enforceCompanyOnCriteria(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] != null) {
            applyCompanyFilter(args[0]);
        }
        return joinPoint.proceed(args);
    }

    private void applyCompanyFilter(Object criteria) {
        // La tabla `compania` define los tenants; no debe filtrarse por la empresa activa
        // (p. ej. asignación usuario–centro debe listar todas las compañías).
        if (criteria instanceof CompaniaCriteria) {
            return;
        }

        // Admin: en /api/usuario-centros (listado/pantalla) debe poder ver todas las compañías.
        if (criteria instanceof UsuarioCentroCriteria && isUsuarioCentroApiRequestByAdmin()) {
            return;
        }

        Method getNoCia = findMethod(criteria.getClass(), "getNoCia");
        Method setNoCia = findMethod(criteria.getClass(), "setNoCia", LongFilter.class);
        if (getNoCia == null || setNoCia == null) {
            return;
        }

        Long companyId = companyContextService.getCurrentCompanyId().orElse(null);
        if (companyId == null) {
            return;
        }
        LongFilter requestedFilter = invokeGetter(criteria, getNoCia);

        if (requestedFilter != null && requestedFilter.getEquals() != null && !companyId.equals(requestedFilter.getEquals())) {
            if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                throw new AccessDeniedException("No puede consultar informacion de otra compania");
            }
            // Administrador: respeta noCia.equals (p. ej. centros al elegir compania en usuario-centro)
            return;
        }

        LongFilter forcedFilter = new LongFilter();
        forcedFilter.setEquals(companyId);
        invokeSetter(criteria, setNoCia, forcedFilter);
    }

    private boolean isUsuarioCentroApiRequestByAdmin() {
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return false;
        }
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return false;
        }
        HttpServletRequest req = attrs.getRequest();
        if (!"GET".equals(req.getMethod())) {
            return false;
        }
        String path = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        return "/api/usuario-centros".equals(path) || path.startsWith("/api/usuario-centros/");
    }

    private Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private LongFilter invokeGetter(Object target, Method method) {
        try {
            return (LongFilter) method.invoke(target);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer noCia del criterio", e);
        }
    }

    private void invokeSetter(Object target, Method method, LongFilter filter) {
        try {
            method.invoke(target, filter);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo asignar noCia en el criterio", e);
        }
    }
}
