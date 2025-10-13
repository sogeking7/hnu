package common.util;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Enums {

	private static final Logger log = LogManager.getLogger(Enums.class);

	@Nullable
	public static <T extends Enum<T>> T of(String name, Class<T> enumType) {
		if (Strings.isEmpty(name)) {
			return null;
		}
		try {
			return Enum.valueOf(enumType, name);
		} catch (Exception e) {
			log.atError().withThrowable(e).log("failed to convert: {} - {}", name, enumType.getCanonicalName());
		}
		return null;
	}

	@Nullable
	public static String name(Enum en) {
		if (en != null) {
			return en.name();
		}
		return null;
	}

	@NotNull
	public static <T extends Enum<T>> Optional<T> ofNullable(String name, Class<T> enumType) {
		if (Strings.isEmpty(name)) {
			return Optional.empty();
		}
		try {
			return Optional.of(Enum.valueOf(enumType, name));
		} catch (Exception e) {
			log.atError().withThrowable(e).log("failed to convert: {} - {}", name, enumType.getCanonicalName());
		}
		return Optional.empty();
	}
}
