package com.whatshu.whatshu_be.membership.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CohortRequestDto {

    @NotNull(message = "기수 번호는 필수입니다.")
    @Positive(message = "기수 번호는 양수여야 합니다.")
    private int cohortNo;

    @NotBlank(message = "오거나이저는 필수입니다.")
    private String organizer;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;
}
