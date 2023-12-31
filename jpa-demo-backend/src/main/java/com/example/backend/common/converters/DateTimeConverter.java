package com.example.backend.common.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeConverter implements Converter<String, LocalDateTime> {

	private final DateTimeFormatter formatter;

	public DateTimeConverter(String dateFormat) {
		formatter = DateTimeFormatter.ofPattern(dateFormat);
	}

	@Override
	public LocalDateTime convert(String source) {
		if (source.isEmpty()) {
			return null;
		}

		return LocalDateTime.parse(source, formatter);
	}
}
