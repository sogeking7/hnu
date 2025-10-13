package common.dto;

import io.vertx.core.json.JsonObject;

public abstract class CommonData {

	public CommonData() {
	}

	public CommonData(JsonObject jsonObject) {
		fromJson(jsonObject);
	}

	public void fromJson(JsonObject jsonObject) {}
}
