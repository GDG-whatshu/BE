package com.whatshu.whatshu_be.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 출석 제출 요청용 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceRequestDto {
    @NotBlank(message = "이름은 공백일 수 없습니다.") // 👈 피드백 반영
    private String name;
    private String comment;
}