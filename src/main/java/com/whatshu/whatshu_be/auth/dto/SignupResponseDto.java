package com.whatshu.whatshu_be.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {

    private String email;
    //컨트롤러 코드를 깔끔하게 하기 위해 static 사용
    //of 메서드를 통해 이메일 저장
    public static SignupResponseDto of(String Email){
        return SignupResponseDto.builder()
                .email(Email)
                .build();

    }
}
