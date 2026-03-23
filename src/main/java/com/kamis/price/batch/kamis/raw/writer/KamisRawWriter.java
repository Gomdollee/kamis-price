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
     * writer는 최종 저장 담당
     * upsert 사용 이유
     * - 같은 데이터가 다시 들어와도 중복 insert를 막기 위해
     * - 배치 재실행 시에도 안전하게 같은 키 기준으로 update 되게 하기 위해
     * - 즉, 멱등성을 보장하기 위해
     *
     * 현재 unique key 기준:
     * item_code + kind_code + rank_code + regday + country_code
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
