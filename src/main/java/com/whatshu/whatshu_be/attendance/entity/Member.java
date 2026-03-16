package com.whatshu.whatshu_be.attendance.entity; // 패키지 경로는 프로젝트에 맞게!

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private Long memberId;
    private Integer cohortNo;
    private String name;
    private String role; // 'CORE', 'MEMBER'
}