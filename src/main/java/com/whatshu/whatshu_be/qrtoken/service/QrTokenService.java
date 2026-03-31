package com.whatshu.whatshu_be.qrtoken.service;

import com.whatshu.whatshu_be.global.util.CryptoProvider;
import com.whatshu.whatshu_be.qrtoken.dto.QrTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrTokenService {

    private final CryptoProvider cryptoProvider;

    @Value("qr.token.exp.minutes")
    private int expMinutes;

    public QrTokenResponseDto generateToken(Long sessionId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", sessionId);
        claims.put("exp", Instant.now().plus(Duration.ofMinutes(expMinutes)));

        String token = cryptoProvider.encode(claims);

        return QrTokenResponseDto.of(token);
    }
}
