package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

// https://core.telegram.org/bots/api#chat
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgChat(
	Long id,
	String type, //“private”, “group”, “supergroup” or “channel”
	String firstName
) {
	@JsonIgnore
	public boolean isPrivate() {
		return "private".equals(type);
	}
}
