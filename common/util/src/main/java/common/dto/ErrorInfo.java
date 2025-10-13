package common.dto;

import io.vertx.core.json.JsonObject;

public class ErrorInfo {
	private String message;
	private String field;

	public static final String FIELD_REQUIRED = "field.required";
	public static final String WRONG_LENGTH = "field.wrongLength";
	public static final String LIST_EMPTY = "list.empty";
	public static final String WRONG_QUERY_PARAM = "wrong.query.param";
	public static final String INVALID_DATE = "field.invalidDate";

	public static ErrorInfo required(String field) {
		return new ErrorInfo().setField(field).setMessage(FIELD_REQUIRED);
	}

	public static ErrorInfo of(String field, String message) {
		return new ErrorInfo().setField(field).setMessage(message);
	}

	public static ErrorInfo listEmpty(String field) {
		return new ErrorInfo().setField(field).setMessage(LIST_EMPTY);
	}

	public static ErrorInfo wrongLength(String field) {
		return new ErrorInfo().setField(field).setMessage(WRONG_LENGTH);
	}

	public static ErrorInfo invalidDate(String field) {
		return new ErrorInfo().setField(field).setMessage(INVALID_DATE);
	}

	public JsonObject toJson() {
		return JsonObject.mapFrom(this);
	}

	public String getMessage() {
		return message;
	}

	public ErrorInfo setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getField() {
		return field;
	}

	public ErrorInfo setField(String field) {
		this.field = field;
		return this;
	}

	@Override
	public String toString() {
		return "ErrorInfo{" +
			"message='" + message + '\'' +
			", field='" + field + '\'' +
			'}';
	}
}
