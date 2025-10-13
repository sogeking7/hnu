package com.hnu.db.session.dto;

import com.hnu.db.jooq.model.tables.records.NuSessionRecord;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record SessionDto(
	@NotNull String sessionId,
	Boolean removed,
	@NotNull OffsetDateTime createDate,
	OffsetDateTime modifyDate,
	@NotNull UUID userId,
	@NotNull String phone,
	@NotNull SessionData data,
	OffsetDateTime expireDate,
	UUID deviceId
) {

	public static SessionDto of(@NotNull NuSessionRecord record) {
		return new SessionDto(
			record.getSessionId(),
			record.getRemoved(),
			record.getCreateDate(),
			record.getModifyDate(),
			record.getUserId(),
			record.getPhone(),
			Optional.ofNullable(record.getData())
				.map(data -> new JsonObject(data.data()))
				.orElseGet(JsonObject::of)
				.mapTo(SessionData.class),
			record.getExpireDate(),
			record.getDeviceId()
		);
	}
}
