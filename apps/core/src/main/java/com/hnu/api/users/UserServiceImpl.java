package com.hnu.api.users;

import com.hnu.db.user.UserDao;
import com.hnu.db.user.dto.UserDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@RequestScoped
public class UserServiceImpl implements UserService {
	@Inject
	UserDao userDao;

	@Override
	public List<UserDto> getAllUsers() {
		return userDao.find();
	}

	@Override
	public UserDto getUserById(UUID id) {
		return userDao.findById(id);
	}
}
