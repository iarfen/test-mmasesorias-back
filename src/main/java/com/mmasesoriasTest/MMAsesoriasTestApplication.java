package com.mmasesoriasTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.mmasesoriasTest"})
@SpringBootApplication
@EnableJpaRepositories("com.mmasesoriasTest.dao")
@EntityScan("com.mmasesoriasTest.model")
@Configuration
@EnableAutoConfiguration
public class MMAsesoriasTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MMAsesoriasTestApplication.class, args);
	}

}
