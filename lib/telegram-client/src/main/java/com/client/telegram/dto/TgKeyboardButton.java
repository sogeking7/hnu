package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TgKeyboardButton {
	private String text;
	@Nullable
	private Boolean requestContact;

	public String getText() {
		return text;
	}

	public TgKeyboardButton setText(String text) {
		this.text = text;
		return this;
	}

	@Nullable
	public Boolean getRequestContact() {
		return requestContact;
	}

	public TgKeyboardButton setRequestContact(Boolean requestContact) {
		this.requestContact = requestContact;
		return this;
	}
}
