package com.whatshu.whatshu_be.membership.controller;

import com.whatshu.whatshu_be.membership.dto.CohortRequestDto;
import com.whatshu.whatshu_be.membership.dto.CohortResponseDto;
import com.whatshu.whatshu_be.membership.service.CohortService;
import com.whatshu.whatshu_be.global.common.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<CommonResponseBody<CohortResponseDto>> createCohort(@Valid @RequestBody CohortRequestDto cohortRequestDto) {
        CohortResponseDto data = cohortService.createCohort(cohortRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponseBody.success(data));
    }
}
