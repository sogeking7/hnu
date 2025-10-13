package com.hnu.api.auth;

import com.hnu.api.auth.model.AuthUserModel;
import com.hnu.api.auth.operation.ProfileUpdateRequest;
import com.hnu.auth.UserSession;
import com.hnu.db.otp.dto.OtpDto;
import com.hnu.db.user.dto.UserDto;
import com.client.telegram.TelegramRestClient;
import com.client.telegram.dto.TgReplyMarkup;
import com.client.telegram.dto.TgSendMessageRequest;
import common.enumeration.Authenticator;
import common.util.Errors;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class AuthFacade {
	@Inject
	@RestClient
	TelegramRestClient telegramRestClient;

	@Inject
	AuthService authService;

	@Inject
	Logger log;

	@ConfigProperty(name = "auth.available_authenticators")
	List<Authenticator> availableAuthenticators;

	@ConfigProperty(name = "tg.botTitle")
	String tgBotTitle;

	@ConfigProperty(name = "tg.token")
	String tgToken;

	public Optional<String> sendOtpCode(String phone, Authenticator authenticator) {
		OtpDto otp = authService.saveOtp(phone, authenticator);

		var messageText = otp.getCode();
		var tgUser = authService.getActiveTelegramUserByPhone(phone).orElseThrow(() -> Errors.telegramChatIdNotFound(phone));

		log.info("sent tg message with otp to {}", otp.getPhone());

		telegramRestClient.sendMessage(
			tgToken,
			new TgSendMessageRequest(
				tgUser.telegramChatId(),
				messageText,
				TgReplyMarkup.removeMarkup()));

		return Optional.empty();
	}

	public String confirmOtpCode(String phone, String code, String userAgent, String clientIp) {
		return authService.saveOtpRequest(phone, code)
			.orElseGet(() -> authService.confirmOtpCode(phone, code, userAgent, clientIp));
	}

	public AuthUserModel getUserInfoByToken(String sessionId) {
		return authService.getUserInfoByToken(sessionId);
	}

	public UserDto saveUserInfo(String phone, ProfileUpdateRequest body) {
		return authService.saveUserInfo(phone, body);
	}

	public void invalidate(String sessionId) {
		authService.invalidate(sessionId);
	}

	public String unlockSessionById(UUID sessionId) {
		return authService.unlockSessionById(sessionId.toString());
	}

	public void deleteUserByToken(String sessionId) {
		authService.deleteUserByToken(sessionId);
	}

	public List<Authenticator> getAuthenticators(String phone, String domain) {
		return authService.getAuthenticatorsByPhone(phone, domain);
	}

	public void update(UserSession session) {
		authService.update(session);
	}
}
