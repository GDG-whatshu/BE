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
    private final JwtTokenProvider jwtTokenProvider; // 토큰 주입


    //로그인 로직

    public String login(LoginRequestDto dto) {
        // 1. 이메일로 회원 조회
        Account account = accountMapper.findByEmail(dto.getEmail());
        if (account == null) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }

        // 2. 비밀번호 검증 (입력받은 비번 vs DB 암호화된 비번)
        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // 3. 인증 성공. 토큰 생성해서 반환
        // role은 Enum이라서 String으로 변환해서 넣어줌
        return jwtTokenProvider.createToken(account.getEmail(), account.getRole().name());
    }
}
