package com.devix.security.company;

import com.devix.domain.UsuarioCentro;
import com.devix.repository.AccountCompanyProjection;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.security.SecurityUtils;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CompanyContextService {

    public static final String COMPANY_HEADER = "X-Company-Id";

    private final UsuarioCentroRepository usuarioCentroRepository;

    public CompanyContextService(UsuarioCentroRepository usuarioCentroRepository) {
        this.usuarioCentroRepository = usuarioCentroRepository;
    }

    public Optional<Long> getCurrentCompanyId() {
        return CompanyContextHolder.getCurrentCompanyId();
    }

    public Long getCurrentCompanyIdOrThrow() {
        return getCurrentCompanyId().orElseThrow(() -> new AccessDeniedException("No existe una compania activa en la solicitud"));
    }

    public void setCurrentCompanyId(Long companyId) {
        CompanyContextHolder.setCurrentCompanyId(companyId);
    }

    public void clear() {
        CompanyContextHolder.clear();
    }

    public List<UsuarioCentro> getUserCompanies(String login) {
        return usuarioCentroRepository.findAllByUserLoginOrderByPrincipalDescNoCiaAsc(login);
    }

    public Optional<Long> resolveCurrentUserCompanyId(String headerValue) {
        Optional<String> currentUser = SecurityUtils.getCurrentUserLogin();
        if (currentUser.isEmpty()) {
            return Optional.empty();
        }

        String login = currentUser.get();

        if (headerValue != null && !headerValue.isBlank()) {
            Long requestedCompany = parseCompanyId(headerValue);
            if (!usuarioCentroRepository.existsByUserLoginAndEffectiveNoCia(login, requestedCompany)) {
                throw new AccessDeniedException("La compania enviada en header no pertenece al usuario autenticado");
            }
            return Optional.of(requestedCompany);
        }

        List<AccountCompanyProjection> rows = usuarioCentroRepository.findDistinctAccountCompaniesByUserLogin(login);
        return rows.stream().map(AccountCompanyProjection::getEffectiveNoCia).findFirst();
    }

    private Long parseCompanyId(String companyHeader) {
        try {
            return Long.valueOf(companyHeader.trim());
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("El valor de X-Company-Id no es valido");
        }
    }
}
