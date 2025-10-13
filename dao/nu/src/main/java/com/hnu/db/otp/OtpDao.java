package com.hnu.db.otp;

import com.hnu.db.jooq.model.tables.records.NuOtpRecord;
import com.hnu.db.otp.dto.OtpDto;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface OtpDao {
	static OtpDao create(DSLContext jooq) {
		return new OtpDaoImpl(jooq);
	}

	@NotNull
	OtpDto insert(Consumer<NuOtpRecord> fn);

	@NotNull
	OtpDto update(Consumer<NuOtpRecord> fn, @NotNull UUID id);

	Optional<OtpDto> findLastActiveByPhone(@NotNull String phone);
}
