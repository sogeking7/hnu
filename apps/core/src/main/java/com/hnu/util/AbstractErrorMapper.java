package com.hnu.util;

import common.exception.HxError;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;

public class AbstractErrorMapper {
	@Inject
	Logger log;

	@Inject
	HttpServerRequest request;

	public Response buildResponse(HxError e) {
		e.description = e.getMessageCode();
		log.error("error: {}", e.toJson().encode());
		return Response.status(e.failureCode())
			.entity(e.toJson().encode())
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
}
