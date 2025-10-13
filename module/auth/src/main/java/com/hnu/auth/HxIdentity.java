package com.hnu.auth;

import common.exception.HxError;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@RequestScoped
public class HxIdentity {

	public interface Attr {
		String USER_SESSION = "user_session";
	}

	@Inject
	SecurityIdentity securityIdentity;

	public UserSession userSession() {
		return securityIdentity.getAttribute(Attr.USER_SESSION);
	}

	public @NotNull UUID userId() {
		if (securityIdentity.isAnonymous()) {
			throw HxError.of("unauthenticated");
		}
		return userSession().userId();
	}

	public @NotNull String phone() {
		if (securityIdentity.isAnonymous()) {
			throw HxError.of("unauthenticated");
		}
		return userSession().phone();
	}

	public String sessionId() {
		if (securityIdentity.isAnonymous()) {
			throw HxError.of("unauthenticated");
		}
		return userSession().sessionId();
	}

	public boolean isSuperAdmin() {
		return userSession().isSuperAdmin();
	}
}
