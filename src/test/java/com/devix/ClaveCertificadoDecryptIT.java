package com.devix;

import static org.assertj.core.api.Assertions.assertThat;

import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.security.AesGcmCryptoService;
import com.devix.service.security.CompanyClientSecretService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClaveCertificadoDecryptIT {

    @Autowired
    CompaniaRepository companiaRepository;

    @Autowired
    CompanyClientSecretService companyClientSecretService;

    @Autowired
    AesGcmCryptoService aesGcmCryptoService;

    @Test
    void puedeDescifrarClaveCertificado() throws Exception {
        long noCia = Long.parseLong(System.getenv().getOrDefault("NO_CIA", "1"));
        Compania c = companiaRepository.findByNoCia(noCia).orElseThrow();

        SecretKey key = companyClientSecretService.getAesKeyOrThrow(c);
        String plain = aesGcmCryptoService.decryptIfEncrypted(c.getClaveCertificado(), key);
        assertThat(plain).isNotBlank();

        String expected = System.getenv("EXPECTED_P12_PASSWORD");
        if (expected != null) {
            assertThat(plain).isEqualTo(expected);
        }

        // No imprimimos la clave en claro; solo un hash para verificación visual.
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));
        String sha256b64 = Base64.getEncoder().encodeToString(digest);
        System.out.println("clave_certificado SHA-256 (Base64) = " + sha256b64);
    }
}
