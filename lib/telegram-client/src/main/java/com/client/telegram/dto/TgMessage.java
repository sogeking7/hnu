package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgMessage(
	Integer messageId,
	TgUser from,
	String text,
	TgContact contact,
	@NotNull TgChat chat,
//	@JsonProperty("reply_markup") InlineKeyboardMarkup replyMarkup,
	TgLocation location,
	String caption,
	Long date,
	@JsonProperty("photo")
	List<TgPhotoSize> photos,
	TgDocument document,
	TgVideo video,
	TgVoice voice
) {
	@JsonIgnore
	public boolean isStartCommand() {
		return "/start".equals(this.text());
	}
}
