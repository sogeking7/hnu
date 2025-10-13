package com.hnu.db.otp.dto;

import com.hnu.db.jooq.model.tables.records.NuOtpRequestRecord;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OtpRequestDto {
	private UUID id;
	private OffsetDateTime createDate;
	private OffsetDateTime modifyDate;
	private Boolean removed;
	private String code;
	private UUID otpId;

	public UUID getId() {
		return id;
	}

	public OtpRequestDto setId(UUID id) {
		this.id = id;
		return this;
	}

	public OffsetDateTime getCreateDate() {
		return createDate;
	}

	public OtpRequestDto setCreateDate(OffsetDateTime createDate) {
		this.createDate = createDate;
		return this;
	}

	public OffsetDateTime getModifyDate() {
		return modifyDate;
	}

	public OtpRequestDto setModifyDate(OffsetDateTime modifyDate) {
		this.modifyDate = modifyDate;
		return this;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public OtpRequestDto setRemoved(Boolean removed) {
		this.removed = removed;
		return this;
	}

	public String getCode() {
		return code;
	}

	public OtpRequestDto setCode(String code) {
		this.code = code;
		return this;
	}

	public UUID getOtpId() {
		return otpId;
	}

	public OtpRequestDto setOtpId(UUID otpId) {
		this.otpId = otpId;
		return this;
	}

	public static OtpRequestDto of(NuOtpRequestRecord record) {
		var dto = new OtpRequestDto();
		dto.id = record.getId();
		dto.createDate = record.getCreateDate();
		dto.modifyDate = record.getModifyDate();
		dto.removed = record.getRemoved();
		dto.code = record.getCode();
		dto.otpId = record.getOtpId();
		return dto;
	}
}
