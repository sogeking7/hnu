package com.hnu.api.auth.operation;

import jakarta.validation.constraints.NotNull;

public record ProfileUpdateRequest(
	@NotNull String firstname,
	String lastname,
	String patronymic
) {
}
