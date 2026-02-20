package com.whatshu.whatshu_be.session.controller;

import com.whatshu.whatshu_be.session.dto.SessionCreateRequestDto;
import com.whatshu.whatshu_be.session.dto.SessionDetailResponseDto;
import com.whatshu.whatshu_be.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "세션(Session) API", description = "세션 생성 및 상세 명단 조회 기능을 제공합니다.") // 스웨거용 설명
@RestController // 🌟 스웨거가 이 파일을 찾게 해주는 핵심 이름표!
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // 1. 세션 생성 API
    @Operation(summary = "세션 생성", description = "새로운 세션을 생성하고 해당 기수 전체 멤버를 '결석' 상태로 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> createSession(@RequestBody SessionCreateRequestDto requestDto) {
        Long sessionId = sessionService.createSession(requestDto);
        return ResponseEntity.ok(sessionId);
    }

    // 2. 세션 상세 페이지 조회 API
    @Operation(summary = "세션 상세 조회", description = "특정 세션의 정보와 출석, 방문, 결석자 명단을 조회합니다.")
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDetailResponseDto> getSessionDetail(@PathVariable Long sessionId) {
        SessionDetailResponseDto responseDto = sessionService.getSessionDetail(sessionId);
        return ResponseEntity.ok(responseDto);
    }
}