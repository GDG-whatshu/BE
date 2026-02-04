package com.whatshu.whatshu_be.membership.entity;

import com.whatshu.whatshu_be.membership.dto.CohortRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Cohort {

    private Integer cohortNo;
    private String organizer;
    private LocalDate startDate;
    private LocalDate endDate;

    public static Cohort from(CohortRequestDto dto) {
        return Cohort.builder()
                .cohortNo(dto.getCohortNo())
                .organizer(dto.getOrganizer())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
