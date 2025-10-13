package com.hnu.util;

import common.exception.HxError;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.Logger;

@Provider
public class DefaultErrorMapper extends AbstractErrorMapper implements ExceptionMapper<Exception> {

	@Inject
	Logger log;

	@Override
	public Response toResponse(Exception ex) {
		var e = HxError.unexpected();
		log.error("exception: " + e.id, ex);
		return buildResponse(e);
	}
}
