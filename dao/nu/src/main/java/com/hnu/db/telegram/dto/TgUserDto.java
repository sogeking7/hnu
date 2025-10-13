package com.hnu.db.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hnu.db.jooq.model.tables.records.TgUserRecord;
import common.util.Booleans;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TgUserDto(
	UUID id,
	Boolean removed,
	OffsetDateTime createDate,
	OffsetDateTime modifyDate,
	String phone,
	Long telegramUserId,
	Long telegramChatId,
	String telegramUsername,
	String telegramLanguageCode,
	String lastname,
	String firstname,
	Boolean enabled
) {

	public static final String RU = "ru";
	public static final String KK = "kk";
	public static final String EN = "en";

	public static TgUserDto of(TgUserRecord record) {
		return new TgUserDto(
			record.getId(),
			record.getRemoved(),
			record.getCreateDate(),
			record.getModifyDate(),
			record.getPhone(),
			record.getTelegramUserId(),
			record.getTelegramChatId(),
			record.getTelegramUsername(),
			record.getTelegramLanguageCode(),
			record.getLastname(),
			record.getFirstname(),
			record.getEnabled()
		);
	}

	@JsonIgnore
	public boolean isActive() {
		return Booleans.val(enabled);
	}
}
