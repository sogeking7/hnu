package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TgReplyMarkup {
	@Nullable
	private Boolean removeKeyboard;
	@Nullable
	private List<List<TgKeyboardButton>> keyboard;
	@Nullable
	private Boolean oneTimeKeyboard;


	@Nullable
	@JsonProperty("inline_keyboard")
	private List<List<TgInlineKeyboardButton>> inlineKeyboard;

	public static TgReplyMarkup requestContact(String text) {
		return new TgReplyMarkup()
			.setOneTimeKeyboard(true)
			.setKeyboard(List.of(List.of(new TgKeyboardButton().setText(text).setRequestContact(true))));
	}

	public static TgReplyMarkup removeMarkup() {
		return new TgReplyMarkup().setRemoveKeyboard(true);
	}

	@Nullable
	public Boolean getRemoveKeyboard() {
		return removeKeyboard;
	}

	public TgReplyMarkup setRemoveKeyboard(Boolean removeKeyboard) {
		this.removeKeyboard = removeKeyboard;
		return this;
	}

	public List<List<TgKeyboardButton>> getKeyboard() {
		return keyboard;
	}

	public TgReplyMarkup setKeyboard(List<List<TgKeyboardButton>> keyboard) {
		this.keyboard = keyboard;
		return this;
	}

	@Nullable
	public Boolean getOneTimeKeyboard() {
		return oneTimeKeyboard;
	}

	public TgReplyMarkup setOneTimeKeyboard(Boolean oneTimeKeyboard) {
		this.oneTimeKeyboard = oneTimeKeyboard;
		return this;
	}

	public List<List<TgInlineKeyboardButton>> getInlineKeyboard() {
		return inlineKeyboard;
	}

	public TgReplyMarkup setInlineKeyboard(List<List<TgInlineKeyboardButton>> inlineKeyboard) {
		this.inlineKeyboard = inlineKeyboard;
		return this;
	}
}
