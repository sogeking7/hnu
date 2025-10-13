package com.client.telegram.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TgVideo(
	@NotNull String fileId,
	@NotNull String fileUniqueId,
	@NotNull Integer width,
	@NotNull Integer height,
	@NotNull Integer duration, //in seconds
	Integer fileSize,
	String fileName,
	String mimeType,
	Integer startTimestamp,
	List<TgPhotoSize> cover,
	TgPhotoSize thumbnail
) {
}
