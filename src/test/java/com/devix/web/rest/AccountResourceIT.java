package com.devix.web.rest;

import static com.devix.test.util.OAuth2TestUtil.TEST_USER_LOGIN;
import static com.devix.test.util.OAuth2TestUtil.registerAuthenticationToken;
import static com.devix.test.util.OAuth2TestUtil.testAuthenticationToken;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devix.IntegrationTest;
import com.devix.domain.User;
import com.devix.domain.UsuarioCentro;
import com.devix.repository.UserRepository;
import com.devix.repository.UsuarioCentroRepository;
import com.devix.security.AuthoritiesConstants;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class AccountResourceIT {

    @Autowired
    private MockMvc restAccountMockMvc;

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    ClientRegistration clientRegistration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @Test
    @Transactional
    void testGetExistingAccount() throws Exception {
        TestSecurityContextHolder.getContext()
            .setAuthentication(registerAuthenticationToken(authorizedClientService, clientRegistration, testAuthenticationToken()));

        restAccountMockMvc
            .perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(TEST_USER_LOGIN))
            .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    @Test
    @Transactional
    void testGetAccountCompanies() throws Exception {
        User user = userRepository
            .findOneByLogin(TEST_USER_LOGIN)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setId(UUID.randomUUID().toString());
                newUser.setLogin(TEST_USER_LOGIN);
                newUser.setActivated(true);
                return userRepository.saveAndFlush(newUser);
            });

        UsuarioCentro principalCompany = new UsuarioCentro();
        principalCompany.setNoCia(1L);
        principalCompany.setPrincipal(true);
        principalCompany.setUser(user);
        usuarioCentroRepository.saveAndFlush(principalCompany);

        UsuarioCentro secondaryCompany = new UsuarioCentro();
        secondaryCompany.setNoCia(2L);
        secondaryCompany.setPrincipal(false);
        secondaryCompany.setUser(user);
        usuarioCentroRepository.saveAndFlush(secondaryCompany);

        TestSecurityContextHolder.getContext()
            .setAuthentication(registerAuthenticationToken(authorizedClientService, clientRegistration, testAuthenticationToken()));

        restAccountMockMvc
            .perform(get("/api/account/companies").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].noCia").value(1))
            .andExpect(jsonPath("$[0].principal").value(true))
            .andExpect(jsonPath("$[1].noCia").value(2))
            .andExpect(jsonPath("$[1].principal").value(false));
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void testRejectInvalidCompanyHeader() throws Exception {
        restAccountMockMvc
            .perform(get("/api/centros").header("X-Company-Id", "999999").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void testRejectRequestWhenAuthenticatedUserHasNoCompanies() throws Exception {
        restAccountMockMvc.perform(get("/api/centros").accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    void testGetUnknownAccount() throws Exception {
        restAccountMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testNonAuthenticatedUser() throws Exception {
        restAccountMockMvc.perform(get("/api/authenticate")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void testAuthenticatedUser() throws Exception {
        restAccountMockMvc.perform(get("/api/authenticate").with(request -> request)).andExpect(status().isNoContent());
    }
}
