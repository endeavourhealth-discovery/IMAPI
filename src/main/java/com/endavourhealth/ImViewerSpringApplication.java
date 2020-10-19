package com.endavourhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@EnableSwagger2
@EnableJpaRepositories("com.endavourhealth")
@ComponentScan(basePackages = "com.endavourhealth")
@EntityScan("com.endavourhealth")
public class ImViewerSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImViewerSpringApplication.class, args);
	}

}
