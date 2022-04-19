package com.ddr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class PostgreBootRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgreBootRestApplication.class, args);
	}

}
