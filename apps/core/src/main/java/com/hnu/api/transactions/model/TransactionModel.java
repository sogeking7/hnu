package com.hnu.api.transactions.model;

import com.hnu.db.transactions.dto.TransactionDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionModel(
	@NotNull UUID id,
	@NotNull OffsetDateTime createDate,
	@NotNull UUID userId,
	@NotNull Double amount,
	@NotNull String categoryType,
	@NotNull LocalDateTime date
) {

	public static @NotNull TransactionModel of(@NotNull TransactionDto dto) {
		return new TransactionModel(
			dto.id(),
			dto.createDate(),
			dto.userId(),
			dto.amount(),
			dto.categoryType(),
			dto.date()
		);
	}
}
