package com.devix.service.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.springframework.stereotype.Service;

@Service
public class AesGcmCryptoService {

    private static final String PREFIX = "enc:v1:";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LEN_BYTES = 12;
    private static final int TAG_LEN_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    public boolean isEncrypted(String value) {
        return value != null && value.startsWith(PREFIX);
    }

    public String encrypt(String plaintext, SecretKey key) {
        if (plaintext == null) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_LEN_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LEN_BITS, iv));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buf = ByteBuffer.allocate(iv.length + cipherBytes.length);
            buf.put(iv);
            buf.put(cipherBytes);
            String payload = Base64.getEncoder().encodeToString(buf.array());
            return PREFIX + payload;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo cifrar el valor", e);
        }
    }

    public String decrypt(String encrypted, SecretKey key) {
        if (encrypted == null) {
            return null;
        }
        if (!isEncrypted(encrypted)) {
            throw new IllegalArgumentException("El valor no está cifrado con el formato esperado");
        }
        try {
            String payload = encrypted.substring(PREFIX.length());
            byte[] all = Base64.getDecoder().decode(payload);
            if (all.length <= IV_LEN_BYTES) {
                throw new IllegalArgumentException("Payload cifrado inválido (muy corto)");
            }
            byte[] iv = new byte[IV_LEN_BYTES];
            byte[] cipherBytes = new byte[all.length - IV_LEN_BYTES];
            System.arraycopy(all, 0, iv, 0, IV_LEN_BYTES);
            System.arraycopy(all, IV_LEN_BYTES, cipherBytes, 0, cipherBytes.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LEN_BITS, iv));
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo descifrar el valor", e);
        }
    }

    public String decryptIfEncrypted(String value, SecretKey key) {
        if (value == null) {
            return null;
        }
        return isEncrypted(value) ? decrypt(value, key) : value;
    }
}
