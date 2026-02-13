package com.whatshu.whatshu_be.membership.service;

import com.whatshu.whatshu_be.membership.dto.CohortRequestDto;
import com.whatshu.whatshu_be.membership.dto.CohortResponseDto;
import com.whatshu.whatshu_be.membership.entity.Cohort;
import com.whatshu.whatshu_be.membership.mapper.CohortMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CohortService {

    private final CohortMapper cohortMapper;

    public List<CohortResponseDto> getAllCohorts() {
        List<Cohort> cohorts = cohortMapper.findAllCohorts();

        List<CohortResponseDto> cohortsListResponse = cohorts.stream().map(CohortResponseDto::from).toList();

        return cohortsListResponse;
    }

    public CohortResponseDto createCohort(CohortRequestDto cohortRequestDto) {
        if (cohortMapper.findCohortByCohortNo(cohortRequestDto.getCohortNo()).isPresent()) {
            throw new IllegalArgumentException("cohortNo에 해당하는 기수 데이터가 이미 존재합니다.");
        }

        Cohort cohort = Cohort.from(cohortRequestDto);

        cohortMapper.insertCohort(cohort);

        CohortResponseDto cohortResponse = CohortResponseDto.from(cohort);

        return cohortResponse;
    }
}
