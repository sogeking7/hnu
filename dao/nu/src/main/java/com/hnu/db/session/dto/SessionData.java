package com.hnu.db.session.dto;

import io.vertx.core.json.JsonObject;

import java.util.UUID;

public record SessionData(
	String userAgent,
	String ip,
	JsonObject deviceInfo,
	Boolean superAdmin
) {
}
