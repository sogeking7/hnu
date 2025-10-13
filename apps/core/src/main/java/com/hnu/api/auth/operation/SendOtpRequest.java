package com.hnu.api.auth.operation;

import common.enumeration.Authenticator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SendOtpRequest(
	@NotNull @Length(min = 11, max = 11) String phone,
	@NotNull Authenticator authenticator
) {
}
