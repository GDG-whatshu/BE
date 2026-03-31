package com.whatshu.whatshu_be.qrtoken.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QrValidationRequestDto {

    @NotBlank(message = "토큰 정보는 필수입니다.")
    private String token;
}
