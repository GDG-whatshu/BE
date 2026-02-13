package com.whatshu.whatshu_be.global.filter;

import com.whatshu.whatshu_be.global.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. 헤더에서 토큰 꺼내기
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 2. 토큰이 있고, 유효하다면?
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰에서 정보(Authentication)를 꺼내서
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 4. "이 사람은 인증된 사람입니다"라고 도장 찍기 (SecurityContext에 저장)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터로 넘기기
        chain.doFilter(request, response);
    }
}