package com.rabobank.customer.transaction.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class bootstraps the application.
 *
 */
@SpringBootApplication
@ComponentScan(basePackages="com.rabobank.customer.*")
@EnableAutoConfiguration
public class RaboCustStmtProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaboCustStmtProcessorApplication.class, args);
	}

}
