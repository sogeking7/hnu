package common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaveResult(@NotNull UUID id) {
	public static SaveResult of(UUID id) {
		return new SaveResult(id);
	}
}
