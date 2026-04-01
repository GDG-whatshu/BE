package com.whatshu.whatshu_be.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Aes256CryptoProviderTest {

    private Aes256CryptoProvider aes256CryptoProvider;
    private String testKeyString;

    @BeforeEach
    void setUp() throws Exception {
        // Generate a test AES-256 key and encode to Base64 string
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey testKey = keyGen.generateKey();
        testKeyString = java.util.Base64.getEncoder().encodeToString(testKey.getEncoded());
        aes256CryptoProvider = new Aes256CryptoProvider(testKeyString);
    }

    @Test
    @DisplayName("encode는 null이 아닌 Base64 URL-safe 토큰을 반환해야 함")
    void encode_shouldReturnNonNullBase64Token() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123456L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // When
        String token = aes256CryptoProvider.encode(claims);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Base64 URL-safe는 +, /, = 문자를 포함하지 않음
        assertFalse(token.contains("+"));
        assertFalse(token.contains("/"));
        assertFalse(token.contains("="));
    }

    @Test
    @DisplayName("토큰 길이는 100자 이하여야 함 (MessagePack 최적화)")
    void encode_shouldProduceShortToken() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 999999999L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // When
        String token = aes256CryptoProvider.encode(claims);

        // Then
        System.out.println("Token length: " + token.length());
        System.out.println("Token: " + token);
        assertTrue(token.length() < 100, "Token should be under 100 characters");
    }

    @Test
    @DisplayName("encode-decode 라운드트립 테스트: 데이터 무결성 검증")
    void encodeDecode_shouldPreserveDataIntegrity() {
        // Given
        Instant originalExpiration = Instant.now().plusSeconds(3600);
        Map<String, Object> originalClaims = new HashMap<>();
        originalClaims.put("sid", 123456L);
        originalClaims.put("exp", originalExpiration);

        // When
        String token = aes256CryptoProvider.encode(originalClaims);
        @SuppressWarnings("unchecked")
        Map<String, Object> decodedClaims = (Map<String, Object>) aes256CryptoProvider.decode(token);

        // Then
        assertNotNull(decodedClaims);
        assertEquals(123456L, decodedClaims.get("sid"));
        assertTrue(decodedClaims.get("exp") instanceof Instant);

        // Instant는 밀리초 정밀도로 변환되므로, 밀리초 단위로 비교
        Instant decodedExpiration = (Instant) decodedClaims.get("exp");
        assertEquals(originalExpiration.toEpochMilli(), decodedExpiration.toEpochMilli());
    }

    @Test
    @DisplayName("작은 세션 ID로 라운드트립 테스트")
    void encodeDecode_withSmallSessionId() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 1L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // When
        String token = aes256CryptoProvider.encode(claims);
        @SuppressWarnings("unchecked")
        Map<String, Object> decodedClaims = (Map<String, Object>) aes256CryptoProvider.decode(token);

        // Then
        assertEquals(1L, decodedClaims.get("sid"));
    }

    @Test
    @DisplayName("큰 세션 ID로 라운드트립 테스트")
    void encodeDecode_withLargeSessionId() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 999999999L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // When
        String token = aes256CryptoProvider.encode(claims);
        @SuppressWarnings("unchecked")
        Map<String, Object> decodedClaims = (Map<String, Object>) aes256CryptoProvider.decode(token);

        // Then
        assertEquals(999999999L, decodedClaims.get("sid"));
    }

    @Test
    @DisplayName("동일한 데이터를 encode해도 다른 토큰이 생성되어야 함 (IV 무작위성)")
    void encode_shouldProduceDifferentTokensForSameData() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123456L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // When
        String token1 = aes256CryptoProvider.encode(claims);
        String token2 = aes256CryptoProvider.encode(claims);

        // Then
        assertNotEquals(token1, token2, "Each encode should produce different token due to random IV");
    }

    @Test
    @DisplayName("변조된 토큰은 decode 시 예외를 발생시켜야 함")
    void decode_withTamperedToken_shouldThrowException() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123456L);
        claims.put("exp", Instant.now().plusSeconds(3600));
        String token = aes256CryptoProvider.encode(claims);

        // When: 토큰의 마지막 문자를 반드시 다른 문자로 변경
        char lastChar = token.charAt(token.length() - 1);
        char replacedChar = (lastChar == 'A') ? 'B' : 'A';
        String tamperedToken = token.substring(0, token.length() - 1) + replacedChar;

        // Then
        assertThrows(RuntimeException.class, () -> {
            aes256CryptoProvider.decode(tamperedToken);
        });
    }

    @Test
    @DisplayName("잘못된 Base64 문자열은 decode 시 예외를 발생시켜야 함")
    void decode_withInvalidBase64_shouldThrowException() {
        // Given
        String invalidToken = "this-is-not-valid-base64!!!";

        // Then
        assertThrows(RuntimeException.class, () -> {
            aes256CryptoProvider.decode(invalidToken);
        });
    }

    @Test
    @DisplayName("다른 키로 decode 시 예외를 발생시켜야 함")
    void decode_withDifferentKey_shouldThrowException() throws Exception {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123456L);
        claims.put("exp", Instant.now().plusSeconds(3600));
        String token = aes256CryptoProvider.encode(claims);

        // When: 다른 키로 새 provider 생성
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey differentKey = keyGen.generateKey();
        String differentKeyString = java.util.Base64.getEncoder().encodeToString(differentKey.getEncoded());
        Aes256CryptoProvider differentProvider = new Aes256CryptoProvider(differentKeyString);

        // Then
        assertThrows(RuntimeException.class, () -> {
            differentProvider.decode(token);
        });
    }

    @Test
    @DisplayName("일반 문자열 secret도 정상적으로 처리되어야 함 (SHA-256 해싱)")
    void constructor_withPlainTextSecret_shouldWork() {
        // Given
        String plainSecret = "i7LB.v*_AmCwjllt";

        // When
        Aes256CryptoProvider provider = new Aes256CryptoProvider(plainSecret);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        // Then
        String token = provider.encode(claims);
        assertNotNull(token);

        @SuppressWarnings("unchecked")
        Map<String, Object> decoded = (Map<String, Object>) provider.decode(token);
        assertEquals(123L, decoded.get("sid"));
    }

    @Test
    @DisplayName("동일한 일반 문자열 secret은 항상 동일한 키를 생성해야 함")
    void constructor_withSamePlainTextSecret_shouldProduceSameKey() {
        // Given
        String secret = "i7LB.v*_AmCwjllt";

        // When
        Aes256CryptoProvider provider1 = new Aes256CryptoProvider(secret);
        Aes256CryptoProvider provider2 = new Aes256CryptoProvider(secret);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 999L);
        claims.put("exp", Instant.now().plusSeconds(3600));

        String token = provider1.encode(claims);

        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> decoded = (Map<String, Object>) provider2.decode(token);
        assertEquals(999L, decoded.get("sid"));
    }

    @Test
    @DisplayName("유효기간이 만료된 토큰은 decode 시 예외를 발생시켜야 함")
    void decode_withExpiredToken_shouldThrowException() {
        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", 123456L);
        // 1초 전에 만료된 토큰
        claims.put("exp", Instant.now().minusSeconds(1));

        // When
        String token = aes256CryptoProvider.encode(claims);

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            aes256CryptoProvider.decode(token);
        });
        assertTrue(exception.getMessage().contains("Token has expired"));
    }
}
