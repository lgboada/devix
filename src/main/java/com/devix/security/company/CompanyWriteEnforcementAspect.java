package com.devix.security.company;

import com.devix.domain.Compania;
import com.devix.domain.UsuarioCentro;
import com.devix.security.AuthoritiesConstants;
import com.devix.security.SecurityUtils;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        // La tabla `compania` define los tenants; no debe forzarse/validarse por la compañía activa de sesión.
        if (entity instanceof Compania) {
            return joinPoint.proceed(new Object[] { entity });
        }
        if (entity != null) {
            Optional<Long> currentCompany = companyContextService.getCurrentCompanyId();
            currentCompany.ifPresent(companyId -> {
                if (shouldValidateEntityBelongsToCurrentCompanyForUpdate(entity)) {
                    validateEntityBelongsToCurrentCompanyForUpdate(entity, companyId);
                }
                if (shouldStampNoCiaFromSession(entity)) {
                    forceCompany(entity, companyId);
                }
            });
        }
        return joinPoint.proceed(new Object[] { entity });
    }

    /**
     * Solo en el flujo REST de usuario-centro (pantalla / API {@code /api/usuario-centros} POST/PUT/PATCH),
     * un {@code ROLE_ADMIN} puede persistir el {@code noCia} enviado en el cuerpo sin que la sesión lo pise.
     * Cualquier otro guardado de {@link UsuarioCentro} sigue aplicando el {@code noCia} de la compañía activa.
     */
    private boolean shouldStampNoCiaFromSession(Object entity) {
        if (!(entity instanceof UsuarioCentro)) {
            return true;
        }
        if (isUsuarioCentroResourceWriteByAdmin()) {
            return false;
        }
        return true;
    }

    private boolean isUsuarioCentroResourceWriteByAdmin() {
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return false;
        }
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return false;
        }
        HttpServletRequest req = attrs.getRequest();
        String method = req.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method))) {
            return false;
        }
        String path = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        return "/api/usuario-centros".equals(path) || path.startsWith("/api/usuario-centros/");
    }

    /**
     * Para admin en el flujo REST de usuario-centro, permitimos actualizar registros de otras compañías
     * (p. ej. reasignar usuario-centro entre compañías) sin que lo bloquee la compañía activa de sesión.
     */
    private boolean shouldValidateEntityBelongsToCurrentCompanyForUpdate(Object entity) {
        if (entity instanceof UsuarioCentro && isUsuarioCentroResourceWriteByAdmin()) {
            return false;
        }
        return true;
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
