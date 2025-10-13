package com.hnu.api.auth;

import com.hnu.api.auth.model.AuthUserModel;
import com.hnu.api.auth.operation.ProfileUpdateRequest;
import com.hnu.auth.UserSession;
import com.hnu.db.otp.dto.OtpDto;
import com.hnu.db.telegram.dto.TgUserDto;
import com.hnu.db.user.dto.UserDto;
import com.client.telegram.dto.TgContact;
import com.client.telegram.dto.TgUser;
import common.enumeration.Authenticator;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface AuthService {

	OtpDto saveOtp(@NotNull String phone, @NotNull Authenticator authenticator);

	Optional<TgUserDto> getActiveTelegramUserByPhone(@NotNull String phone);

	Optional<String> saveOtpRequest(@NotNull String phone, @NotNull String code);

	String confirmOtpCode(String phone, String code, String userAgent, String ip);

	@NotNull
	AuthUserModel getUserInfoByToken(@NotNull String token);

	@NotNull
	UserDto saveUserInfo(@NotNull String phone, @NotNull ProfileUpdateRequest body);

	@NotNull
	String unlockSessionById(@NotNull String sessionId);

	void invalidate(@NotNull String sessionId);

	void deleteUserByToken(@NotNull String sessionId);

	List<Authenticator> getAuthenticatorsByPhone(@NotNull String phone, String domain);

	void saveTelegramUser(@NotNull Long tgChatId, @NotNull TgUser tgUser, @NotNull String phone, @NotNull TgContact contact);

	void update(@NotNull UserSession session);
}
