package com.hnu.db.otp.dto;

import com.hnu.db.jooq.model.tables.records.NuOtpRecord;
import common.enumeration.Authenticator;
import common.util.Enums;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OtpDto {

	private UUID id;
	private OffsetDateTime createDate;
	private OffsetDateTime modifyDate;
	private Boolean removed;
	private OffsetDateTime expireDate;
	private OffsetDateTime successDate;
	private UUID userId;
	private String code;
	private String phone;
	private String sessionId;
	private Authenticator authenticator;

	public static OtpDto of(NuOtpRecord record) {
		var dto = new OtpDto();
		dto.id = record.getId();
		dto.createDate = record.getCreateDate();
		dto.modifyDate = record.getModifyDate();
		dto.removed = record.getRemoved();
		dto.expireDate = record.getExpireDate();
		dto.successDate = record.getSuccessDate();
		dto.userId = record.getUserId();
		dto.code = record.getCode();
		dto.phone = record.getPhone();
		dto.sessionId = record.getSessionId();
		dto.authenticator = Enums.of(record.getAuthenticator(), Authenticator.class);
		return dto;
	}

	public UUID getId() {
		return id;
	}

	public OtpDto setId(UUID id) {
		this.id = id;
		return this;
	}

	public OffsetDateTime getCreateDate() {
		return createDate;
	}

	public OtpDto setCreateDate(OffsetDateTime createDate) {
		this.createDate = createDate;
		return this;
	}

	public OffsetDateTime getModifyDate() {
		return modifyDate;
	}

	public OtpDto setModifyDate(OffsetDateTime modifyDate) {
		this.modifyDate = modifyDate;
		return this;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public OtpDto setRemoved(Boolean removed) {
		this.removed = removed;
		return this;
	}

	public OffsetDateTime getExpireDate() {
		return expireDate;
	}

	public OtpDto setExpireDate(OffsetDateTime expireDate) {
		this.expireDate = expireDate;
		return this;
	}

	public OffsetDateTime getSuccessDate() {
		return successDate;
	}

	public OtpDto setSuccessDate(OffsetDateTime successDate) {
		this.successDate = successDate;
		return this;
	}

	public UUID getUserId() {
		return userId;
	}

	public OtpDto setUserId(UUID userId) {
		this.userId = userId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public OtpDto setCode(String code) {
		this.code = code;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public OtpDto setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getSessionId() {
		return sessionId;
	}

	public OtpDto setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public OtpDto setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
		return this;
	}
}
