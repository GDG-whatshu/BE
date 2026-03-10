package com.whatshu.whatshu_be.membership.service;

import com.whatshu.whatshu_be.membership.dto.CohortMemberAttendanceResponseDto;
import com.whatshu.whatshu_be.membership.dto.CohortRequestDto;
import com.whatshu.whatshu_be.membership.dto.CohortResponseDto;
import com.whatshu.whatshu_be.membership.dto.FinishedSessionsResponseDto;
import com.whatshu.whatshu_be.membership.entity.Cohort;
import com.whatshu.whatshu_be.membership.entity.Member;
import com.whatshu.whatshu_be.membership.mapper.CohortMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public CohortResponseDto getCohortByCohortNo(int cohortNo) {
        Optional<Cohort> result = cohortMapper.findCohortByCohortNo(cohortNo);

        Cohort cohort = result.orElseThrow();

        CohortResponseDto cohortResponse = CohortResponseDto.from(cohort);

        return cohortResponse;
    }

    public FinishedSessionsResponseDto getFinishedSessionsByCohortNo(int cohortNo) {
        List<FinishedSessionsResponseDto.FinishedSession> finishedSessionsList = cohortMapper.findFinishedSessionsByCohortNo(cohortNo);
        List<Member> cohortMembers = cohortMapper.findMembersByCohortNo(cohortNo);

        FinishedSessionsResponseDto finishedSessionsResponse =
                FinishedSessionsResponseDto.of(cohortMembers.size(), finishedSessionsList.size(), finishedSessionsList);

        return finishedSessionsResponse;
    }

    public CohortMemberAttendanceResponseDto getCohortMemberAttendancesByCohortNo(int cohortNo) {
        List<FinishedSessionsResponseDto.FinishedSession> finishedSessionsList = cohortMapper.findFinishedSessionsByCohortNo(cohortNo);
        List<Member> cohortMembers = cohortMapper.findMembersByCohortNo(cohortNo);
        List<CohortMemberAttendanceResponseDto.MemberSessionAttendance> memberSessionAttendanceList =
                cohortMapper.findMemberSessionAttendancesByCohortNo(cohortNo);

        Map<Long, List<CohortMemberAttendanceResponseDto.MemberSessionAttendance>> attendanceMap =
                memberSessionAttendanceList
                        .stream()
                        .collect(Collectors.groupingBy(CohortMemberAttendanceResponseDto.MemberSessionAttendance::getMemberId));

        List<CohortMemberAttendanceResponseDto.MemberAttendance> memberAttendanceList = cohortMembers.stream().map(member -> {
            Long memberId = member.getMemberId();
            String memberName = member.getName();
            String memberRole = member.getRole();

            List<CohortMemberAttendanceResponseDto.MemberSessionAttendance> attendancesList = attendanceMap.getOrDefault(memberId, List.of());

            int attendedCount =
                    attendancesList.stream().filter(CohortMemberAttendanceResponseDto.MemberSessionAttendance::getIsPresent).toList().size();

            return CohortMemberAttendanceResponseDto.MemberAttendance.of(memberId, memberName, memberRole, attendedCount, attendancesList);
        }).toList();

        CohortMemberAttendanceResponseDto response =
                CohortMemberAttendanceResponseDto.of(cohortMembers.size(), finishedSessionsList.size(), memberAttendanceList);

        return response;
    }
}
