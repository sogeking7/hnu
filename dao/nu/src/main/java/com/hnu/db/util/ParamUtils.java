package com.hnu.db.util;

import jakarta.validation.constraints.NotNull;

public final class ParamUtils {
	public static Integer limit(Integer value, @NotNull Integer defaultValue) {
		if (value == null || value <= 0) {
			return defaultValue;
		}
		return value;
	}

	public static Integer page(Integer value, @NotNull Integer defaultValue) {
		if (value == null || value < 1) {
			return defaultValue;
		}
		return value;
	}
}
