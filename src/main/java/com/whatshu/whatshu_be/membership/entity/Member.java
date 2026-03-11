package com.whatshu.whatshu_be.membership.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private Long memberId;
    private Integer cohortNo;
    private String name;
    private String role;
}
