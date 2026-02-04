package com.whatshu.whatshu_be.membership.controller;

import com.whatshu.whatshu_be.membership.dto.CohortResponseDto;
import com.whatshu.whatshu_be.membership.service.CohortService;
import com.whatshu.whatshu_be.global.common.CommonResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cohorts")
@RequiredArgsConstructor
public class CohortController {

    private final CohortService cohortService;

    @GetMapping
    public ResponseEntity<CommonResponseBody<List<CohortResponseDto>>> getAllCohorts() {
        List<CohortResponseDto> data = cohortService.getAllCohorts();

        return ResponseEntity.ok(CommonResponseBody.success(data));
    }
}
