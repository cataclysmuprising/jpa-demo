package com.example.persistence.config;

import com.example.persistence.utils.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class PersistenceProfiler {

	@Configuration
	@Profile("default")
	@PropertySource(value = "classpath:myapp-persistence-dev.yml", factory = YamlPropertySourceFactory.class)
	static class Default {}

	@Configuration
	@Profile("dev")
	@PropertySource(value = "classpath:myapp-persistence-dev.yml", factory = YamlPropertySourceFactory.class)
	static class Development {}

	@Configuration
	@Profile("prd")
	@PropertySource(value = "classpath:myapp-persistence-prd.yml", factory = YamlPropertySourceFactory.class)
	static class Production {}
}
