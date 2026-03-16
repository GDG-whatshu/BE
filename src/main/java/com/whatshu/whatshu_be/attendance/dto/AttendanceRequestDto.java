package com.whatshu.whatshu_be.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 출석 제출 요청용 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceRequestDto {
    @NotBlank(message = "등록된 실명을 정확히 입력해주세요.")
    private String name;

    private String comment;
}
