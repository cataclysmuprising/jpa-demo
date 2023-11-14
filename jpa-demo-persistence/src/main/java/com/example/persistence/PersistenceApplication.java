package com.example.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@formatter:off
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		ErrorMvcAutoConfiguration.class})
//@formatter:on

@ComponentScan(basePackages = {"com.example.persistence", "annotations"})
public class PersistenceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PersistenceApplication.class, args);
	}
}
