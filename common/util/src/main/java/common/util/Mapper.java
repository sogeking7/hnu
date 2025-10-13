package common.util;

import common.dto.JsonSerializable;
import common.exception.HxError;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Mapper {

	private static final Logger log = LogManager.getLogger(Mapper.class);

	public static <T> T map(Class<T> type, @NotNull JsonObject jsonObject) {
		if (jsonObject == null) {
			throw HxError.of("json.conversion.undefined");
		}
		try {
			return DatabindCodec.mapper().convertValue(jsonObject, type);
		} catch (Throwable e) {
			log.error(e);
			throw HxError.of("json.conversion:" + type.getCanonicalName());
		}
	}

	public static <T> Optional<T> mapNullable(Class<T> type, @Nullable JsonObject jsonObject) {
		if (jsonObject == null) {
			return Optional.empty();
		}
		try {
			return Optional.of(DatabindCodec.mapper().convertValue(jsonObject, type));
		} catch (Throwable e) {
			log.error(e);
			throw HxError.of("json.conversion:" + type.getCanonicalName());
		}
	}

	public static <T> List<T> map(Class<T> type, @NotNull JsonArray jsonArray) {
		if (jsonArray == null) {
			throw HxError.of("json.conversion.undefined");
		}
		try {
			List<T> list = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				T object = jsonArray.getJsonObject(i).mapTo(type);
				list.add(object);
			}
			return list;
		} catch (Throwable e) {
			log.error(e);
			throw HxError.of("json.conversion:" + type.getCanonicalName());
		}
	}

	public static List<Integer> mapToInt(@NotNull JsonArray jsonArray) {
		if (jsonArray == null) {
			throw HxError.of("json.conversion.undefined");
		}
		try {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				Integer object = jsonArray.getInteger(i);
				list.add(object);
			}
			return list;
		} catch (Throwable e) {
			log.error(e);
			throw HxError.of("json.conversion");
		}
	}

	public static <T> JsonArray toJsonArray(List<T> list, Function<T, JsonObject> mapFn) {
		var arr = new JsonArray();
		if (list != null) {
			list.stream().map(mapFn).forEach(arr::add);
		}
		return arr;
	}

	public static <T extends JsonSerializable> JsonArray toJsonArray(List<T> list) {
		var arr = new JsonArray();
		if (list != null) {
			list.stream().map(JsonSerializable::toJson).forEach(arr::add);
		}
		return arr;
	}
}
