package com.kamis.price.batch.kamis.raw.reader;

import com.kamis.price.external.kamis.dto.KamisItemDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class KamisApiReader implements ItemReader<KamisItemDto>, StepExecutionListener {

    private Iterator<KamisItemDto> iterator;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        List<KamisItemDto> items =
                (List<KamisItemDto>) stepExecution.getExecutionContext().get("items");

        this.iterator = items.iterator();
    }

    @Override
    public KamisItemDto read() {
        return (iterator != null && iterator.hasNext()) ? iterator.next() : null;
    }
}
