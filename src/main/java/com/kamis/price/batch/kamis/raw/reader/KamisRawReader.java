package com.kamis.price.batch.kamis.raw.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamis.price.external.kamis.dto.KamisItemDto;
import com.kamis.price.external.kamis.dto.KamisResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KamisRawReader implements ItemReader<KamisItemDto>, StepExecutionListener {

    private Iterator<KamisItemDto> iterator;

    @Override
    public void beforeStep(StepExecution stepExecution) {

        List<KamisItemDto> items =
                (List<KamisItemDto>) stepExecution.getExecutionContext().get("items");

        if (items == null || items.isEmpty()) {
            this.iterator = Collections.emptyIterator();
            return;
        }

        this.iterator = items.iterator();
    }

    @Override
    public KamisItemDto read() {
        return (iterator != null && iterator.hasNext()) ? iterator.next() : null;
    }
}
