package com.hnu.db.util;

import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public abstract class JooqDb {

	protected DSLContext db;
	protected final DataType<BigDecimal> SQL_NUMERIC_2 = SQLDataType.NUMERIC(12, 2);
	protected final DataType<BigDecimal> SQL_NUMERIC_3 = SQLDataType.NUMERIC(12, 3);

	public JooqDb(DSLContext db) {
		this.db = db;
	}

	protected Condition arrContains(Field<Long[]> field, Long id) {
		return DSL.arrayOverlap(field, new Long[]{id});
	}

	protected Condition arrContains(Field<UUID[]> field, UUID id) {
		return DSL.arrayOverlap(field, new UUID[]{id});
	}

	protected Condition arrNotContains(Field<Long[]> field, Long id) {
		return arrContains(field, id).not();
	}

	protected Condition arrContainsAny(Field<Long[]> field, Collection<Long> ids) {
		return DSL.arrayOverlap(field, ids.toArray(Long[]::new));
	}

	protected Condition arrContainsAnyStr(Field<String[]> field, Collection<String> str) {
		return DSL.arrayOverlap(field, str.toArray(String[]::new));
	}

	protected Condition arrNotContainsAny(Field<Long[]> field, Collection<Long> ids) {
		return arrContainsAny(field, ids).not();
	}

	protected Condition jsonPropEquals(Field<JSONB> field, String propName, String value) {
		return DSL.condition("({0} ->> {1}) = {2}", field, propName, value);
	}

	protected Condition jsonPropEquals(Field<JSONB> field, String propName, Boolean value) {
		return DSL.condition("({0} -> {1})::boolean = {2}", field, propName, value);
	}

	protected Condition jsonPropIsNull(Field<JSONB> field, String propName) {
		return DSL.condition("({0} -> {1}) is null", field, propName);
	}

	protected Condition jsonPropIsNotNull(Field<JSONB> field, String propName) {
		return DSL.condition("({0} -> {1}) is not null", field, propName);
	}

	protected Field<String> jsonStringProp(Field<JSONB> field, String propName) {
		return DSL.field("{0} ->> {1}", String.class, field, propName);
	}

	protected Condition jsonArrayContains(Field<JSONB> field, String propName, String value) {
		return DSL.condition("({0} -> {1}) @> {2}::jsonb", field, DSL.val(propName), DSL.val("[\"" + value + "\"]"));
	}

	protected Field<Object> jsonProp(Field<JSONB> field, String propName) {
		return DSL.field("{0} -> {1}", Object.class, field, propName);
	}

	protected Field<JSONB> arrayToJson(Field<JSONB[]> field) {
		return DSL.field("array_to_json({0})", JSONB.class, field);
	}

	protected Field<JSONB> jsonConcat(Field<JSONB> f1, Field<JSONB> f2) {
		return DSL.field("({0} || {1})", f1.getDataType(), f1, f2);
	}

	protected Field<String> concatWs(String delimeter, Field<String> f1, Field<String> f2) {
		return DSL.field("concat_ws({0}, {1}, {2})", String.class, delimeter, f1, f2);
	}

	protected Field<String> concatWs(String delimeter, Field<String> f1, Field<String> f2, Field<String> f3) {
		return DSL.field("concat_ws({0}, {1}, {2}, {3})", String.class, delimeter, f1, f2, f3);
	}

	protected Field<String> concatWs(String delimeter, Field<String> f1, Field<String> f2, Field<String> f3, Field<String> f4) {
		return DSL.field("concat_ws({0}, {1}, {2}, {3}, {4})", String.class, delimeter, f1, f2, f3, f4);
	}

	protected Field<String> concatWs(String delimeter, Field<String> f1, Field<String> f2, Field<String> f3, Field<String> f4, Field<String> f5) {
		return DSL.field("concat_ws({0}, {1}, {2}, {3}, {4}, {5})", String.class, delimeter, f1, f2, f3, f4, f5);
	}

	protected Field<JSONB> jsonConcat(Field<JSONB> f1, JSONB f2) {
		return DSL.field("(coalesce({0}, '{}') || coalesce({1}, '{}'))", f1.getDataType(), f1, DSL.val(f2));
	}

	protected Field<JSONB> jsonConcat(Field<JSONB> f1, JsonObject f2) {
		return jsonConcat(f1, JooqUtils.toJSONB(f2));
	}

	protected <T> Field<T> dateTrunc(String datePart, Field<T> field) {
		return DSL.field("date_trunc({0}, {1})",
			field.getDataType(), DSL.inline(datePart), field);
	}
}
