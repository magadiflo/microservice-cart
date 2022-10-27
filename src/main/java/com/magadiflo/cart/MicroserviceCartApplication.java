package com.magadiflo.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroserviceCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCartApplication.class, args);
	}

}
