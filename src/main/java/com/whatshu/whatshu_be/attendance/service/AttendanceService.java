package com.whatshu.whatshu_be.attendance.service;

import com.whatshu.whatshu_be.attendance.entity.Member;
import com.whatshu.whatshu_be.attendance.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whatshu.whatshu_be.attendance.dto.AttendanceRequestDto;
import com.whatshu.whatshu_be.attendance.dto.AttendanceResponseDto;
import com.whatshu.whatshu_be.attendance.dto.SessionResponseDto;
import com.whatshu.whatshu_be.attendance.mapper.AttendanceMapper;
import com.whatshu.whatshu_be.attendance.entity.Attendance;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;
    // private final SessionMapper sessionMapper; // 코드 올리면 주석 해제
    private final MemberMapper  memberMapper;

    @Transactional
    public AttendanceResponseDto submitAttendance(Long sessionId, AttendanceRequestDto request) {
        // TODO: 세션 코드 오면 세션의 실제 기수(cohortNo)를 가져오도록 수정
        Integer tempCohortNo = 7;

        Member member = memberMapper.findByCohortNoAndName(tempCohortNo, request.getName())
                .orElseThrow(() -> new IllegalArgumentException("등록된 실명과 일치하지 않습니다. 철자를 확인해주세요."));

        // 🚨 기존: existsBySessionIdAndUserId 로 확인하던 부분을 status 체크로 변경!
        String currentStatus = attendanceMapper.getAttendanceStatus(sessionId, member.getMemberId());

        if (currentStatus == null) {
            throw new IllegalArgumentException("해당 세션의 출석 명단에 존재하지 않습니다.");
        }
        if ("ATTEND".equals(currentStatus)) { // DB의 출석 상태값(예: ATTEND 또는 PRESENT)에 맞추세요
            throw new IllegalArgumentException("이미 출석이 완료된 세션입니다.");
        }

        attendanceMapper.updateAttendanceStatus(sessionId, member.getMemberId(), request.getComment());

        return AttendanceResponseDto.builder()
                // 업데이트의 경우 ID를 새로 생성하지 않으므로, memberId나 sessionId를 활용하거나 메시지만 반환
                .message("출석이 성공적으로 완료되었습니다!")
                .build();
    }
}