package com.whatshu.whatshu_be.session.dto;

import lombok.Getter;
import java.time.LocalDateTime; // 또는 LocalDate (기존 설정에 맞게)

@Getter
public class SessionCreateRequestDto {
    private String title;
    private LocalDateTime date;
    private String type;
    private String description;
    private Byte cohortNo;
}