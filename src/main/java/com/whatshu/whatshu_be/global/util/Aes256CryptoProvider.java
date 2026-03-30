package com.whatshu.whatshu_be.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Aes256CryptoProvider implements CryptoProvider {

    private final Key key;
    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    public Aes256CryptoProvider(String secretKey) {

        byte[] keyBytes;

        try {
            // 1. Base64 디코딩 시도 (프로덕션 환경의 적절히 인코딩된 키)
            keyBytes = Base64.getDecoder().decode(secretKey);

            // Base64 디코딩이 성공하더라도 키 길이가 32바이트가 아니면 SHA-256 해싱
            if (keyBytes.length != 32) {
                keyBytes = hashToAes256Key(secretKey);
            }

        } catch (IllegalArgumentException e) {
            // 2. Base64 디코딩 실패 시 SHA-256 해싱 (개발 환경의 일반 문자열)
            keyBytes = hashToAes256Key(secretKey);
        }

        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 임의의 문자열을 SHA-256 해싱하여 32바이트 AES-256 키를 생성합니다.
     *
     * @param input 해싱할 문자열 (예: "i7LB.v*_AmCwjllt")
     * @return 32바이트 키 배열
     */
    private byte[] hashToAes256Key(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            // SHA-256은 JDK 표준 알고리즘이므로 이 예외는 발생하지 않음
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    @Override
    public String encode(Object data) {
        try {
            // 1. Cast data to Map and prepare for serialization
            @SuppressWarnings("unchecked")
            Map<String, Object> claims = (Map<String, Object>) data;
            Map<String, Object> serializableData = new HashMap<>();

            // Convert Instant to epoch milliseconds for MessagePack compatibility
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                if (entry.getValue() instanceof Instant) {
                    serializableData.put(entry.getKey(), ((Instant) entry.getValue()).toEpochMilli());
                } else {
                    serializableData.put(entry.getKey(), entry.getValue());
                }
            }

            // 2. MessagePack serialization
            byte[] messagePackBytes = objectMapper.writeValueAsBytes(serializableData);

            // 3. AES-GCM encryption
            // Generate random 12-byte IV
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);

            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

            // Encrypt the MessagePack bytes
            byte[] ciphertext = cipher.doFinal(messagePackBytes);

            // 4. Combine IV + ciphertext
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

            // 5. Base64 URL-safe encoding (no padding)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Failed to encode token", e);
        }
    }

    @Override
    public Object decode(String token) {
        try {
            // 1. Base64 URL-safe decoding
            byte[] combined = Base64.getUrlDecoder().decode(token);

            // 2. Extract IV and ciphertext
            byte[] iv = new byte[12];
            byte[] ciphertext = new byte[combined.length - 12];
            System.arraycopy(combined, 0, iv, 0, 12);
            System.arraycopy(combined, 12, ciphertext, 0, ciphertext.length);

            // 3. AES-GCM decryption
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            // Decrypt to get MessagePack bytes
            byte[] messagePackBytes = cipher.doFinal(ciphertext);

            // 4. MessagePack deserialization
            Map<String, Object> claims = objectMapper.readValue(
                messagePackBytes,
                new TypeReference<Map<String, Object>>() {}
            );

            // 5. Type conversions
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                if ("exp".equals(entry.getKey()) && entry.getValue() instanceof Number) {
                    // Convert epoch milliseconds back to Instant
                    result.put(entry.getKey(), Instant.ofEpochMilli(((Number) entry.getValue()).longValue()));
                } else if ("sid".equals(entry.getKey()) && entry.getValue() instanceof Number) {
                    // Ensure session ID is Long
                    result.put(entry.getKey(), ((Number) entry.getValue()).longValue());
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }

            // 6. Validate expiration time
            if (result.containsKey("exp")) {
                Instant expirationTime = (Instant) result.get("exp");
                if (expirationTime.isBefore(Instant.now())) {
                    throw new RuntimeException("Token has expired");
                }
            }

            return result;

        } catch (RuntimeException e) {
            // 만료 예외는 그대로 전파
            if (e.getMessage() != null && e.getMessage().contains("Token has expired")) {
                throw e;
            }
            throw new RuntimeException("Failed to decode token", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode token", e);
        }
    }
}
