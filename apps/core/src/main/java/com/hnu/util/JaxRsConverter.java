package com.hnu.util;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

@Provider
public class JaxRsConverter implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
											  Annotation[] annotations) {
		if (rawType.equals(LocalDate.class)) {
			return (ParamConverter<T>) new LocalDateConverter();
		}
		return null;
	}

	static class LocalDateConverter implements ParamConverter<LocalDate> {

		@Override
		public LocalDate fromString(String value) {
			if (value == null) {
				return null;
			}
			return LocalDate.parse(value);
		}

		@Override
		public String toString(LocalDate value) {
			if (value == null) {
				return null;
			}
			return value.toString();
		}

	}
}


