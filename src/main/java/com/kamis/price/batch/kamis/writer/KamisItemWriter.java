package com.kamis.price.batch.kamis.writer;

import com.kamis.price.domain.price.entity.PriceData;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class KamisItemWriter {

    @Bean
    public JdbcBatchItemWriter<PriceData> kamisWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<PriceData>()
                .dataSource(dataSource)
                .sql("""
           INSERT INTO price_data
                    (
                      source_type,
                      item_code,
                      item_name,
                      kind_code,
                      kind_name,
                      `rank`,
                      country_code,
                      country_name,
                      market_name,
                      unit,
                      price,
                      reg_day,
                      price_type,
                      created_at
                    )
                    VALUES
                    (
                      :sourceType,
                      :itemCode,
                      :itemName,
                      :kindCode,
                      :kindName,
                      :rank,
                      :countryCode,
                      :countryName,
                      :marketName,
                      :unit,
                      :price,
                      :regDay,
                      :priceType,
                      NOW()
                    )
                    ON DUPLICATE KEY UPDATE
                      price = VALUES(price),
                      created_at = NOW()
                    """)
                .beanMapped()
                .build();
    }
}
