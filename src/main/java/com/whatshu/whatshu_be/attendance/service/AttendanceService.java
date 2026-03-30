package com.whatshu.whatshu_be.attendance.service;

import com.whatshu.whatshu_be.attendance.entity.Member;
import com.whatshu.whatshu_be.attendance.mapper.MemberMapper;
import com.whatshu.whatshu_be.session.mapper.SessionMapper;
import com.whatshu.whatshu_be.session.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whatshu.whatshu_be.attendance.dto.AttendanceRequestDto;
import com.whatshu.whatshu_be.attendance.dto.AttendanceResponseDto;
import com.whatshu.whatshu_be.attendance.mapper.AttendanceMapper;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;
    private final MemberMapper memberMapper;
    private final SessionMapper sessionMapper;

    @Transactional
    public AttendanceResponseDto submitAttendance(Long sessionId, AttendanceRequestDto request) {

        // SessionMapper를 이용해 DB에서 실제 세션 정보 가져오기
        Session session = sessionMapper.selectSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("해당 세션을 찾을 수 없습니다.");
        }

        // 세션 객체에서 기수(cohortNo) 뽑아오기
        Integer currentCohortNo = Integer.valueOf(session.getCohortNo());

        // 실제 기수와 입력한 이름으로 출석 대상 멤버 찾기
        Member member = memberMapper.findByCohortNoAndName(currentCohortNo, request.getName())
                .orElseThrow(() -> new IllegalArgumentException("등록된 실명과 일치하지 않습니다. 철자를 확인해주세요."));

        // 현재 출석 상태 확인 및 중복 출석 방지
        String currentStatus = attendanceMapper.getAttendanceStatus(sessionId, member.getMemberId());

        if (currentStatus == null) {
            throw new IllegalArgumentException("해당 세션의 출석 명단에 존재하지 않습니다.");
        }

        // 검증
        if ("PRESENT".equals(currentStatus) || "ATTEND".equals(currentStatus)) {
            throw new IllegalArgumentException("이미 출석이 완료된 세션입니다.");
        }

        // 출석 상태 업데이트 (ABSENT -> PRESENT)
        attendanceMapper.updateAttendanceStatus(sessionId, member.getMemberId(), request.getComment());

        return AttendanceResponseDto.builder()
                .message("출석이 성공적으로 완료되었습니다!")
                .build();
    }
}