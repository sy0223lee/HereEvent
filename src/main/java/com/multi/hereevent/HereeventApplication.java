package com.multi.hereevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // @Scheduled 어노테이션을 사용하기 위해 추가
@SpringBootApplication
public class HereeventApplication {

	public static void main(String[] args) {
		SpringApplication.run(HereeventApplication.class, args);
	}

}