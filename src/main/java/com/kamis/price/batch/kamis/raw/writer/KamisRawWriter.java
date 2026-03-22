package com.kamis.price.batch.kamis.raw.writer;

import com.kamis.price.domain.raw.entity.KamisRawItem;
import com.kamis.price.domain.raw.repository.KamisRawItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KamisRawWriter implements ItemWriter<KamisRawItem> {

    private final KamisRawItemRepository repository;


    /**
     * Raw item을 MySQL upsert로 저장합니다.
     * 같은 조건(item_code, kind_code, rank_code, regday, country_code)이 있으면 갱신합니다.
     */
    @Override
    public void write(Chunk<? extends KamisRawItem> items) {
        for (KamisRawItem item : items) {
            repository.upsert(
                    item.getRequest().getId(),
                    item.getCountryCode(),
                    item.getRegday(),
                    item.getCategoryCode(),
                    item.getItemName(),
                    item.getItemCode(),
                    item.getKindName(),
                    item.getKindCode(),
                    item.getRank(),
                    item.getRankCode(),
                    item.getUnit(),
                    item.getDay1(), item.getDpr1(),
                    item.getDay2(), item.getDpr2(),
                    item.getDay3(), item.getDpr3(),
                    item.getDay4(), item.getDpr4(),
                    item.getDay5(), item.getDpr5(),
                    item.getDay6(), item.getDpr6(),
                    item.getDay7(), item.getDpr7()
            );
        }
    }
}
