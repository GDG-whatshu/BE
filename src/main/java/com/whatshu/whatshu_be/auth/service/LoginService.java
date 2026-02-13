package com.whatshu.whatshu_be.auth.service;

import com.whatshu.whatshu_be.auth.dto.LoginRequestDto;
import com.whatshu.whatshu_be.auth.entity.Account;
import com.whatshu.whatshu_be.auth.mapper.AccountMapper;
import com.whatshu.whatshu_be.global.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequestDto dto) {
        // 1. 이메일로 회원 조회
        Account account = accountMapper.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 2. 비밀번호 검증
        // matches(입력받은비번, DB의암호화된비번) 순서로 비교
        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 -> 토큰 발급
        return jwtTokenProvider.createToken(account.getEmail(), account.getRole().name());
    }
}
