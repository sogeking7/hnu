package com.hnu.api.auth.operation;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ConfirmOtpRequest(
	@NotNull @Length(min = 11, max = 11) String phone,
	@NotNull String code
) {
}
