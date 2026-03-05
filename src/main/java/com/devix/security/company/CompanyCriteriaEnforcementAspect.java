package com.devix.security.company;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
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
            throw new AccessDeniedException("No puede consultar informacion de otra compania");
        }

        LongFilter forcedFilter = new LongFilter();
        forcedFilter.setEquals(companyId);
        invokeSetter(criteria, setNoCia, forcedFilter);
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
