package common.util;

import common.exception.HxError;

public interface Errors {
	static HxError otpNotFound(String phone) {
		return HxError.of("otp.notFound", Maps.of("phone", phone));
	}

	static HxError otpNotValid(String phone) {
		return HxError.of("otp.notValid", Maps.of("phone", phone));
	}

	static HxError telegramChatIdNotFound(String phone) {
		return HxError.of("tg.chatId.notFound", Maps.of("phone", phone));
	}

	static HxError notAuthenticated(String token) {
		return HxError.of(401, "notAuthenticated", Maps.of("token", token));
	}

	static HxError userNotFound(String phone) {
		return HxError.of("user.notFound", Maps.of("phone", phone));
	}
}
