package com.nttdataTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.nttdataTest"})
@SpringBootApplication
@EnableJpaRepositories("com.nttdataTest.dao")
@EntityScan("com.nttdataTest.model")
@Configuration
@EnableAutoConfiguration
public class NTTDataTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NTTDataTestApplication.class, args);
	}

}
