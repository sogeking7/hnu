package com.hnu.api.telegram;

import com.client.telegram.dto.TgUpdate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/service/api/fcs/telegram")
public class TelegramResource {

	@Inject
	TelegramService telegramService;

	@Inject
	Logger log;

	@ConfigProperty(name = "tg.token")
	String tgToken;

	@RunOnVirtualThread
	@POST
	@Path("/")
	public Response onTelegramMessage(JsonObject update) {
		log.info("tg token {}", tgToken);
		log.info("tg update {}", update);

		try {
			telegramService.handleUpdate(tgToken, update.mapTo(TgUpdate.class));
			return Response.ok().build(); // Always return 200 to Telegram
		} catch (Exception e) {
			log.error("Error handling Telegram update", e);
			return Response.ok().build(); // Still respond 200 so Telegram doesn’t retry
		}
	}

}
