package com.whatshu.whatshu_be.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "인증 성공! 이 글자가 보인다면 당신은 로그인한 사람입니다.";
    }
}
