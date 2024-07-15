package com.dexter.gcv_life;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class GcvLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcvLifeApplication.class, args);
		System.out.println("********************"+"\"Application started\"********************");
	}

}
