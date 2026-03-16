package com.whatshu.whatshu_be.attendance.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class SessionResponseDto {
    private Long sessionId;
    private Integer cohortNo;
    private String type;
    private String title;
    private LocalDate date;
}