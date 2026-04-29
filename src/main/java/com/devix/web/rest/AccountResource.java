package com.devix.web.rest;

import com.devix.repository.AccountBodegaProjection;
import com.devix.repository.AccountCentroProjection;
import com.devix.repository.AccountCompanyProjection;
import com.devix.repository.UsuarioCentroBodegaRepository;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.security.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);
    private final UsuarioCentroRepository usuarioCentroRepository;
    private final UsuarioCentroBodegaRepository usuarioCentroBodegaRepository;

    public AccountResource(UsuarioCentroRepository usuarioCentroRepository, UsuarioCentroBodegaRepository usuarioCentroBodegaRepository) {
        this.usuarioCentroRepository = usuarioCentroRepository;
        this.usuarioCentroBodegaRepository = usuarioCentroBodegaRepository;
    }

    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @param principal the current user; resolves to {@code null} if not authenticated.
     * @return the current user.
     * @throws AccountResourceException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserVM getAccount(Principal principal) {
        if (principal instanceof AbstractAuthenticationToken) {
            return getUserFromAuthentication((AbstractAuthenticationToken) principal);
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)},
     * or with status {@code 401 (Unauthorized)} if not authenticated.
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Void> isAuthenticated(Principal principal) {
        LOG.debug("REST request to check if the current user is authenticated");
        return ResponseEntity.status(principal == null ? HttpStatus.UNAUTHORIZED : HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/account/companies")
    public List<CompanyVM> getAccountCompanies(Principal principal) {
        if (!(principal instanceof AbstractAuthenticationToken authenticationToken)) {
            throw new AccountResourceException("User could not be found");
        }

        List<AccountCompanyProjection> rows = usuarioCentroRepository.findDistinctAccountCompaniesByUserLogin(
            authenticationToken.getName()
        );
        return rows
            .stream()
            .map(row ->
                new CompanyVM(
                    row.getEffectiveNoCia(),
                    row.getPrincipalInt() != null && row.getPrincipalInt() >= 1,
                    nullIfBlank(row.getLabel())
                )
            )
            .toList();
    }

    private static String nullIfBlank(String s) {
        return s == null || s.isBlank() ? null : s;
    }

    private static class UserVM {

        private String login;
        private Set<String> authorities;
        private Map<String, Object> details;

        UserVM(String login, Set<String> authorities, Map<String, Object> details) {
            this.login = login;
            this.authorities = authorities;
            this.details = details;
        }

        public boolean isActivated() {
            return true;
        }

        public Set<String> getAuthorities() {
            return authorities;
        }

        public String getLogin() {
            return login;
        }

        @JsonAnyGetter
        public Map<String, Object> getDetails() {
            return details;
        }
    }

    @GetMapping("/account/centros")
    public List<CentroVM> getAccountCentros(
        @org.springframework.web.bind.annotation.RequestParam("noCia") Long noCia,
        Principal principal
    ) {
        if (!(principal instanceof AbstractAuthenticationToken authToken)) {
            throw new AccountResourceException("User could not be found");
        }
        List<AccountCentroProjection> rows = usuarioCentroRepository.findDistinctAccountCentrosByUserLoginAndNoCia(
            authToken.getName(),
            noCia
        );
        return rows
            .stream()
            .map(row ->
                new CentroVM(row.getCentroId(), row.getPrincipalInt() != null && row.getPrincipalInt() >= 1, nullIfBlank(row.getLabel()))
            )
            .toList();
    }

    @GetMapping("/account/bodegas")
    public List<BodegaVM> getAccountBodegas(
        @org.springframework.web.bind.annotation.RequestParam("centroId") Long centroId,
        Principal principal
    ) {
        if (!(principal instanceof AbstractAuthenticationToken authToken)) {
            throw new AccountResourceException("User could not be found");
        }
        List<AccountBodegaProjection> rows = usuarioCentroBodegaRepository.findDistinctAccountBodegasByUserLoginAndCentroId(
            authToken.getName(),
            centroId
        );
        return rows
            .stream()
            .map(row ->
                new BodegaVM(
                    row.getBodegaId(),
                    row.getPrincipalInt() != null && row.getPrincipalInt() >= 1,
                    nullIfBlank(row.getCodigo()),
                    nullIfBlank(row.getLabel())
                )
            )
            .toList();
    }

    private record CompanyVM(Long noCia, boolean principal, String label) {}

    private record CentroVM(Long centroId, boolean principal, String label) {}

    private record BodegaVM(Long bodegaId, boolean principal, String codigo, String label) {}

    private static UserVM getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        Map<String, Object> attributes;
        if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        return new UserVM(
            authToken.getName(),
            authToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()),
            SecurityUtils.extractDetailsFromTokenAttributes(attributes)
        );
    }
}
