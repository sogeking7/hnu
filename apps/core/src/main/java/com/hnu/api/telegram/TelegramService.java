package com.hnu.api.telegram;

import com.client.telegram.dto.TgUpdate;

public interface TelegramService {

	void handleUpdate(String tgToken, TgUpdate update);
}
