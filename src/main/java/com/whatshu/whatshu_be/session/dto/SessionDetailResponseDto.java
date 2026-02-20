package com.whatshu.whatshu_be.session.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class SessionDetailResponseDto {
    private String title;       // 세션명 (예: 1주차 정규세션)
    private String type;        // 세션 종류
    private String date;        // 세션 날짜 및 시간

    // 프론트엔드에서 명단을 화면에 그릴 수 있도록 리스트 형태로 담아줍니다.
    private List<MemberInfo> presentMembers; // 출석자 목록
    private List<GuestInfo> guests;          // 방문자 목록
    private List<MemberInfo> absentMembers;  // 결석자 목록

    // 명단에 들어갈 멤버의 상세 정보 (내부 클래스로 작성)
    @Getter @Builder
    public static class MemberInfo {
        private Long attendanceId; // 나중에 '수동 출석' 처리를 할 때 필요함
        private String name;       // 이름
        private String time;       // 출석 시간 (예: "13:50")
    }

    // 방문자 상세 정보
    @Getter @Builder
    public static class GuestInfo {
        private String guestName;
        private String time;
    }
}