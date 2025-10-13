package com.hnu.model;

import jakarta.validation.constraints.NotNull;

public record StringResult(@NotNull String value) {
	public static @NotNull StringResult of(@NotNull String value) {
		return new StringResult(value);
	}
}
