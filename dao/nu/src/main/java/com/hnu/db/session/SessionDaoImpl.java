package com.hnu.db.session;

import com.hnu.db.jooq.model.tables.NuSessions;
import com.hnu.db.jooq.model.tables.records.NuSessionRecord;
import com.hnu.db.session.dto.SessionDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Dependent
public class SessionDaoImpl extends JooqDb implements SessionDao {
	private final NuSessions s = NuSessions.NU_SESSIONS.as("s");

	public SessionDaoImpl(DSLContext jooq) {
		super(jooq);
	}

	@Override
	public SessionDto insert(Consumer<NuSessionRecord> fn) {
		var record = new NuSessionRecord();
		fn.accept(record);
		return db.insertInto(s).set(record)
			.returning()
			.fetchSingle(SessionDto::of);
	}

	@Override
	public SessionDto update(Consumer<NuSessionRecord> fn, String id) {
		var record = new NuSessionRecord();
		fn.accept(record);
		return db.update(s)
			.set(record)
			.where(s.SESSION_ID.eq(id))
			.returning()
			.fetchSingle(SessionDto::of);
	}

	@Override
	public Optional<SessionDto> findActiveById(String sessionId) {
		return db.selectFrom(s)
			.where(s.SESSION_ID.eq(sessionId)
				.and(s.REMOVED.isFalse())
				.and(s.EXPIRE_DATE.isNull().or(s.EXPIRE_DATE.gt(OffsetDateTime.now()))))
			.fetchOptional(SessionDto::of);
	}

	@Override
    public SessionDto activeById(String sessionId) {
		return db.selectFrom(s).where(s.SESSION_ID.eq(sessionId).and(s.REMOVED.isFalse()).and(s.EXPIRE_DATE.isNull().or(s.EXPIRE_DATE.gt(OffsetDateTime.now())))).fetchSingle(SessionDto::of);
    }

	@Override
	public SessionDto unlock(String sessionId) {
		return db.update(s).set(s.EXPIRE_DATE, (OffsetDateTime) null)
			.where(s.SESSION_ID.eq(sessionId))
			.returning()
			.fetchSingle(SessionDto::of);
	}

	@Override
	public void invalidate(String sessionId) {
		db.update(s).set(s.EXPIRE_DATE, DSL.currentOffsetDateTime()).where(s.SESSION_ID.eq(sessionId)).execute();
	}

	@Override
	public void invalidateByPhone(Collection<String> phones) {
		db.update(s).set(s.EXPIRE_DATE, DSL.currentOffsetDateTime()).where(s.PHONE.in(phones)).execute();
	}

	@Override
	public List<SessionDto> findActiveByUserAndDevice(UUID userId, UUID deviceId) {
		return db.selectFrom(s)
			.where(
				s.REMOVED.isFalse()
					.and(s.EXPIRE_DATE.isNull().or(s.EXPIRE_DATE.gt(DSL.currentOffsetDateTime())))
					.and(s.USER_ID.eq(userId))
					.and(s.DEVICE_ID.eq(deviceId))
			).fetch(SessionDto::of);
	}

	@Override
	public List<SessionDto> findActiveByUser(UUID userId) {
		return db.selectFrom(s)
			.where(
				s.REMOVED.isFalse()
					.and(s.EXPIRE_DATE.isNull().or(s.EXPIRE_DATE.gt(DSL.currentOffsetDateTime())))
					.and(s.USER_ID.eq(userId))
			).fetch(SessionDto::of);
	}

	@Override
	public void saveStoreIdIntoSession(UUID storeId, UUID sessionId) {
		db.update(s)
			.set(s.DATA,
				DSL.field(
					"jsonb_set(coalesce({0}, {1}), '{storeId}', to_jsonb({2}::uuid), true)",
					SQLDataType.JSONB,
					s.DATA,
					DSL.inline("{}").cast(SQLDataType.JSONB),
					DSL.val(storeId.toString())
				)
			)
			.where(s.SESSION_ID.eq(sessionId.toString()))
			.execute();
	}
}
