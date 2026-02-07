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

    //회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        Account newAccount = Account.builder()
                .email(signupRequestDto.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(signupRequestDto.getRole()))
                .build();

        accountMapper.insertAccount(newAccount);
    }
}
