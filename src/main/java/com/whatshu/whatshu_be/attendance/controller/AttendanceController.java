package com.whatshu.whatshu_be.attendance.controller;

import com.whatshu.whatshu_be.attendance.dto.AttendanceRequestDto;
import com.whatshu.whatshu_be.attendance.dto.AttendanceResponseDto;
import com.whatshu.whatshu_be.attendance.dto.SessionResponseDto;
import com.whatshu.whatshu_be.global.common.CommonResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.whatshu.whatshu_be.attendance.service.AttendanceService;


@RestController
@RequestMapping("sessions")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;


    // 출석 제출 API (출석하기 버튼 클릭 시)
    @PostMapping("/{sessionId}/attendances")
    public ResponseEntity<CommonResponseBody<AttendanceResponseDto>> submitAttendance(
            @PathVariable Long sessionId,
            @Valid @RequestBody AttendanceRequestDto request
    ) {
        // 세션ID랑 입력받은 요청(이름, 코멘트)만 서비스로 던집니다.
        AttendanceResponseDto response = attendanceService.submitAttendance(sessionId, request);

        return ResponseEntity.ok(CommonResponseBody.success(response));
    }
}