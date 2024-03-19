package ru.sber.codetasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class CodetasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodetasksApplication.class, args);
	}

}
