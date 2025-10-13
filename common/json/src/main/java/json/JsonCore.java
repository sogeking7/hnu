package json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.jackson.DatabindCodec;

public class JsonCore {
	private static ObjectMapper ignoreUnknownMapper;

	public static void configure(ObjectMapper objectMapper) {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		objectMapper.registerModule(new JsonObjectModule());
//		objectMapper.registerModule(new VertxModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new DateModule());
		objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
	}

	public static void configure() {
		configure(DatabindCodec.mapper());
	}

	public static ObjectMapper ignoreUnknownMapperInstance() {
		if (ignoreUnknownMapper == null) {
			ignoreUnknownMapper = DatabindCodec.mapper().copy();
			ignoreUnknownMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return ignoreUnknownMapper;
	}
}
