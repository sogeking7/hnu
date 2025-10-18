package com.hnu.db.transactions.dto;

import com.hnu.db.jooq.model.tables.records.NuTransactionRecord;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import static common.util.Numbers.toDouble;


public record TransactionDto(
	@NotNull UUID id,
	@NotNull Boolean removed,
	@Nullable OffsetDateTime createDate,
	@Nullable OffsetDateTime modifyDate,
	@NotNull UUID userId,
	@NotNull Double amount,
	@NotNull String categoryType,
	@NotNull LocalDateTime date
) {
	public static @NotNull TransactionDto of(@NotNull NuTransactionRecord record) {
		return new TransactionDto(
			record.getId(),
			record.getRemoved(),
			record.getCreateDate(),
			record.getModifyDate(),
			record.getUserId(),
			toDouble(record.getAmount()),
			record.getCategoryType(),
			record.getDate()
		);
	}
}
