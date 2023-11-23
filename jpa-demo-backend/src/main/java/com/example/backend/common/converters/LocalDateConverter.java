package com.example.backend.common.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class LocalDateConverter implements Converter<String, LocalDate> {

	private final DateTimeFormatter formatter;

	public LocalDateConverter(String dateFormat) {
		formatter = DateTimeFormatter.ofPattern(dateFormat);
	}

	@Override
	public LocalDate convert(String source) {
		if (source.isEmpty()) {
			return null;
		}

		return LocalDate.parse(source, formatter);
	}
}
