package com.dblog.dblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class DblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DblogApplication.class, args);
	}

}
