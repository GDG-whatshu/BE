package com.whatshu.whatshu_be.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceResponseDto {
    private Long attendanceId;
    private String message;
}
