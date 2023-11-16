package com.example.backend;

import com.example.persistence.PersistenceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(PersistenceApplication.class)
public class BackendApplication {

	public static final String APP_NAME = "myapp-backend";

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
