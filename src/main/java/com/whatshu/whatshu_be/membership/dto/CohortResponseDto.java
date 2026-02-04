package com.whatshu.whatshu_be.membership.dto;

import com.whatshu.whatshu_be.membership.entity.Cohort;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CohortResponseDto {

    private int cohortNo;
    private String organizer;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CohortResponseDto from(Cohort cohort) {
        return CohortResponseDto.builder()
                .cohortNo(cohort.getCohortNo())
                .organizer(cohort.getOrganizer())
                .startDate(cohort.getStartDate())
                .endDate(cohort.getEndDate())
                .build();
    }
}