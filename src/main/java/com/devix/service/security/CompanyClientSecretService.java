package com.devix.service.security;

import com.devix.domain.Compania;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Resuelve la llave AES por compañía leyendo {@code client_secret} desde:
 * {@code <pathFileServer>/parametros.properties}.
 *
 * El {@code client_secret} debe ser Base64 de 32 bytes (AES-256).
 */
@Service
public class CompanyClientSecretService {

    private static final String PARAMS_FILENAME = "parametros.properties";
    private static final String CLIENT_SECRET_KEY = "client_secret";

    private final String fallbackUploadDir;
    private final Map<Long, SecretKey> keyCacheByNoCia = new ConcurrentHashMap<>();

    public CompanyClientSecretService(@Value("${app.files.upload-dir:uploads}") String fallbackUploadDir) {
        this.fallbackUploadDir = fallbackUploadDir;
    }

    public SecretKey getAesKeyOrThrow(Compania compania) {
        Long noCia = compania.getNoCia();
        if (noCia == null) {
            throw new IllegalStateException("La compañía no tiene noCia para resolver client_secret");
        }
        return keyCacheByNoCia.computeIfAbsent(noCia, __ -> loadKeyOrThrow(compania));
    }

    private SecretKey loadKeyOrThrow(Compania compania) {
        Path root = resolveCompanyRoot(compania);
        Path propsPath = root.resolve(PARAMS_FILENAME);
        Properties props = new Properties();
        try (InputStream is = Files.newInputStream(propsPath)) {
            props.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + propsPath + " para obtener client_secret", e);
        }

        String clientSecretB64 = props.getProperty(CLIENT_SECRET_KEY);
        if (clientSecretB64 == null || clientSecretB64.isBlank()) {
            throw new IllegalStateException("No existe la propiedad '" + CLIENT_SECRET_KEY + "' en " + propsPath);
        }

        byte[] raw;
        try {
            raw = Base64.getDecoder().decode(clientSecretB64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("El 'client_secret' no es Base64 válido en " + propsPath, e);
        }

        if (raw.length != 32) {
            throw new IllegalStateException("El 'client_secret' debe decodificar a 32 bytes (AES-256) en " + propsPath);
        }

        return new SecretKeySpec(raw, "AES");
    }

    private Path resolveCompanyRoot(Compania compania) {
        String configured = compania.getPathFileServer();
        Path rootLocation;
        if (configured != null && !configured.isBlank()) {
            rootLocation = Paths.get(configured.trim());
            if (!rootLocation.isAbsolute()) {
                throw new IllegalArgumentException("El pathFileServer debe ser una ruta absoluta");
            }
        } else {
            rootLocation = Paths.get(fallbackUploadDir).resolve("cia_" + compania.getNoCia());
        }
        return rootLocation;
    }
}
