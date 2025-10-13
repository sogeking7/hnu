package com.hnu.app.auth;

import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.BaseAuthenticationRequest;
import io.smallrye.common.constraint.NotNull;

public class AppAuthRequest extends BaseAuthenticationRequest implements AuthenticationRequest {

	private final String sessionId;

	public AppAuthRequest(@NotNull String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public static AppAuthRequest of(@NotNull String sessionId) {
		return new AppAuthRequest(sessionId);
	}
}
