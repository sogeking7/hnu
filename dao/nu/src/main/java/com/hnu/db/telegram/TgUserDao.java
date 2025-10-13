package com.hnu.db.telegram;

import com.hnu.db.jooq.model.tables.records.TgUserRecord;
import com.hnu.db.telegram.dto.TgUserDto;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface TgUserDao {
	static TgUserDao create(DSLContext dsl) {
		return new TgUserDaoImpl(dsl);
	}

	TgUserDto insert(Consumer<TgUserRecord> fn);

	TgUserDto update(Consumer<TgUserRecord> fn, UUID id);

	boolean existsByPhone(@NotNull String phone, @NotNull String botTitle);

	Optional<TgUserDto> findActiveByPhone(@NotNull String phone, @NotNull String botTitle);

	boolean existsByTgUserId(@NotNull Long tgUserId, @NotNull String botTitle);

	Optional<TgUserDto> byTgUserId(@NotNull Long tgUserId, @NotNull String botTitle);

	TgUserDto byPhone(@NotNull String phone, @NotNull String botTitle);

	void deleteByTgUserId(@NotNull Long telegramUserId, @NotNull String botTitle);
}
