package com.whatshu.whatshu_be.auth.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder //.bulider()로 사용
public class Account {

    private Long accountId;
    private String email;
    private String password;
    private Role role;

}
