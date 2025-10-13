package com.hnu.auth;


import java.security.Principal;
import java.util.UUID;

public class AppPrincipal implements Principal {
	private final UUID userId;

	public AppPrincipal(UserSession session) {
		this.userId = session.userId();
	}

	@Override
	public String getName() {
		return userId.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AppPrincipal that = (AppPrincipal) o;

		return userId.equals(that.userId);
	}

	@Override
	public int hashCode() {
		return userId.hashCode();
	}
}
