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
    // MyBatis에서는 다른 클래스 자체를 넣기보다, 보통 ID(번호)만 저장하여 연결합니다.
    private Long attendanceId;
    private Long sessionId; // Session session 대신 아이디만!
    private Long accountId; // Account account 대신 아이디만! (방문자는 null)
    private String status;
    private LocalDateTime attendedAt;
    private String guestName;
}