package com.client.telegram.dto;

public record TgSendMessageResponse(Boolean ok, TgMessage result, String description) {
}
