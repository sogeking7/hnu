package com.hnu.util;

import common.exception.HxError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HxErrorMapper extends AbstractErrorMapper implements ExceptionMapper<HxError> {
	@Override
	public Response toResponse(HxError e) {
		log.error("hx.error", e);
		return buildResponse(e);
	}
}
