package com.data.desensitization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.data.desensitization.controller.SqlPraserController;;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
public class DataDesensitizationApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DataDesensitizationApplication.class, args);
	}

}
