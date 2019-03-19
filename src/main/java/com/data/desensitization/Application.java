package com.data.desensitization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.data.desensitization.controller.SqlPraserController;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
}
