package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TgDocument(
	@NotNull String fileId,
	@NotNull String fileName,
	Integer fileSize,
	String fileUniqueId,
	String mimeType,
	TgPhotoSize thumbnail
) {

}
