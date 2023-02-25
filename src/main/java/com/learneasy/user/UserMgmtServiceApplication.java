package com.learneasy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.learneasy.user.infrastructure.mapper"})
public class UserMgmtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMgmtServiceApplication.class, args);
	}

}
