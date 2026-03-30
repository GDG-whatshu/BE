package com.whatshu.whatshu_be.qrtoken.controller;

import com.whatshu.whatshu_be.global.common.CommonResponseBody;
import com.whatshu.whatshu_be.qrtoken.dto.QrTokenResponseDto;
import com.whatshu.whatshu_be.qrtoken.dto.QrValidationRequestDto;
import com.whatshu.whatshu_be.qrtoken.dto.QrValidationResponseDto;
import com.whatshu.whatshu_be.qrtoken.service.QrTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr-token")
@RequiredArgsConstructor
public class QrTokenController {

    private final QrTokenService qrTokenService;

    @GetMapping("/generate")
    public ResponseEntity<CommonResponseBody<QrTokenResponseDto>> generateQrTokenForSessionId(@RequestParam Long sessionId) {
        QrTokenResponseDto data = qrTokenService.generateToken(sessionId);

        return ResponseEntity.ok(CommonResponseBody.success(data));
    }

    @PostMapping("/validate")
    public ResponseEntity<CommonResponseBody<QrValidationResponseDto>> validateQrToken(@RequestBody QrValidationRequestDto request) {
        QrValidationResponseDto data = qrTokenService.validateToken(request.getToken());

        return ResponseEntity.ok(CommonResponseBody.success(data));
    }
}
