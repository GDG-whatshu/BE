package com.whatshu.whatshu_be.qrtoken.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class QrTokenResponseDto {

    private String token;

    public static QrTokenResponseDto of(String token) {
        return QrTokenResponseDto.builder()
                .token(token)
                .build();
    }
}
