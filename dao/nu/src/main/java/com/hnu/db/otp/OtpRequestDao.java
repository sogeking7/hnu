package com.hnu.db.otp;

import com.hnu.db.jooq.model.tables.records.NuOtpRequestRecord;
import com.hnu.db.otp.dto.OtpRequestDto;
import org.jooq.DSLContext;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

public interface OtpRequestDao {
	static OtpRequestDao create(DSLContext jooq) {
		return new OtpRequestDaoImpl(jooq);
	}

	OtpRequestDto insertOtpRequest(Consumer<NuOtpRequestRecord> fn);

	Integer countOtpRequests(UUID otpId, Duration duration);
}
