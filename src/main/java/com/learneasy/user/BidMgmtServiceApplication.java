package com.learneasy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.learneasy.user.infrastructure.mapper"})
public class BidMgmtServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");

		SpringApplication.run(BidMgmtServiceApplication.class, args);
	}

}
