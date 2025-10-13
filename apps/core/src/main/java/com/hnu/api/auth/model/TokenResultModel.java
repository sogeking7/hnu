package com.hnu.api.auth.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record TokenResultModel(@NotNull String token) {
	public static TokenResultModel of(String token) {
		return new TokenResultModel(token);
	}
}
