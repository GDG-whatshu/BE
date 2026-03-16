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



    //1. 세션 정보 조회 (Mock 데이터로 대체)
    public SessionResponseDto getSessionInfo(Long sessionId) {
        // TODO: 세션 코드 머지 되면 그때 가져오기
        // Session session = sessionMapper.findById(sessionId);

        System.out.println("임시 테스트용 가짜 세션 데이터를 반환합니다.");

        // 가짜 세션 데이터 리턴 (프론트엔드 UI 렌더링용)
        return SessionResponseDto.builder()
                .sessionId(sessionId)
                .cohortNo(7)
                .type("GTL")
                .title("GDG on Campus HUFS Tuesday Live (GTL) - Monthly(Online)")
                .date(LocalDate.of(2026, 3, 17))
                .build();
    }

    //2. 출석 제출 (DB 쓰기 작업 -> @Transactional)
    @Transactional
    public AttendanceResponseDto submitAttendance(Long sessionId, AttendanceRequestDto request) {

        // 1. 세션 정보 조회 (몇 기 세션인지 알아야 함)
        Session session = sessionMapper.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        // 2. 기수(cohort_no)와 입력한 이름(name)으로 멤버 명단에서 찾기!
        Member member = memberMapper.findByCohortNoAndName(session.getCohortNo(), request.getName())
                .orElseThrow(() -> new IllegalArgumentException("등록된 실명과 일치하지 않습니다. 철자를 확인해주세요."));

        // 3. 중복 출석 검증
        if (attendanceMapper.existsBySessionIdAndMemberId(sessionId, member.getMemberId())) {
            throw new IllegalArgumentException("이미 출석이 완료된 세션입니다.");
        }

        // 4. 출석 엔티티 생성 및 저장
        Attendance attendance = Attendance.builder()
                .sessionId(sessionId)
                .memberId(member.getMemberId())
                .comment(request.getComment())
                .build();

        attendanceMapper.insertAttendance(attendance);

        return AttendanceResponseDto.builder()
                .attendanceId(attendance.getId())
                .message("출석이 성공적으로 완료되었습니다!")
                .build();
    }
}