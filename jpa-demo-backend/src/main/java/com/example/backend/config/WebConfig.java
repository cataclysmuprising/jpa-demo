package com.example.backend.config;

import com.example.backend.utils.converters.DateTimeConverter;
import com.example.backend.utils.converters.LocalDateConverter;
import com.example.backend.utils.thymeleaf.ThymeleafLayoutInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.time.Duration;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		var templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/web/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(0);
		return templateResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ThymeleafLayoutInterceptor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// @formatter:off
		registry
				.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/")
				.setCacheControl(CacheControl.maxAge( Duration.ofDays(365)))
				.resourceChain(false);
		// @formatter:on

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new LocalDateConverter("dd-MM-yyyy"));
		registry.addConverter(new DateTimeConverter("dd-MM-yyyy HH:mm:ss"));
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:resourceBundles/messages", "classpath:resourceBundles/validation");
		messageSource.setCacheSeconds(3600 * 24 * 365); // refresh cache once per year
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		converters.add(converter);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new JsonOrgModule());

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

//		SimpleModule module = new SimpleModule();
//		module.addDeserializer(JsonNodeValue.class, new JsonNodeValueDeserializer());
//		objectMapper.registerModule(module);

		// Other options such as how to deal with nulls or identing...
		objectMapper.setTimeZone(TimeZone.getDefault());
		return objectMapper;
	}
}
