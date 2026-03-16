package com.whatshu.whatshu_be.attendance.entity;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Attendance {
    private Long id;            // 출석 고유 ID (PK)
    private Long sessionId;     // 어떤 세션인지 (다른 분이 만든 세션의 ID)
    private Long memberId;        // 누가 출석했는지 (로그인한 유저 ID)
    private String comment;     // 한줄 평 (선택)
    private LocalDateTime createdAt; // 출석한 시간
}