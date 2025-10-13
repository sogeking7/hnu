	package com.client.telegram;

	import com.client.telegram.dto.TgGetFileResponse;
	import com.client.telegram.dto.TgSendMessageRequest;
	import com.client.telegram.dto.TgSendMessageResponse;
	import com.client.telegram.dto.TgSendPhotoRequest;
	import jakarta.validation.constraints.NotNull;
	import jakarta.ws.rs.GET;
	import jakarta.ws.rs.POST;
	import jakarta.ws.rs.Path;
	import jakarta.ws.rs.PathParam;
	import jakarta.ws.rs.QueryParam;
	import jakarta.ws.rs.core.Response;
	import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

	@RegisterRestClient(configKey = "telegram")
	public interface TelegramRestClient {

		@POST
		@Path("/bot{token}/sendMessage")
		TgSendMessageResponse sendMessage(@PathParam("token") String token, TgSendMessageRequest message);

		@POST
		@Path("/bot{token}/sendPhoto")
		TgSendMessageResponse sendPhoto(@PathParam("token") String token, TgSendPhotoRequest message);

		@GET
		@Path("/bot{token}/getFile")
		TgGetFileResponse getFile(@PathParam("token") String token, @QueryParam("file_id") @NotNull String fileId);

		@GET
		@Path("/file/bot{token}/{filePath}")
		Response downloadFile(@PathParam("token") String token, @PathParam("filePath") String filePath);

	}
