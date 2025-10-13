package com.hnu.db.telegram;

import com.hnu.db.jooq.model.tables.TgUsers;
import com.hnu.db.jooq.model.tables.records.TgUserRecord;
import com.hnu.db.telegram.dto.TgUserDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Dependent
public class TgUserDaoImpl extends JooqDb implements TgUserDao {
	private final TgUsers u = TgUsers.TG_USERS;

	public TgUserDaoImpl(DSLContext dsl) {
		super(dsl);
	}

	@Override
	public TgUserDto insert(Consumer<TgUserRecord> fn) {
		var record = new TgUserRecord();
		fn.accept(record);
		return db.insertInto(u).set(record)
			.returning()
			.fetchSingle(TgUserDto::of);
	}

	@Override
	public TgUserDto update(Consumer<TgUserRecord> fn, UUID id) {
		var record = new TgUserRecord();
		fn.accept(record);
		return db.update(u)
			.set(record)
			.where(u.ID.eq(id).and(u.REMOVED.isFalse()))
			.returning()
			.fetchSingle(TgUserDto::of);
	}

	@Override
	public boolean existsByPhone(String phone, String botTitle) {
		return db.fetchExists(u, u.PHONE.eq(phone).and(u.REMOVED.isFalse()).and(u.BOT.eq(botTitle)));
	}

	@Override
	public Optional<TgUserDto> findActiveByPhone(String phone, String botTitle) {
		return db.selectFrom(u).where(u.PHONE.eq(phone)
				.and(u.REMOVED.isFalse())
				.and(u.ENABLED.isTrue())
				.and(u.BOT.eq(botTitle)))
			.fetchOptional(TgUserDto::of);
	}

	@Override
	public boolean existsByTgUserId(Long tgUserId, String botTitle) {
		return db.fetchExists(u, u.TELEGRAM_USER_ID.eq(tgUserId)
			.and(u.REMOVED.isFalse())
			.and(u.BOT.eq(botTitle)));
	}

	@Override
	public Optional<TgUserDto> byTgUserId(Long tgUserId, String botTitle) {
		return db.selectFrom(u)
			.where(u.TELEGRAM_USER_ID.eq(tgUserId).and(u.REMOVED.isFalse()).and(u.BOT.eq(botTitle)))
			.fetchOptional(TgUserDto::of);
	}

	@Override
	public TgUserDto byPhone(String phone, String botTitle) {
		return db.selectFrom(u)
			.where(u.PHONE.eq(phone)
				.and(u.REMOVED.isFalse())
				.and(u.BOT.eq(botTitle)))
			.fetchSingle(TgUserDto::of);
	}

	@Override
	public void deleteByTgUserId(Long telegramUserId, String botTitle) {
		db.deleteFrom(u).where(u.TELEGRAM_USER_ID.eq(telegramUserId).and(u.REMOVED.isFalse()).and(u.BOT.eq(botTitle))).execute();
	}
}
