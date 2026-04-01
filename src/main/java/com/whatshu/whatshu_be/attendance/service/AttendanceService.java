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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;
    private final MemberMapper memberMapper;
    private final SessionMapper sessionMapper;

    @Transactional
    public AttendanceResponseDto submitAttendance(Long sessionId, AttendanceRequestDto request) {

        // 1. DB에서 실제 세션 정보 가져오기
        Session session = sessionMapper.selectSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("해당 세션을 찾을 수 없습니다.");
        }

        Integer currentCohortNo = Integer.valueOf(session.getCohortNo());
        String requestName = request.getName();

        // 2. 프론트에서 넘어온 이름이 해당 기수(7기) 멤버 명단에 있는지 조회
        Optional<Member> optionalMember = memberMapper.findByCohortNoAndName(currentCohortNo, requestName);

        if (optionalMember.isPresent()) {

            // 기존 멤버 출석 로직
            Member member = optionalMember.get();

            String currentStatus = attendanceMapper.getAttendanceStatus(sessionId, member.getMemberId());

            if (currentStatus == null) {
                throw new IllegalArgumentException("해당 세션의 출석 명단에 존재하지 않습니다.");
            }

            if ("PRESENT".equals(currentStatus)) {
                throw new IllegalArgumentException("이미 출석이 완료된 세션입니다.");
            }

            // 출석 상태 업데이트 (ABSENT -> PRESENT) - 동시성 방어 로직 적용됨
            attendanceMapper.updateAttendanceStatus(sessionId, member.getMemberId(), request.getComment());

            return AttendanceResponseDto.builder()
                    .message("멤버 출석이 성공적으로 완료되었습니다!")
                    .build();

        } else {

            // 게스트 출석 로직
            // 중복 출석 방지 (같은 세션에 같은 이름의 게스트가 또 제출하는 것 방지)
            int guestCount = attendanceMapper.countGuestAttendance(sessionId, requestName);
            if (guestCount > 0) {
                throw new IllegalArgumentException("이미 출석이 완료된 게스트입니다.");
            }

            // 게스트 출석 기록 생성 (memberId는 null, guestName에 이름 저장)
            attendanceMapper.insertGuestAttendance(sessionId, requestName, request.getComment());

            return AttendanceResponseDto.builder()
                    .message("게스트 출석이 성공적으로 완료되었습니다!")
                    .build();
        }
    }
}