package com.whatshu.whatshu_be.auth.controller;

import com.whatshu.whatshu_be.auth.dto.SignupRequestDto;
import com.whatshu.whatshu_be.auth.dto.SignupResponseDto;
import com.whatshu.whatshu_be.auth.service.SignupService;
import com.whatshu.whatshu_be.global.common.CommonResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseBody<SignupResponseDto>> signup(@RequestBody SignupRequestDto signupRequestDto) {

        signupService.signup(signupRequestDto);

        SignupResponseDto responseDto = SignupResponseDto.of(signupRequestDto.getEmail());

        //ok: 상태코드 200(성공) ()안에 실제 데이터 담아 보냄
        return ResponseEntity.ok(CommonResponseBody.success(responseDto));
    }
}
