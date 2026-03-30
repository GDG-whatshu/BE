package com.whatshu.whatshu_be.qrtoken.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class QrValidationResponseDto {

    private Long sessionId;

    public static QrValidationResponseDto of(Long sessionId) {
        return QrValidationResponseDto.builder()
                .sessionId(sessionId)
                .build();
    }
}
