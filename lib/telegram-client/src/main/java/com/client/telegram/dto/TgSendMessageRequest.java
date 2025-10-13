package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TgSendMessageRequest(
	@JsonProperty("chat_id") Long chatId,
	String text,
	@JsonProperty("reply_markup") TgReplyMarkup replyMarkup,
	@JsonProperty("parse_mode") String parseMode
) {
	public TgSendMessageRequest(Long chatId, String text, TgReplyMarkup replyMarkup) {
		this(chatId, text, replyMarkup, null);
	}
}
