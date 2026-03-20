package com.kamis.price.controller;

import com.kamis.price.batch.kamis.price.dto.BatchRunResponse;
import com.kamis.price.batch.kamis.price.dto.BatchStatusResponse;
import com.kamis.price.batch.kamis.price.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    /**
     * 배치 수동 실행
     *
     * POST /api/batch/run?itemCategoryCode=200&regDay=2024-01-15
     */
    @PostMapping("/run")
    public BatchRunResponse runBatch(
            @RequestParam(name = "itemCategoryCode", defaultValue = "200") String itemCategoryCode,
            @RequestParam(name = "regDay", required = false) String regDay
    ) {
        if (regDay == null || regDay.isBlank()) {
            regDay = LocalDate.now().toString();
        }

        return batchService.runBatch(itemCategoryCode, regDay);
    }

    /**
     * 배치 실행 이력 조회
     */
    @GetMapping("/status")
    public BatchStatusResponse getBatchStatus() {
        return batchService.getBatchStatus();
    }


}
