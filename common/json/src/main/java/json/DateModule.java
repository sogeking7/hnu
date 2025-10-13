package json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateModule extends SimpleModule {

	public DateModule() {
		addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
			@Override
			public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				if(value == null) {
					gen.writeNull();
				} else {
					gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
				}
			}
		});
		addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
	}

}
