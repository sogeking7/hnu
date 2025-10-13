package com.hnu.api.telegram;

import com.hnu.api.auth.AuthFacade;
import com.hnu.api.auth.AuthService;
import com.hnu.db.telegram.TgUserDao;
import com.hnu.db.telegram.dto.TgUserDto;
import com.client.telegram.TelegramRestClient;
import com.client.telegram.dto.TgContact;
import com.client.telegram.dto.TgReplyMarkup;
import com.client.telegram.dto.TgSendMessageRequest;
import com.client.telegram.dto.TgUpdate;
import common.enumeration.Authenticator;
import common.exception.HxError;
import common.util.Strings;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.UUID;

@RequestScoped
public class TelegramServiceImpl implements TelegramService {

	@Inject
	AuthFacade auth;

	@Inject
	AuthService authService;

	@Inject
	TgUserDao tgUserDao;

	@Inject
	@RestClient
	TelegramRestClient telegramClient;

	@Inject
	Logger log;

	@ConfigProperty(name = "tg.botTitle")
	String botTitle;

	@Override
	public void handleUpdate(String tgToken, TgUpdate update) {
		var botTitle = this.botTitle;
		TgUserDto tgUser = null;

		if (update.message() != null) {
			tgUser = tgUserDao.byTgUserId(update.message().from().id(), botTitle).orElse(null);
		} else if (update.callbackQuery() != null) {
			tgUser = tgUserDao.byTgUserId(update.callbackQuery().from().id(), botTitle).orElse(null);
		} else if (update.myChatMember() != null) {
			tgUser = tgUserDao.byTgUserId(update.myChatMember().from().id(), botTitle).orElse(null);
		}

		if (tgUser != null) {
			if (update.myChatMember() == null) {
				enableTelegramClient(tgUser.telegramUserId(), tgUser.phone());
			}

			if (update.callbackQuery() != null) {
				try {
					var callback = update.callbackQuery();
					String data = callback.data();
					if (data != null && data.startsWith("store:")) {
						String idStr = data.substring("store:".length());
						try {
							return;
						} catch (IllegalArgumentException e) {

						}
					}
				} catch (Exception ex) {
					log.error("Failed to handle callbackQuery", ex);
				}
			}

			var message = update.message();
			if (message == null) {
				log.warn("got empty message...");
				return;
			}
			if (message.from() == null) {
				log.info("message.from undefined, skip");
				return;
			}
			if (message.from().isBot()) {
				log.info("got message from bot, skip");
				return;
			} else if (!message.chat().isPrivate()) {
				log.info("got non private message, skip");
				return;
			}

			Long tgChatId = message.chat().id();

			log.info("is start command? {} - {}", message.isStartCommand(), message.text());

			var newMessage = new TgSendMessageRequest(
				tgChatId,
				TgTranslations.tgManual,
				TgReplyMarkup.requestContact(TgTranslations.tgSendPhoneText)
			);
			log.info("Sending message to Telegram: chatId={}, text={}", tgChatId, TgTranslations.tgManual);
			try {
				telegramClient.sendMessage(tgToken, newMessage);
			} catch (Exception e) {
				log.error("Failed to send Telegram message: {}", e.getMessage(), e);
				throw e;
			}
		} else {
			var message = update.message();

			if (message.contact() != null) {
				TgContact contact = message.contact();
				Long tgUserId = message.from().id();
				Long tgChatId = message.chat().id();

				if (contact.userId() != null && contact.userId().equals(tgUserId)) {
					String sanitizedPhone = Strings.getDigits(contact.phoneNumber());
					if (sanitizedPhone.startsWith("8")) {
						sanitizedPhone = "7" + sanitizedPhone.substring(1);
					}
					String phone = sanitizedPhone;

					try {
						authService.saveTelegramUser(tgChatId, message.from(), phone, contact);
						auth.sendOtpCode(phone, Authenticator.TELEGRAM);
						return;
					} catch (HxError err) {
						log.error("failed to save user", err);
					}
				}
			}
			Long tgChatId = update.message().chat().id();

			var newMessage = new TgSendMessageRequest(
				tgChatId,
				TgTranslations.tgManual,
				TgReplyMarkup.requestContact(TgTranslations.tgSendPhoneText)
			);
			telegramClient.sendMessage(tgToken, newMessage);
		}
	}

	private void enableTelegramClient(Long tgUserId, String phone) {
		auth.sendOtpCode(phone, Authenticator.TELEGRAM);
	}
}

interface TgTranslations {
	String tgManual = "Please share your phone number to enter the {botTitle} application";
	String tgSendPhoneText = "Share phone number";
	String tgOtp = "{otpCode} - code to access the {botTitle} application";
}
