package com.hnu.db.user;

import com.hnu.db.user.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.Optional;

public interface UserRepo {
	static UserRepo create(DSLContext jooq) {
		return new UserRepoImpl(jooq);
	}

	void changePhoneNumber(String newPhone, String oldPhone);

	Optional<UserDto> findByPhone(@NotNull String phone);

}
