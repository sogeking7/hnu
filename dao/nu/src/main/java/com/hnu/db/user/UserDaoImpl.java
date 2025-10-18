package com.hnu.db.user;

import com.hnu.db.jooq.model.tables.NuUsers;
import com.hnu.db.jooq.model.tables.records.NuUserRecord;
import com.hnu.db.user.dto.UserDto;
import com.hnu.db.util.JooqDb;
import common.enumeration.SortType;
import common.util.Booleans;
import common.util.Lists;
import common.util.PagedList;
import common.util.Strings;
import jakarta.enterprise.context.Dependent;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.SelectQuery;
import org.jooq.SortOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Dependent
public class UserDaoImpl extends JooqDb implements UserDao {

	private final NuUsers u = NuUsers.NU_USERS.as("u");

	public UserDaoImpl(DSLContext dsl) {
		super(dsl);
	}

	@Override
	public @NotNull UserDto insertUser(Consumer<NuUserRecord> fn) {
		var record = new NuUserRecord();
		fn.accept(record);
		return db.insertInto(u).set(record)
			.returning()
			.fetchSingle(UserDto::of);
	}

	@Override
	public Optional<UserDto> findByPhone(String phone) {
		return db.selectFrom(u)
			.where(u.PHONE.eq(phone).and(u.REMOVED.isFalse()))
			.fetchOptional(UserDto::of);
	}

	@Override
	public boolean existsByPhone(String phone) {
		return db.fetchExists(u, u.PHONE.eq(phone).and(u.REMOVED.isFalse()));
	}

	@Override
	public @NotNull UserDto update(Consumer<NuUserRecord> fn, @NotNull UUID id) {
		var record = new NuUserRecord();
		fn.accept(record);
		return db.update(u)
			.set(record)
			.where(u.ID.eq(id))
			.returning()
			.fetchSingle(UserDto::of);
	}

	@Override
	public @NotNull UserDto findById(@NotNull UUID id) {
		return db.selectFrom(u)
			.where(u.ID.eq(id).and(u.REMOVED.isFalse()))
			.fetchSingle(UserDto::of);
	}

	@Override
	public List<UserDto> find() {
		return db.selectFrom(u)
			.where(u.REMOVED.isFalse())
			.fetch(UserDto::of);
	}

	@Override
	public UserDto lockById(UUID userId) {
		return db.selectFrom(u)
			.where(u.ID.eq(userId).and(u.REMOVED.isFalse()))
			.forUpdate()
			.fetchSingle(UserDto::of);
	}

	@Override
	public Map<String, UserDto> findByPhonesAsMap(List<String> phones) {
		return db.selectFrom(u)
			.where(u.PHONE.in(phones).and(u.REMOVED.isFalse()))
			.forUpdate()
			.fetchMap(u.PHONE, UserDto::of);
	}
}
