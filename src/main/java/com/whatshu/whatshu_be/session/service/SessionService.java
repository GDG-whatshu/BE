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
    private final AccountMapper accountMapper; // 🌟 AccountMapper 주입 (멤버 정보 가져오기용)

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

        // 2) 🌟 드디어 진짜 멤버를 가져옵니다! (해당 기수의 전체 멤버 조회)
        List<Account> cohortMembers = accountMapper.findAccountsByCohortNo(requestDto.getCohortNo());

        // 3) 멤버 수만큼 'ABSENT(결석)' 출석 데이터 생성
        List<Attendance> attendances = new ArrayList<>();
        // 가져온 회원이 있을 때만 실행 (NullPointerException 방지)
        if (cohortMembers != null && !cohortMembers.isEmpty()) {
            for (Account member : cohortMembers) {
                attendances.add(Attendance.builder()
                        .sessionId(newSessionId)
                        .accountId(member.getAccountId())
                        .status("ABSENT")
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
            String timeStr = att.getAttendedAt() != null ? att.getAttendedAt().format(timeFormatter) : "-";

            // 🌟 나중에 Account 테이블과 Join(조인)해서 진짜 이메일/이름을 가져오는 쿼리로 발전시키면 더 좋습니다!
            // 지금은 임시로 account_id를 이름 대신 뿌려줍니다.
            String memberNameInfo = "멤버_" + att.getAccountId();

            if ("PRESENT".equals(att.getStatus())) {
                if (att.getAccountId() != null) {
                    presentMembers.add(SessionDetailResponseDto.MemberInfo.builder()
                            .attendanceId(att.getAttendanceId())
                            .name(memberNameInfo)
                            .time(timeStr)
                            .build());
                } else {
                    guests.add(SessionDetailResponseDto.GuestInfo.builder()
                            .guestName(att.getGuestName())
                            .time(timeStr)
                            .build());
                }
            } else if ("ABSENT".equals(att.getStatus())) {
                absentMembers.add(SessionDetailResponseDto.MemberInfo.builder()
                        .attendanceId(att.getAttendanceId())
                        .name(memberNameInfo)
                        .time(timeStr)
                        .build());
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