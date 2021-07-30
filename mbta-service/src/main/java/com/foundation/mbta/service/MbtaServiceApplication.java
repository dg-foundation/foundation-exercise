package com.foundation.mbta.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point for Service application
 */
@SpringBootApplication
@EnableJpaAuditing
public class MbtaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MbtaServiceApplication.class, args);

	}

}
