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

    private Long attendanceId;      // 출석 고유 ID (PK)
    private Long sessionId;         // 세션 ID (외래키)

    // 🌟 변경 포인트 1: accountId -> memberId
    private Long memberId;          // 멤버 ID (외래키, NULL이면 게스트를 의미)

    private String guestName;       // 게스트 이름 (memberId가 NULL일 때만 값 존재)

    // 🌟 추가 포인트: ERD 반영
    private String comment;         // 코멘트 (예: "재밌었어요")

    private String status;          // 출석 상태 ('PRESENT', 'ABSENT' / 기본값: 'ABSENT')

    // 🌟 변경 포인트 2: attendedAt -> attendanceTime
    private LocalDateTime attendanceTime; // 실제 출석 시간 (NULL 상태로 생성, 출석 시 UPDATE)
}