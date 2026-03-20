package com.kamis.price.batch.kamis.price.writer;

import com.kamis.price.domain.price.entity.PriceData;
import com.kamis.price.domain.price.repository.PriceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PriceWriter implements ItemWriter<List<PriceData>> {

    private final PriceDataRepository repository;

    @Override
    public void write(Chunk<? extends List<PriceData>> chunk) {

        // 2차원 리스트 → 1차원으로 평탄화
        List<PriceData> flatList = chunk.getItems().stream()
                .filter(Objects::nonNull) // null 제거
                .flatMap(List::stream)
                .toList();

        // DB 저장
        repository.saveAll(flatList);
    }
}
