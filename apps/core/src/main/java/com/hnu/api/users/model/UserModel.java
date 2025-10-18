package com.hnu.api.users.model;

import com.hnu.db.user.dto.UserDto;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserModel(
	@NotNull UUID id,
	@NotNull OffsetDateTime createDate,
	@NotNull Boolean enabled,
	@NotNull String phone,
	@NotNull String firstname,
	String lastname,
	String patronymic,
	String uuin,
	OffsetDateTime birthDate
) {

	public static @NotNull UserModel of(@NotNull UserDto dto) {
		return new UserModel(
			dto.id(),
			dto.createDate(),
			dto.enabled(),
			dto.phone(),
			dto.firstname(),
			dto.lastname(),
			dto.patronymic(),
			dto.iin(),
			dto.birthDate()
		);
	}
}
