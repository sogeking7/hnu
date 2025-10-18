package com.hnu.api.users;

import com.hnu.api.users.model.UserModel;
import com.hnu.db.user.dto.UserDto;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;
import java.util.UUID;

@Path("/api/hnu/users")
public class UserResource {

	@Inject
	UserService userService;

	@GET
	@Path("/")
	public List<UserModel> getUsers() {
		return userService.getAllUsers().stream().map(UserModel::of).toList();
	}

	@GET
	@Path("/{id}")
	public UserModel getUserById(@NotNull UUID id) {
		UserDto user = userService.getUserById(id);
		return UserModel.of(user);
	}

}
