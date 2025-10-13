package com.hnu.db.user;

import com.hnu.db.jooq.model.tables.NuUsers;
import com.hnu.db.user.dto.UserDto;
import com.hnu.db.util.JooqDb;
import jakarta.enterprise.context.Dependent;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import java.util.Optional;


@Dependent
public class UserRepoImpl extends JooqDb implements UserRepo {

	public UserRepoImpl(DSLContext jooq) {
		super(jooq);
	}

	@Override
	public void changePhoneNumber(String newPhone, String oldPhone) {
		var u = NuUsers.NU_USERS.as("u");

		db.update(u)
			.set(u.REMOVED, true)
			.where(u.PHONE.eq(newPhone).and(u.REMOVED.isFalse()))
			.execute();

		db.update(u)
			.set(u.PHONE, newPhone)
			.where(u.PHONE.eq(oldPhone).and(u.REMOVED.isFalse()))
			.execute();
	}

	@Override
	public Optional<UserDto> findByPhone(@NotNull String phone) {
		var u = NuUsers.NU_USERS.as("u");
		return db.selectFrom(u).where(u.PHONE.eq(phone).and(u.REMOVED.isFalse()))
			.fetchOptional(UserDto::of);
	}
}
