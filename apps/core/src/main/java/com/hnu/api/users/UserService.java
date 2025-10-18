package com.hnu.api.users;

import com.hnu.db.user.UserDao;
import com.hnu.db.user.dto.UserDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {
	public List<UserDto> getAllUsers();

	public UserDto getUserById(@NotNull UUID id);
}
