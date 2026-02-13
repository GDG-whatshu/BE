package com.whatshu.whatshu_be.auth.service;

import com.whatshu.whatshu_be.auth.dto.SignupRequestDto;
import com.whatshu.whatshu_be.auth.entity.Account;
import com.whatshu.whatshu_be.auth.entity.Role;
import com.whatshu.whatshu_be.auth.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {


        // Optional 상자가 비어있지 않다면 에러
        if (accountMapper.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 3. 저장
        Account newAccount = Account.builder()
                .email(signupRequestDto.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(signupRequestDto.getRole()))
                .build();

        accountMapper.insertAccount(newAccount);
    }
}