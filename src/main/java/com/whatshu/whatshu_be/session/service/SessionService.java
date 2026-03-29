package com.whatshu.whatshu_be.session.service;

import com.whatshu.whatshu_be.auth.entity.Account;
import com.whatshu.whatshu_be.auth.mapper.AccountMapper;
import com.whatshu.whatshu_be.session.dto.SessionCreateRequestDto;
import com.whatshu.whatshu_be.session.dto.SessionDetailResponseDto;
import com.whatshu.whatshu_be.session.entity.Attendance;
import com.whatshu.whatshu_be.session.entity.Session;
import com.whatshu.whatshu_be.session.mapper.AttendanceMapper;
import com.whatshu.whatshu_be.session.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionMapper sessionMapper;
    private final AttendanceMapper attendanceMapper;
    private final MemberMapper memberMapper;

    /**
     * 1. 세션 생성 및 전체 멤버 자동 결석 처리
     */
    @Transactional
    public Long createSession(SessionCreateRequestDto requestDto) {

        // 1) 세션 생성 및 저장
        Session session = Session.builder()
                .title(requestDto.getTitle())
                .date(requestDto.getDate())
                .type(requestDto.getType())
                .description(requestDto.getDescription())
                .cohortNo(requestDto.getCohortNo())
                .build();

        sessionMapper.insertSession(session);
        Long newSessionId = session.getSessionId();

        // 2) 해당 기수의 전체 멤버 조회
        List<Member> cohortMembers = MemberMapper.findMembersByCohortNo(requestDto.getCohortNo());

        // 3) 멤버 수만큼 'ABSENT(결석)' 출석 데이터 생성
        List<Attendance> attendances = new ArrayList<>();
        if (cohortMembers != null && !cohortMembers.isEmpty()) {
            for (Member member : cohortMembers) {
                // ERD 제약조건 반영: member_id가 있으므로 guest_name은 세팅하지 않음(null)
                attendances.add(Attendance.builder()
                        .sessionId(newSessionId)
                        .memberId(member.getMemberId()) // ERD의 member_id 필드에 대응
                        .status("ABSENT") // 기본값 ABSENT
                        .build());
            }
            // 4) 출석 기록 한 번에 DB에 저장
            attendanceMapper.insertAttendanceList(attendances);
        }

        return newSessionId;
    }

    /**
     * 2. 세션 상세 페이지 (명단) 조회
     */
    public SessionDetailResponseDto getSessionDetail(Long sessionId) {

        Session session = sessionMapper.selectSessionById(sessionId);
        List<Attendance> attendances = attendanceMapper.selectAttendancesBySessionId(sessionId);

        List<SessionDetailResponseDto.MemberInfo> presentMembers = new ArrayList<>();
        List<SessionDetailResponseDto.GuestInfo> guests = new ArrayList<>();
        List<SessionDetailResponseDto.MemberInfo> absentMembers = new ArrayList<>();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Attendance att : attendances) {
            // ERD 반영: attendance_time 필드 사용
            String timeStr = att.getAttendanceTime() != null ? att.getAttendanceTime().format(timeFormatter) : "-";

            if ("PRESENT".equals(att.getStatus())) {
                // ERD 조건: member_id가 NULL이 아니면 멤버, NULL이면 게스트
                if (att.getMemberId() != null) {
                    String memberNameInfo = "멤버_" + att.getMemberId();
                    presentMembers.add(SessionDetailResponseDto.MemberInfo.builder()
                            .attendanceId(att.getAttendanceId())
                            .name(memberNameInfo)
                            .time(timeStr)
                            .build());
                } else if (att.getGuestName() != null) {
                    guests.add(SessionDetailResponseDto.GuestInfo.builder()
                            .guestName(att.getGuestName())
                            .time(timeStr)
                            .build());
                }
            } else if ("ABSENT".equals(att.getStatus())) {
                // 결석은 초기 생성된 멤버 데이터에만 해당됨
                if (att.getMemberId() != null) {
                    String memberNameInfo = "멤버_" + att.getMemberId();
                    absentMembers.add(SessionDetailResponseDto.MemberInfo.builder()
                            .attendanceId(att.getAttendanceId())
                            .name(memberNameInfo)
                            .time(timeStr)
                            .build());
                }
            }
        }

        return SessionDetailResponseDto.builder()
                .title(session.getTitle())
                .type(session.getType())
                .date(session.getDate() != null ? session.getDate().toString() : "")
                .presentMembers(presentMembers)
                .guests(guests)
                .absentMembers(absentMembers)
                .build();
    }
}