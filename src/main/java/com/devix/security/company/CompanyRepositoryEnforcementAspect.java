package com.devix.security.company;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Aspect
@Component
public class CompanyRepositoryEnforcementAspect {

    private final CompanyContextService companyContextService;
    private final EntityManager entityManager;

    public CompanyRepositoryEnforcementAspect(CompanyContextService companyContextService, EntityManager entityManager) {
        this.companyContextService = companyContextService;
        this.entityManager = entityManager;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.findById(..)) && args(id)")
    public Object enforceCompanyOnFindById(ProceedingJoinPoint joinPoint, Object id) throws Throwable {
        Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
        if (currentCompany.isEmpty()) {
            return joinPoint.proceed(new Object[] { id });
        }

        Object result = joinPoint.proceed(new Object[] { id });
        if (result instanceof Optional<?> optional && optional.isPresent()) {
            validateEntityCompany(optional.get(), currentCompany.get(), "No puede consultar informacion de otra compania");
        }
        return result;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.existsById(..)) && args(id)")
    public Object enforceCompanyOnExistsById(ProceedingJoinPoint joinPoint, Object id) throws Throwable {
        Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
        if (currentCompany.isEmpty()) {
            return joinPoint.proceed(new Object[] { id });
        }

        Object entity = findEntityById(joinPoint.getTarget(), id);
        if (entity == null) {
            return joinPoint.proceed(new Object[] { id });
        }
        if (!hasNoCia(entity.getClass())) {
            return joinPoint.proceed(new Object[] { id });
        }

        Long entityCompany = readNoCia(entity);
        // Backward compatibility: legacy records can have noCia null.
        // In that case we let the flow continue and write enforcement
        // will stamp the current company on save/update.
        if (entityCompany == null) {
            return true;
        }
        if (!currentCompany.get().equals(entityCompany)) {
            throw new AccessDeniedException("No puede operar informacion de otra compania");
        }
        return true;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.deleteById(..)) && args(id)")
    public Object enforceCompanyOnDeleteById(ProceedingJoinPoint joinPoint, Object id) throws Throwable {
        Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
        if (currentCompany.isEmpty()) {
            return joinPoint.proceed(new Object[] { id });
        }

        Object entity = findEntityById(joinPoint.getTarget(), id);
        if (entity != null) {
            validateEntityCompany(entity, currentCompany.get(), "No puede eliminar informacion de otra compania");
        }
        return joinPoint.proceed(new Object[] { id });
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.findAllById(..)) && args(ids)")
    public Object enforceCompanyOnFindAllById(ProceedingJoinPoint joinPoint, Iterable<?> ids) throws Throwable {
        Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
        Object result = joinPoint.proceed(new Object[] { ids });
        if (currentCompany.isEmpty()) {
            return result;
        }

        if (!(result instanceof Iterable<?> iterableResult)) {
            return result;
        }

        List<Object> filtered = new ArrayList<>();
        for (Object entity : iterableResult) {
            validateEntityCompany(entity, currentCompany.get(), "No puede consultar informacion de otra compania");
            filtered.add(entity);
        }
        return filtered;
    }

    @Around("execution(* org.springframework.data.repository.CrudRepository+.delete(..)) && args(entity)")
    public Object enforceCompanyOnDelete(ProceedingJoinPoint joinPoint, Object entity) throws Throwable {
        Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
        if (currentCompany.isPresent() && entity != null) {
            validateEntityCompany(entity, currentCompany.get(), "No puede eliminar informacion de otra compania");
        }
        return joinPoint.proceed(new Object[] { entity });
    }

    private Object findEntityById(Object repositoryTarget, Object id) {
        Class<?> domainClass = resolveDomainClass(repositoryTarget);
        if (domainClass == null) {
            return null;
        }
        return entityManager.find(domainClass, id);
    }

    private Class<?> resolveDomainClass(Object repositoryTarget) {
        Field entityInformationField = ReflectionUtils.findField(repositoryTarget.getClass(), "entityInformation");
        if (entityInformationField == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(entityInformationField);
        Object entityInformation = ReflectionUtils.getField(entityInformationField, repositoryTarget);
        if (entityInformation == null) {
            return null;
        }

        Method getJavaType = ReflectionUtils.findMethod(entityInformation.getClass(), "getJavaType");
        if (getJavaType == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(getJavaType);
        Object javaType = ReflectionUtils.invokeMethod(getJavaType, entityInformation);
        return javaType instanceof Class<?> clazz ? clazz : null;
    }

    private void validateEntityCompany(Object entity, Long companyId, String errorMessage) {
        if (!hasNoCia(entity.getClass())) {
            return;
        }
        Long entityCompany = readNoCia(entity);
        if (entityCompany != null && !companyId.equals(entityCompany)) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    private boolean hasNoCia(Class<?> entityClass) {
        return ReflectionUtils.findMethod(entityClass, "getNoCia") != null;
    }

    private Long readNoCia(Object entity) {
        Method getNoCia = ReflectionUtils.findMethod(entity.getClass(), "getNoCia");
        if (getNoCia == null) {
            return null;
        }
        Object value = ReflectionUtils.invokeMethod(getNoCia, entity);
        return value instanceof Long longValue ? longValue : null;
    }
}
