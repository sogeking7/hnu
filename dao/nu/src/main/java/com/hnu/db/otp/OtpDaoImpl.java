package com.hnu.db.otp;

import com.hnu.db.jooq.model.tables.NuOtps;
import com.hnu.db.jooq.model.tables.records.NuOtpRecord;
import com.hnu.db.otp.dto.OtpDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import org.jooq.DSLContext;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Dependent
public class OtpDaoImpl extends JooqDb implements OtpDao {
	private final NuOtps otp = NuOtps.NU_OTPS.as("otp");

	public OtpDaoImpl(DSLContext db) {
		super(db);
	}

	@Override
	public Optional<OtpDto> findLastActiveByPhone(String phone) {
		return db.selectFrom(otp)
			.where(otp.PHONE.eq(phone).and(otp.REMOVED.isFalse()).and(otp.EXPIRE_DATE.gt(OffsetDateTime.now())))
			.orderBy(otp.CREATE_DATE.desc())
			.limit(1)
			.fetchOptional(OtpDto::of);
	}

	@Override
	public OtpDto insert(Consumer<NuOtpRecord> fn) {
		var record = new NuOtpRecord();
		fn.accept(record);
		return db.insertInto(otp).set(record)
			.returning()
			.fetchSingle(OtpDto::of);
	}

	@Override
	public OtpDto update(Consumer<NuOtpRecord> fn, UUID id) {
		var record = new NuOtpRecord();
		fn.accept(record);
		return db.update(otp)
			.set(record)
			.where(otp.ID.eq(id))
			.returning()
			.fetchSingle(OtpDto::of);
	}
}
