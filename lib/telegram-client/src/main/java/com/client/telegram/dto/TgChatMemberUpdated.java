package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/*
 	@see https://core.telegram.org/bots/api#chatmemberupdated
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgChatMemberUpdated(TgChat chat,
								  TgUser from,
								  Integer date,
								  @JsonProperty("old_chat_member") TgChatMember oldChatMember,
								  @JsonProperty("new_chat_member") TgChatMember newChatMember) {

}
