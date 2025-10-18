package com.hnu.api.goals.model;

import com.hnu.db.goals.dto.GoalDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GoalModel(
	@NotNull UUID id,
	OffsetDateTime createDate,
	@NotNull UUID userId,
	@NotNull String name,
	@NotNull Integer durationMonth,
	@NotNull Double monthlyInvest,
	@NotNull Double target,
	@NotNull LocalDateTime estimatedDate,
	@NotNull Double balance,
	@NotNull Boolean completed
) {
	public static @NotNull GoalModel of(@NotNull GoalDto dto) {
		return new GoalModel(
			dto.id(),
			dto.createDate(),
			dto.userId(),
			dto.name(),
			dto.durationMonth(),
			dto.monthlyInvest(),
			dto.target(),
			dto.estimatedDate(),
			dto.balance(),
			dto.completed()
		);
	}
}
