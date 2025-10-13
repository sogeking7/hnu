package com.hnu.db.user.dto;

import com.hnu.db.jooq.model.tables.records.NuUserRecord;
import common.util.Strings;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto(
	@NotNull UUID id,
	@NotNull Boolean removed,
	OffsetDateTime createDate,
	OffsetDateTime modifyDate,
	UUID modifyUserId,
	@NotNull Boolean enabled,
	@NotNull String phone,
	String firstname,
	String lastname,
	String patronymic
) {

	public static final String className = "User";

	public static @Nullable UserDto ofNullable(@Nullable NuUserRecord record) {
		if (record == null || record.getId() == null) {
			return null;
		}
		return of(record);
	}

	public static @NotNull UserDto of(@NotNull NuUserRecord record) {
		return new UserDto(
			record.getId(),
			record.getRemoved(),
			record.getCreateDate(),
			record.getModifyDate(),
			record.getModifyUserId(),
			record.getEnabled(),
			record.getPhone(),
			record.getFirstname(),
			record.getLastname(),
			record.getPatronymic()
		);
	}

	public String fullname() {
		return Strings.concat(" ", lastname, firstname, patronymic);
	}
}
