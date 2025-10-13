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
	public PagedList<UserDto> find(FindParams params) {
		if (params.page == null || params.page < 1) {
			params.page = 1;
		}

		if (params.limit == null || params.limit <= 0) {
			params.limit = 20;
		}

		SelectQuery<NuUserRecord> selectQuery = db.selectQuery(u);
		selectQuery.addConditions(u.REMOVED.isFalse());

		if (Lists.isNotEmpty(params.ids)) {
			selectQuery.addConditions(u.ID.in(params.ids));
			selectQuery.addOrderBy(u.ID);
			List<UserDto> list = selectQuery.fetch(UserDto::of);
			return PagedList.of(list, list.size());
		}

		boolean includeDisabled = Booleans.val(params.includeDisabled);
		if (!includeDisabled) {
			selectQuery.addConditions(u.ENABLED.isTrue());
		}

		Optional.ofNullable(params.createDateFrom)
			.map(u.CREATE_DATE.cast(LocalDate.class)::ge)
			.ifPresent(selectQuery::addConditions);

		Optional.ofNullable(params.createDateTo)
			.map(u.CREATE_DATE.cast(LocalDate.class)::le)
			.ifPresent(selectQuery::addConditions);

		if (params.query != null) {
			var searchText = params.query;
			if (Strings.isNumeric(searchText)) {
				searchText = "%" + searchText + "%";
				selectQuery.addConditions(u.PHONE.likeIgnoreCase(searchText));
			} else {
				searchText = "%" + searchText + "%";
				selectQuery.addConditions(u.LASTNAME.likeIgnoreCase(searchText).or(u.FIRSTNAME.likeIgnoreCase(searchText)));
			}
		}

		int count = db.fetchCount(selectQuery);
		selectQuery.addOffset((params.page - 1) * params.limit);
		selectQuery.addLimit(params.limit);

		if (Lists.isEmpty(params.sort)) {
			selectQuery.addOrderBy(u.CREATE_DATE);
		} else {
			Map<String, Field> orderFieldMap = Map.of(
				"create_date", u.CREATE_DATE
			);
			List<OrderField> sortFields = params.sort.stream()
				.map(sortStr -> {
					String[] arr = sortStr.split(":");
					return Pair.of(arr[0], SortType.valueOf(arr[1]));
				})
				.filter(pair -> orderFieldMap.containsKey(pair.getLeft()))
				.map(sortPair -> {
					String column = sortPair.getLeft();
					SortType sortType = sortPair.getRight();
					return orderFieldMap.get(column).sort(SortOrder.valueOf(sortType.name().toUpperCase()));
				})
				.collect(Collectors.toList());
			if (!sortFields.isEmpty()) {
				selectQuery.addOrderBy(sortFields.toArray(OrderField[]::new));
			}
		}

		List<UserDto> list = selectQuery.fetch(UserDto::of);
		return PagedList.of(list, count);
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
