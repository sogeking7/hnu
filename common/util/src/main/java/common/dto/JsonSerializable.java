package common.dto;

import io.vertx.core.json.JsonObject;

// rename to json serializable
public abstract class JsonSerializable {

	@Override
	public String toString() {
		return toJson().toString();
	}

	public JsonObject toJson() {
		return JsonObject.mapFrom(this);
	}
}
