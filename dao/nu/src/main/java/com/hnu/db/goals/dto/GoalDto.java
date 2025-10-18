package com.hnu.db.goals.dto;

import com.hnu.db.jooq.model.tables.records.NuGoalRecord;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import static common.util.Numbers.toDouble;

public record GoalDto(
	@NotNull UUID id,
	@NotNull Boolean removed,
	@Nullable OffsetDateTime createDate,
	@Nullable OffsetDateTime modifyDate,
	@NotNull UUID userId,
	@NotNull String name,
	@NotNull Integer durationMonth,
	@NotNull Double monthlyInvest,
	@NotNull Double target,
	@NotNull LocalDateTime estimatedDate,
	@NotNull Double balance,
	@NotNull Boolean completed
) {
	public static @NotNull GoalDto of(@NotNull NuGoalRecord r) {
		return new GoalDto(
			r.getId(),
			r.getRemoved(),
			r.getCreateDate(),
			r.getModifyDate(),
			r.getUserId(),
			r.getName(),
			r.getDurationMonth(),
			toDouble(r.getMonthlyInvest()),
			toDouble(r.getTarget()),
			r.getEstimatedDate(),
			toDouble(r.getBalance()),
			r.getCompleted()
		);
	}
}
