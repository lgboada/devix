package com.devix.security.company;

import java.lang.reflect.Method;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CompanyWriteEnforcementAspect {

    private final CompanyContextService companyContextService;

    public CompanyWriteEnforcementAspect(CompanyContextService companyContextService) {
        this.companyContextService = companyContextService;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.save(..)) && args(entity)")
    public Object enforceCompanyOnSave(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        if (entity != null) {
            Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
            currentCompany.ifPresent(companyId -> forceCompany(entity, companyId));
        }
        return joinPoint.proceed(new Object[] { entity });
    }

    private void forceCompany(Object entity, Long companyId) {
        Method setNoCia = findMethod(entity.getClass(), "setNoCia", Long.class);
        if (setNoCia == null) {
            return;
        }
        try {
            setNoCia.invoke(entity, companyId);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo forzar noCia en la entidad", e);
        }
    }

    private Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
