package com.whatshu.whatshu_be.membership.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CohortRequestDto {

    private int cohortNo;
    private String organizer;
    private LocalDate startDate;
    private LocalDate endDate;
}
