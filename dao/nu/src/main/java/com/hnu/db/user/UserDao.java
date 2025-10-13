package com.hnu.db.user;

import com.hnu.db.jooq.model.tables.records.NuUserRecord;
import com.hnu.db.user.dto.UserDto;
import common.util.PagedList;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserDao {
	static UserDao create(DSLContext dsl) {
		return new UserDaoImpl(dsl);
	}

	@NotNull UserDto insertUser(Consumer<NuUserRecord> fn);

	Optional<UserDto> findByPhone(String phone);

	boolean existsByPhone(String phone);

	@NotNull UserDto update(Consumer<NuUserRecord> fn, @NotNull UUID id);

	@NotNull UserDto findById(@NotNull UUID id);

	PagedList<UserDto> find(FindParams params);

	UserDto lockById(UUID userId);

	Map<String, UserDto> findByPhonesAsMap(List<String> phones);

	class FindParams {
		public Integer page;
		public Integer limit;
		public String query;
		public UUID roleId;
		public String permissionKey;
		public Boolean includeDisabled;
		public List<String> ids;
		public List<String> sort;
		public LocalDate createDateFrom;
		public LocalDate createDateTo;
	}
}
