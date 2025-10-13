package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TgInlineKeyboardButton {

	private String text;

	@JsonProperty("callback_data")
	private String callbackData;

	public TgInlineKeyboardButton() {}

	public TgInlineKeyboardButton(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public TgInlineKeyboardButton setText(String text) {
		this.text = text;
		return this;
	}

	public String getCallbackData() {
		return callbackData;
	}

	public TgInlineKeyboardButton setCallbackData(String callbackData) {
		this.callbackData = callbackData;
		return this;
	}
}
