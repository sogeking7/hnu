package com.hnu.db.otp;

import com.hnu.db.jooq.model.tables.NuOtpRequests;
import com.hnu.db.jooq.model.tables.records.NuOtpRequestRecord;
import com.hnu.db.otp.dto.OtpRequestDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import org.jooq.DSLContext;
import org.jooq.Record1;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Dependent
public class OtpRequestDaoImpl extends JooqDb implements OtpRequestDao {

	private final NuOtpRequests req = NuOtpRequests.NU_OTP_REQUESTS.as("otp_req");

	public OtpRequestDaoImpl(DSLContext jooq) {
		super(jooq);
	}

	@Override
	public OtpRequestDto insertOtpRequest(Consumer<NuOtpRequestRecord> fn) {
		var record = new NuOtpRequestRecord();
		fn.accept(record);
		return db.insertInto(req).set(record)
			.returning()
			.fetchSingle(OtpRequestDto::of);
	}

	@Override
	public Integer countOtpRequests(UUID otpId, Duration duration) {
		return db.selectCount()
			.from(req)
			.where(req.REMOVED.isFalse()
				.and(req.OTP_ID.eq(otpId))
				.and(req.CREATE_DATE.gt(OffsetDateTime.now().minusSeconds(duration.getSeconds())))
			).fetchOne(Record1::value1);
	}
}
