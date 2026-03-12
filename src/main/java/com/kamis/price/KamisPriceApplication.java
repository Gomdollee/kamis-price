package com.kamis.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KamisPriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KamisPriceApplication.class, args);
	}

}
