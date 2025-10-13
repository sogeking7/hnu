package common.exception;

import common.dto.ErrorInfo;
import common.util.IdGenerator;
import common.util.Maps;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HxError extends ReplyException {

	public String id;
	public JsonObject data;
	public String description;
	public final static int FAILURE_CODE = 570;

	public HxError(int failureCode, String message) {
		super(ReplyFailure.ERROR, failureCode, message);
		this.id = errorId();
	}

	public static HxError of(String message, JsonObject data) {
		return of(FAILURE_CODE, message, data);
	}

	public static HxError of(int failureCode, String message, JsonObject data) {
		HxError hxError = new HxError(failureCode, message);
		hxError.data = data;
		return hxError;
	}

	public static HxError of(int failureCode, String message, Map<String, Object> data) {
		HxError hxError = new HxError(failureCode, message);
		hxError.data = new JsonObject(data);
		return hxError;
	}

	public static HxError of(String message, Map<String, Object> data) {
		return of(FAILURE_CODE, message, data);
	}

	public static HxError of(String message, Map<String, Object> data, String description) {
		HxError hxError = of(FAILURE_CODE, message, data);
		hxError.description = description;
		return hxError;
	}

	public static HxError of(String message) {
		return new HxError(FAILURE_CODE, message);
	}

	public static HxError unexpected() {
		return HxError.of("unexpected");
	}

	public static HxError requiredField(String field) {
		return HxError.of("required", JsonObject.of("field", field));
	}

	public static HxError of(JsonObject json) {
		return of(json.getString("message"), json.getJsonObject("data"));
	}

	public static HxError accessDenied() {
		return of("accessDenied");
	}

	public static HxError unimplemented(String message) {
		return of("unimplemented", Maps.of("message", message));
	}

    public String getMessageCode() {
		return super.getMessage();
	}

	@Override
	public String getMessage() {
		return toJson().encode();
	}

	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject()
			.put("id", this.id)
			.put("message", super.getMessage());

		Optional.ofNullable(super.getCause()).ifPresent(cause -> jsonObject.put("cause", cause.getMessage()));

		if (data != null) {
			jsonObject.put("data", data);
		}
		if (description != null) {
			jsonObject.put("description", description);
		}

		return jsonObject;
	}

	private static String errorId() {
		return IdGenerator.getBase62(6);
	}

	public static void throwIfNotEmpty(List<ErrorInfo> errors) {
		if (!errors.isEmpty()) {
			throw HxError.of("requiredFields", Map.of("fields", errors));
		}
	}
}
