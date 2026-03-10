package com.whatshu.whatshu_be.membership.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CohortMemberAttendanceResponseDto {

    private int memberCount;
    private int sessionCount;
    private List<MemberAttendance> items;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberAttendance {

        private Long memberId;
        private String name;
        private String role;
        private int attendedCount;
        private List<MemberSessionAttendance> attendances;

        public static MemberAttendance of(Long memberId, String name, String role, int attendedCount, List<MemberSessionAttendance> attendances) {
            return MemberAttendance.builder()
                    .memberId(memberId)
                    .name(name)
                    .role(role)
                    .attendedCount(attendedCount)
                    .attendances(attendances)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberSessionAttendance {

        private Long memberId;
        private String type;
        private LocalDate date;
        private Boolean isPresent;
    }

    public static CohortMemberAttendanceResponseDto of(int memberCount, int sessionCount, List<MemberAttendance> items) {
        return CohortMemberAttendanceResponseDto.builder()
                .memberCount(memberCount)
                .sessionCount(sessionCount)
                .items(items)
                .build();
    }
}
