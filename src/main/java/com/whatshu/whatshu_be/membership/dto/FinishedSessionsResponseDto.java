package com.whatshu.whatshu_be.membership.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FinishedSessionsResponseDto {

    private int memberCount;
    private int sessionCount;
    private List<FinishedSession> sessions;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FinishedSession {

        private Long sessionId;
        private String type;
        private String title;
        private LocalDate date;
        private Integer presentMembers;
    }

    public static FinishedSessionsResponseDto of(int memberCount, int sessionCount, List<FinishedSession> sessions) {
        return FinishedSessionsResponseDto.builder()
                .memberCount(memberCount)
                .sessionCount(sessionCount)
                .sessions(sessions)
                .build();
    }
}
