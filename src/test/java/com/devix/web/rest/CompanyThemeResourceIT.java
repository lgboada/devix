package com.devix.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devix.IntegrationTest;
import com.devix.domain.User;
import com.devix.domain.UsuarioCentro;
import com.devix.repository.UserRepository;
import com.devix.repository.UsuarioCentroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
class CompanyThemeResourceIT {

    private static final String LOGIN = "theme-test-user";
    private static final Long NO_CIA = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @BeforeEach
    @Transactional
    void setupUserCompany() {
        User user = userRepository
            .findOneByLogin(LOGIN)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setId(UUID.randomUUID().toString());
                newUser.setLogin(LOGIN);
                newUser.setActivated(true);
                return userRepository.saveAndFlush(newUser);
            });

        if (!usuarioCentroRepository.existsByUser_LoginAndNoCia(LOGIN, NO_CIA)) {
            UsuarioCentro uc = new UsuarioCentro();
            uc.setNoCia(NO_CIA);
            uc.setPrincipal(true);
            uc.setUser(user);
            usuarioCentroRepository.saveAndFlush(uc);
        }
    }

    @Test
    @Transactional
    @WithMockUser(LOGIN)
    void getCurrentThemeReturnsDefault() throws Exception {
        mockMvc
            .perform(get("/api/company-theme/current").header("X-Company-Id", NO_CIA).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.themeName").value("litera"));
    }

    @Test
    @Transactional
    @WithMockUser(username = LOGIN, authorities = "ROLE_ADMIN")
    void updateCurrentRejectsInvalidTheme() throws Exception {
        mockMvc
            .perform(
                put("/api/company-theme/current")
                    .header("X-Company-Id", NO_CIA)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(new UpdateBody("not-a-theme")))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username = LOGIN, authorities = "ROLE_ADMIN")
    void uploadAssetsRejectsInvalidExtension() throws Exception {
        MockMultipartFile invalidLogo = new MockMultipartFile("logo", "logo.txt", MediaType.TEXT_PLAIN_VALUE, "x".getBytes());

        mockMvc
            .perform(multipart("/api/company-theme/current/assets").file(invalidLogo).header("X-Company-Id", NO_CIA))
            .andExpect(status().isBadRequest());
    }

    private record UpdateBody(String themeName) {}
}
