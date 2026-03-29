package com.whatshu.whatshu_be.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    private Long attendanceId;      // 출석 고유 ID
    private Long sessionId;         // 세션 ID (외래키)

    // 🌟 변경 포인트 1: accountId -> memberId
    private Long memberId;          // 멤버 ID (멤버인 경우)

    private String guestName;       // 게스트 이름 (멤버가 아닌 경우)

    private String status;          // 출석 상태 (PRESENT, ABSENT, LATE 등)

    // 🌟 변경 포인트 2: attendedAt -> attendanceTime
    private LocalDateTime attendanceTime; // 실제 출석 체크한 시간
}