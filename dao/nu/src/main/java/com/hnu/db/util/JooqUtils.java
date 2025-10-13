package com.hnu.db.util;

import common.dto.CommonData;
import common.util.Mapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.jooq.JSONB.jsonb;

public class JooqUtils {
	@Nullable
	public static JsonObject toJsonObject(JSONB json) {
		if (json == null) {
			return null;
		}
		return new JsonObject(json.data());
	}

	@Nullable
	public static Buffer toBuffer(JSONB json) {
		if (json == null) {
			return null;
		}
		return Buffer.buffer(json.data());
	}

	@Nullable
	public static JsonArray toJsonArray(JSONB json) {
		if (json == null) {
			return null;
		}
		return new JsonArray(json.data());
	}

	@Nullable
	public static JSONB toJSONB(JsonObject json) {
		if (json == null) {
			return null;
		}
		return jsonb(json.encode());
	}

	@Nullable
	public static JSONB toJSONB(JsonArray json) {
		if (json == null) {
			return null;
		}
		return jsonb(json.encode());
	}

	@Nullable
	public static JSONB toJSONB(Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			return null;
		}
		return jsonb(new JsonObject(jsonMap).encode());
	}

	@Nullable
	public static JSONB toJSONB(CommonData label) {
		if (label == null) {
			return null;
		}
		return JSONB.valueOf(JsonObject.mapFrom(label).encode());
	}

	@Nullable
	public static JSONB toJSONB(java.lang.Record record) {
		if (record == null) {
			return null;
		}
		return JSONB.valueOf(JsonObject.mapFrom(record).encode());
	}

	@Nullable
	public static JSONB toJSONB(Collection<? extends java.lang.Record> list) {
		if (list == null) {
			return null;
		}
		JsonArray arr = new JsonArray();
		list.forEach(el -> arr.add(JsonObject.mapFrom(el)));
		return JSONB.valueOf(arr.encode());
	}

	@Nullable
	public static Map<String, Object> toMap(JSONB json) {
		if (json == null) {
			return null;
		}
		return new JsonObject(json.data()).getMap();
	}

	@Nullable
	public static <T> T jsonToObject(Class<T> clazz, JSONB json) {
		if (json == null) {
			return null;
		}
		return Mapper.map(clazz, toJsonObject(json));
	}

	public static <T> List<T> jsonToObjectList(Class<T> clazz, JSONB json) {
		if (json == null) {
			return List.of();
		}
		return Mapper.map(clazz, toJsonArray(json));
	}

	public static <R extends Record> R mapper(JsonObject data, Table<R> table) {
		R newRecord = table.newRecord();

		Objects.requireNonNull(data, "data is null");
		Objects.requireNonNull(data.getMap(), "data.map is null");

		Map<String, Object> map = new HashMap<>(data.getMap());

//		for (Field field : table.fields()) {
//			if (LocalDate.class.equals(field.getDataType().getType())) {
//				Optional.ofNullable(data.getInteger(field.getName()))
//					.ifPresent(i -> map.put(field.getName(), LocalDate.ofEpochDay(i)));
//			}
//		}

		newRecord.fromMap(map);
		return newRecord;
	}

	public static void nullsAsUnchanged(UpdatableRecord record) {
		for (Field<?> field : record.fields()) {
			if (record.getValue(field) == null) {
				record.touched(field, false);
			}
		}
	}

	public static <R extends Record, T> R nullIfEmpty(@NotNull R record, TableField<R, T> tableField) {
		Object obj = record.get(tableField);
		return obj == null ? null : record;
	}
}
