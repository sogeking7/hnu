package com.hnu.api.auth.impl;

import com.hnu.api.auth.AuthService;
import com.hnu.api.auth.model.AuthUserModel;
import com.hnu.api.auth.operation.ProfileUpdateRequest;
import com.hnu.auth.UserSession;
import com.hnu.db.otp.OtpDao;
import com.hnu.db.otp.OtpRequestDao;
import com.hnu.db.otp.dto.OtpDto;
import com.hnu.db.session.SessionDao;
import com.hnu.db.session.dto.SessionData;
import com.hnu.db.session.dto.SessionDto;
import com.hnu.db.telegram.TgUserDao;
import com.hnu.db.telegram.dto.TgUserDto;
import com.hnu.db.user.UserDao;
import com.hnu.db.user.UserRepo;
import com.hnu.db.user.dto.UserDto;
import com.hnu.db.util.JooqUtils;
import com.client.telegram.dto.TgContact;
import com.client.telegram.dto.TgUser;
import common.enumeration.Authenticator;
import common.exception.HxError;
import common.util.Errors;
import common.util.Strings;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Transactional
@RequestScoped
public class AuthServiceImpl implements AuthService {

	private static final long OTP_MAX_CONFIRM_LIMIT_PER_MINUTE = 3L;

	@ConfigProperty(name = "tg.botTitle")
	String tgBotTitle;

	@Inject
	SessionDao sessionDao;
	@Inject
	UserDao userDao;
	@Inject
	UserRepo userRepo;
	@Inject
	OtpRequestDao otpRequestDao;
	@Inject
	Logger log;
	@Inject
	TgUserDao tgUserDao;
	@Inject
	OtpDao otpDao;


	@Override
	public Optional<TgUserDto> getActiveTelegramUserByPhone(String phone) {
		return tgUserDao.findActiveByPhone(phone, tgBotTitle);
	}

	@Override
	public Optional<String> saveOtpRequest(String phone, String code) {
		OtpDto otp = otpDao.findLastActiveByPhone(phone).orElseThrow(() -> Errors.otpNotFound(phone));

		if (otp.getSessionId() != null) {
			return Optional.of(otp.getSessionId());
		}

		Duration duration = Duration.ofMinutes(1);
		long countOtpRequests = otpRequestDao.countOtpRequests(otp.getId(), duration);
		if (countOtpRequests >= OTP_MAX_CONFIRM_LIMIT_PER_MINUTE) {
			throw HxError.of("otp.request.limit.exceeds", Map.of("phone", phone));
		}

		otpRequestDao.insertOtpRequest(record -> {
			record.setCode(code);
			record.setOtpId(otp.getId());
		});

		return Optional.empty();
	}

	@Override
	public OtpDto saveOtp(String phone, Authenticator authenticator) {
		String code = String.format("%05d", new Random().nextInt(99999));
		log.info("[12-0900] generated code for {} is: {}", phone, code);
		return otpDao.insert(record -> {
			record.setPhone(phone);
			record.setAuthenticator(authenticator.name());
			record.setCode(code);
			record.setExpireDate(OffsetDateTime.now().plusMinutes(2));
		});
	}

	@Override
	public String confirmOtpCode(String phone, String code, String userAgent, String ip) {
		log.info("[12-0901] confirm code for {} is: {}", phone, code);
		OtpDto otp = otpDao.findLastActiveByPhone(phone).orElseThrow(() -> Errors.otpNotFound(phone));

		if (!otp.getCode().equals(code)) {
			throw Errors.otpNotValid(phone);
		}

		UserDto user = userDao.findByPhone(phone).filter(UserDto::enabled).orElseThrow(() -> Errors.userNotFound(phone));

		otpDao.update(record -> {
			record.setSuccessDate(OffsetDateTime.now());
			record.setUserId(user.id());
		}, otp.getId());

		String sessionId = UUID.randomUUID().toString();
		SessionData data = new SessionData(userAgent, ip, new JsonObject(), false);

		sessionDao.insert(record -> {
			record.setSessionId(sessionId);
			record.setPhone(phone);
			record.setUserId(user.id());
			record.setData(JooqUtils.toJSONB(data));
		});
		otpDao.update(record -> record.setSessionId(sessionId), otp.getId());
		return sessionId;
	}

	@Override
	public AuthUserModel getUserInfoByToken(String token) {
		SessionDto session = sessionDao.findActiveById(token).orElseThrow(() -> Errors.notAuthenticated(token));
		return getUserInfo(session);
	}

	private AuthUserModel getUserInfo(SessionDto session) {
		var phone = session.phone();
		UserDto user = userDao.findByPhone(phone).orElseThrow(() -> Errors.userNotFound(phone));
		return AuthUserModel.of(user);
	}


	@Override
	public UserDto saveUserInfo(String phone, ProfileUpdateRequest body) {
		UserDto user = userDao.findByPhone(phone).orElseThrow();
		return userDao.update(record -> {
			record.setFirstname(Strings.nullIfEmpty(body.firstname()));
			record.setLastname(Strings.nullIfEmpty(body.lastname()));
			record.setPatronymic(Strings.nullIfEmpty(body.patronymic()));
		}, user.id());
	}

	@Override
	public String unlockSessionById(String sessionId) {
		return sessionDao.unlock(sessionId).sessionId();
	}

	@Override
	public void invalidate(String sessionId) {
		sessionDao.invalidate(sessionId);
	}

	@Override
	public void deleteUserByToken(String sessionId) {
		SessionDto session = sessionDao.findActiveById(sessionId).orElseThrow(() -> Errors.notAuthenticated(sessionId));
		UUID userId = session.userId();
		UserDto user = userDao.lockById(userId);

		userDao.update(record -> {
			record.setFirstname(null);
			record.setLastname(null);
			record.setPatronymic(null);
			record.setEnabled(false);
		}, user.id());

		// invalidate other sessions
		for (SessionDto ses : sessionDao.findActiveByUser(userId)) {
			if (!ses.sessionId().equals(sessionId)) {
				invalidate(ses.sessionId());
			}
		}

		log.info("deleted user {} and avatar", user.phone());
	}

	@Override
	public List<Authenticator> getAuthenticatorsByPhone(String phone, String domain) {
		List<Authenticator> authenticators = new ArrayList<>();
		authenticators.add(Authenticator.TELEGRAM);
		return authenticators;
	}

	@Override
	public void saveTelegramUser(Long tgChatId, TgUser user, String sanitizedPhone, TgContact contact) {
		String botTitle = tgBotTitle;

		if (tgUserDao.existsByTgUserId(user.id(), botTitle)) {
			log.info("telegram user already exists, updating: {}", user.id());
			TgUserDto existingUser = tgUserDao.byTgUserId(user.id(), botTitle).orElse(null);
			tgUserDao.findActiveByPhone(sanitizedPhone, botTitle).ifPresent(tgUser -> {
				if (!tgUser.telegramUserId().equals(user.id())) {
					tgUserDao.deleteByTgUserId(tgUser.telegramUserId(), botTitle);
					log.info("deleted telegram user info with id: {}", tgUser.telegramUserId());
				}
			});
			tgUserDao.update(record -> {
				record.setRemoved(false);
				record.setEnabled(true);
				record.setTelegramChatId(tgChatId);
				record.setTelegramLanguageCode(user.languageCode());
				record.setTelegramUsername(user.username());

				if (Strings.isEmpty(existingUser.lastname())) {
					record.setLastname(contact.lastName());
				}
				if (Strings.isEmpty(existingUser.firstname())) {
					record.setFirstname(contact.firstName());
				}
				record.setPhone(sanitizedPhone);
			}, existingUser.id());
			return;
		}
		if (tgUserDao.existsByPhone(sanitizedPhone, botTitle)) {
			log.info("telegram user already exists, updating: {}", sanitizedPhone);
			TgUserDto existingUser = tgUserDao.byPhone(sanitizedPhone, botTitle);
			tgUserDao.update(record -> {
				record.setRemoved(false);
				record.setEnabled(true);
				record.setTelegramChatId(tgChatId);
				record.setTelegramLanguageCode(user.languageCode());
				record.setTelegramUsername(user.username());

				if (Strings.isEmpty(existingUser.lastname())) {
					record.setLastname(contact.lastName());
				}
				if (Strings.isEmpty(existingUser.firstname())) {
					record.setFirstname(contact.firstName());
				}
				record.setPhone(sanitizedPhone);
			}, existingUser.id());
			return;
		}

		tgUserDao.insert(record -> {
			record.setTelegramUserId(user.id());
			record.setTelegramChatId(tgChatId);
			record.setTelegramLanguageCode(user.languageCode());
			record.setTelegramUsername(user.username());
			record.setLastname(contact.lastName());
			record.setFirstname(contact.firstName());
			record.setPhone(sanitizedPhone);
			record.setBot(botTitle);
			record.setEnabled(true);
		});
		log.info("saved new telegram user with phone: {}", sanitizedPhone);
	}

	@Override
	public void update(UserSession session) {
		UUID userId = session.userId();

		SessionData data = sessionDao.findActiveById(session.sessionId()).map(SessionDto::data).orElseThrow();

		sessionDao.update(record -> {
			record.setData(JooqUtils.toJSONB(new SessionData(
				data.userAgent(),
				data.ip(),
				data.deviceInfo(),
				false
			)));
		}, session.sessionId());
	}
}
