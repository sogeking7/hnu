package com.hnu.app.auth;

import com.hnu.auth.AppPrincipal;
import com.hnu.auth.HxIdentity;
import com.hnu.auth.UserSession;
import com.hnu.db.session.SessionDao;
import com.hnu.db.user.UserRepo;
import com.hnu.db.user.dto.UserDto;
import common.util.Booleans;
import common.util.Errors;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

import static com.hnu.api.auth.AuthResource.SESSION_ID;

@ApplicationScoped
public class AppIdentityProvider implements IdentityProvider<AppAuthRequest> {

	@Inject
	Logger log;

	@Inject
	UserRepo userRepo;

	@Inject
	SessionDao sessionDao;

	@Override
	public Class<AppAuthRequest> getRequestType() {
		return AppAuthRequest.class;
	}

	@Override
	public Uni<SecurityIdentity> authenticate(AppAuthRequest request, AuthenticationRequestContext context) {
		log.trace("StoreIdentityProvider authenticate");
		return context.runBlocking(() -> {
			QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
			builder.addAttribute(SESSION_ID, request.getSessionId());

			getActiveUserSessionByToken(request.getSessionId()).ifPresentOrElse(session -> {
				builder.addRoles(Set.of("USER"));
				builder.setPrincipal(new AppPrincipal(session));
				builder.addAttribute(HxIdentity.Attr.USER_SESSION, session);
			}, () -> {
				log.warn("getActiveSessionByToken not present for {}", request.getSessionId());
				builder.setPrincipal(new QuarkusPrincipal(""));
				builder.setAnonymous(true);
			});
			return builder.build();
		});
	}

	private Optional<UserSession> getActiveUserSessionByToken(String token) {
		return sessionDao.findActiveById(token).map(session -> {
			UserDto user = userRepo.findByPhone(session.phone()).orElseThrow(() -> Errors.userNotFound(session.phone()));

			if (session.modifyDate().toLocalDate().isBefore(LocalDate.now())) {
				sessionDao.update(record -> record.setModifyDate(OffsetDateTime.now()), session.sessionId());
				log.info("updated session modify date {}", session.sessionId());
			}

			return new UserSession(
				session.createDate(),
				session.modifyDate(),
				session.expireDate(),
				session.sessionId(),
				user.id(),
				user.phone(),
				user.firstname(),
				Booleans.val(session.data().superAdmin())
			);
		});
	}
}
