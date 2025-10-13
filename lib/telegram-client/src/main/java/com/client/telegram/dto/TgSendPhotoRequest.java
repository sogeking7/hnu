package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TgSendPhotoRequest(
	@NotNull @JsonProperty("chat_id") Long chatId,
	@NotNull String photo, //InputFile or string
	@JsonProperty("parse_mode") String parseMode,
	String caption,
	@JsonProperty("reply_markup") TgReplyMarkup replyMarkup
) {
}
