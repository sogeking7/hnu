package com.hnu.db.session;

import com.hnu.db.jooq.model.tables.records.NuSessionRecord;
import com.hnu.db.session.dto.SessionDto;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface SessionDao {
	static SessionDao create(DSLContext jooq) {
		return new SessionDaoImpl(jooq);
	}

	@NotNull SessionDto insert(Consumer<NuSessionRecord> fn);

	@NotNull SessionDto update(Consumer<NuSessionRecord> fn, @NotNull String id);

	Optional<SessionDto> findActiveById(@NotNull String sessionId);

	@NotNull SessionDto activeById(@NotNull String sessionId);

	@NotNull
	SessionDto unlock(@NotNull String sessionId);

	void invalidate(@NotNull String sessionId);

	void invalidateByPhone(Collection<String> phones);

	List<SessionDto> findActiveByUserAndDevice(@NotNull UUID userId, @NotNull UUID deviceId);

	List<SessionDto> findActiveByUser(@NotNull UUID userId);

	void saveStoreIdIntoSession(@NotNull UUID storeId, @NotNull UUID sessionId);
}
