package com.hnu.api.auth;

import com.hnu.api.auth.model.AuthUserModel;
import com.hnu.api.auth.model.TokenResultModel;
import com.hnu.api.auth.operation.ConfirmOtpRequest;
import com.hnu.api.auth.operation.ProfileUpdateRequest;
import com.hnu.api.auth.operation.SaveSessionDataRequest;
import com.hnu.api.auth.operation.SendOtpRequest;
import com.hnu.api.auth.operation.SendOtpResponse;
import com.hnu.auth.HxIdentity;
import common.dto.SaveResult;
import common.enumeration.Authenticator;
import io.quarkus.security.Authenticated;
import io.vertx.core.http.HttpServerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestPath;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Path("/api/hnu/auth")
public class AuthResource {
	public static final String SESSION_ID = "sessionId";

	HxIdentity identity;
	AuthFacade auth;
	Logger log;

	AuthResource(Logger log,
				 AuthFacade auth,
				 HxIdentity identity) {
		this.log = log;
		this.auth = auth;
		this.identity = identity;
	}

	@POST
	@Path("/otp")
	public SendOtpResponse sendOtpCode(@Valid @RequestBody SendOtpRequest body, @Context HttpHeaders headers) {
		String phone = body.phone();
		Authenticator authenticator = body.authenticator();
		log.debug("send otp request: {} - {}", phone, authenticator);
		return auth.sendOtpCode(phone, authenticator)
			.map(SendOtpResponse::new).orElse(null);
	}

	@POST
	@Path("/otp-code")
	@APIResponse(
		content = @Content(mediaType = MediaType.APPLICATION_JSON,
		schema = @Schema(implementation = TokenResultModel.class)),
		description = "confirm otp code"
	)
	public Response confirmOtpCode(@Valid @RequestBody ConfirmOtpRequest body,
								   @HeaderParam(HttpHeaders.USER_AGENT) String userAgent,
								   @HeaderParam("X-Real-IP") @DefaultValue("-") String clientIp
	) {
		log.debug("confirm otp request: {} - {}, ua: {}, ip: {}", body.phone(), body.code(), userAgent, clientIp);
		String token = auth.confirmOtpCode(body.phone(), body.code(), userAgent, clientIp);
		var cookie = new NewCookie.Builder(SESSION_ID)
			.path("/")
			.value(token)
			.version(Cookie.DEFAULT_VERSION)
			.maxAge((int) Duration.ofDays(90).getSeconds())
			.secure(false)
			.build();
		return Response.ok(TokenResultModel.of(token)).cookie(cookie).build();
	}

	@Authenticated
	@GET
	@Path("/my-user-info")
	public @NotNull AuthUserModel getMyUserInfo() {
		return auth.getUserInfoByToken(identity.sessionId());
	}

	@Authenticated
	@POST
	@Path("/my-user-info")
	public @NotNull SaveResult saveMyUserInfo(@Valid @RequestBody ProfileUpdateRequest body) {
		var saved = auth.saveUserInfo(identity.phone(), body);
		return SaveResult.of(saved.id());
	}

	@Authenticated
	@DELETE
	@Path("/my-session")
	public void logout(@Context HttpServerRequest request) {
		Optional.ofNullable(request.getCookie(SESSION_ID)).ifPresent(cookie -> {
			cookie.setMaxAge(0);
			cookie.setValue("");
			cookie.setPath("/");
		});
		log.warn("user logout: {}", identity.sessionId());
		auth.invalidate(identity.sessionId());
	}

	@PUT
	@Path("/my-session/{id}")
	public Response unlockSessionById(@RestPath UUID id) {
		String token = auth.unlockSessionById(id);
		var cookie = new NewCookie.Builder(SESSION_ID)
			.path("/")
			.value(token)
			.version(Cookie.DEFAULT_VERSION)
			.maxAge((int) Duration.ofDays(90).getSeconds())
			.secure(false)
			.build();
		return Response.ok(TokenResultModel.of(token)).cookie(cookie).build();
	}

	@Authenticated
	@PUT
	@Path("/my-session")
	public void updateMySession(@RequestBody @Valid SaveSessionDataRequest request) {
		auth.update(identity.userSession());
	}

	@Authenticated
	@DELETE
	@Path("/me")
	public void deleteMe(@Context HttpServerRequest request) {
		auth.deleteUserByToken(identity.sessionId());
		logout(request);
	}
}
