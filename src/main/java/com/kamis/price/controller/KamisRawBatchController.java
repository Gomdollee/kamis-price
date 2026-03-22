package com.kamis.price.controller;


import com.kamis.price.batch.kamis.raw.service.KamisRawBatchService;
import com.kamis.price.domain.raw.service.KamisRawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class KamisRawBatchController {

    private final KamisRawBatchService batchService;

    @PostMapping("/kamis/raw")
    public String runBatch(
            @RequestParam(name = "regday", defaultValue = "2026-03-20") String regday
    ) throws Exception {

        batchService.run(regday);

        return "배치 실행 완료: " + regday;
    }
}
