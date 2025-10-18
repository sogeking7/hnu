package com.hnu.api.goals.operation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GoalSaveRequest(
	UUID id,
	OffsetDateTime createDate,
	@NotNull String name,
	@NotNull Integer durationMonth,
	@NotNull Double monthlyInvest,
	@NotNull Double target,
	@NotNull LocalDateTime estimatedDate
) {

}
