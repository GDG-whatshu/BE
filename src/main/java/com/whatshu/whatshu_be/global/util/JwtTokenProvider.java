package com.whatshu.whatshu_be.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds;

    // application.properties에서 값을 가져와서 세팅
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") long validityInMilliseconds) {
        // 비밀키를 암호화 알고리즘에 맞게 변환
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds * 60 * 1000;
    }

    // ★ 1. 토큰 생성 (로그인 성공 시 호출)
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email); // 토큰 제목(subject)에 이메일 저장
        claims.put("role", role); // "role": "MEMBER" 정보도 토큰 안에 넣음

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(validity) // 만료 시간 정보
                .signWith(key, SignatureAlgorithm.HS256) // 비밀키로 서명
                .compact();
    }

}