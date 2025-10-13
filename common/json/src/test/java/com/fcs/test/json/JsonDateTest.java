package com.fcs.test.json;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

public class JsonDateTest {

	private static Logger log = LogManager.getLogger(JsonDateTest.class);

	@Test
	@DisplayName("test date serialize/deserialize")
	public void testDate() {
		SimpleBean bean = new SimpleBean();
		bean.setDate(OffsetDateTime.now());

		String json = bean.toJson().toString();

		log.info("json: {}", bean.toJson().encodePrettily());

		SimpleBean copy = new JsonObject(json).mapTo(SimpleBean.class);
		log.info("copy json: {}", copy.toJson().encodePrettily());
		if (!copy.getDate().isEqual(bean.getDate())) {
			throw new RuntimeException("date not equals");
		}
	}
}
