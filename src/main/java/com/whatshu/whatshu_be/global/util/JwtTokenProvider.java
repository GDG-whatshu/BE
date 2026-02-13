package com.whatshu.whatshu_be.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds;

    // 생성자: application.properties에서 비밀키와 만료시간(분)을 가져옴
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration}") long validityInMinutes) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMinutes * 60 * 1000; // 분 -> 밀리초 변환
    }

    // 1. 토큰 생성
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email); // sub(subject)에 이메일 저장
        claims.put("role", role); // claim에 권한 정보 저장

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 가져오기
        String role = claims.get("role", String.class);

        // UserDetails 객체 생성 (비밀번호는 빈 문자열)
        UserDetails principal = new User(claims.getSubject(), "",
                Collections.singletonList(new SimpleGrantedAuthority(role)));

        // 팩토리 메서드 사용 (권장 방식)
        // 기존: return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        return UsernamePasswordAuthenticationToken.authenticated(principal, "", principal.getAuthorities());
    }

    // 3. 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 4. 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 만료되었거나, 위조되었거나, 형식이 잘못된 경우 등
            return false;
        }
    }
}