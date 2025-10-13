package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgUpdate(@JsonProperty("update_id") Long updateId,
					   TgMessage message,
					   @JsonProperty("my_chat_member")
					   TgChatMemberUpdated myChatMember,
					   @JsonProperty("callback_query")
					   TgCallbackQuery callbackQuery) {

}
