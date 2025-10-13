package com.hnu.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserSession(
	@NotNull OffsetDateTime createDate,
	@NotNull OffsetDateTime modifyDate,
	@NotNull OffsetDateTime expireDate,
	@NotNull String sessionId,
	@NotNull UUID userId,
	@NotNull String phone,
	@NotNull String firstname,
	@NotNull Boolean superAdmin
) {
	@JsonIgnore
	public boolean isSuperAdmin() {
		return superAdmin;
	}
}
