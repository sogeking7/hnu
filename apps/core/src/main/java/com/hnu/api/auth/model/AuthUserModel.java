package com.hnu.api.auth.model;

import com.hnu.db.user.dto.UserDto;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthUserModel(
	@NotNull UUID id,
	@NotNull String phone,
	@NotNull String firstname,
	String lastname,
	String patronymic
) {

	public static @NotNull AuthUserModel of(
		@NotNull UserDto user
	) {
		return new AuthUserModel(
			user.id(),
			user.phone(),
			user.firstname(),
			user.lastname(),
			user.patronymic()
		);
	}
}
