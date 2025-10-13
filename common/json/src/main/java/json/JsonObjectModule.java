package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectModule extends SimpleModule {

	public JsonObjectModule() {
		addDeserializer(JsonObject.class, new JsonDeserializer<>() {
			@Override
			public JsonObject deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
				com.fasterxml.jackson.databind.node.ObjectNode object = jp.getCodec().readTree(jp);
				if (object == null) {
					return null;
				}
				return new JsonObject(object.toString());
			}
		});
	}

}
