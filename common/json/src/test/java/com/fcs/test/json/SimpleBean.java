package com.fcs.test.json;


import com.fasterxml.jackson.annotation.JsonFormat;
import hx.common.dto.JsonSerializable;

import java.time.OffsetDateTime;


public class SimpleBean extends JsonSerializable {

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private OffsetDateTime date;

	public OffsetDateTime getDate() {
		return date;
	}

	public SimpleBean setDate(OffsetDateTime date) {
		this.date = date;
		return this;
	}
}
