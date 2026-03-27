package com.devix.security.company;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Aspect
@Component
public class CompanyWriteEnforcementAspect {

    private final CompanyContextService companyContextService;
    private final EntityManager entityManager;

    public CompanyWriteEnforcementAspect(CompanyContextService companyContextService, EntityManager entityManager) {
        this.companyContextService = companyContextService;
        this.entityManager = entityManager;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.save(..)) && args(entity)")
    public Object enforceCompanyOnSave(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        if (entity != null) {
            Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
            currentCompany.ifPresent(companyId -> {
                validateEntityBelongsToCurrentCompanyForUpdate(entity, companyId);
                forceCompany(entity, companyId);
            });
        }
        return joinPoint.proceed(new Object[] { entity });
    }

    private void validateEntityBelongsToCurrentCompanyForUpdate(Object entity, Long companyId) {
        Method getId = findMethod(entity.getClass(), "getId");
        Method getNoCia = findMethod(entity.getClass(), "getNoCia");
        if (getId == null || getNoCia == null) {
            return;
        }

        Object id = ReflectionUtils.invokeMethod(getId, entity);
        if (id == null) {
            return;
        }

        Object persisted = entityManager.find(entity.getClass(), id);
        if (persisted == null) {
            return;
        }

        Long persistedCompany = (Long) ReflectionUtils.invokeMethod(getNoCia, persisted);
        if (persistedCompany != null && !companyId.equals(persistedCompany)) {
            throw new AccessDeniedException("No puede modificar informacion de otra compania");
        }
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
