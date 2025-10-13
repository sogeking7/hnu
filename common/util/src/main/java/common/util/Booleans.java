package common.util;

import java.util.stream.Stream;

public class Booleans {

	public static boolean val(Boolean bool) {
		if (bool == null) {
			return false;
		}
		return bool;
	}

	public static boolean val(Boolean bool, Boolean defaultValue) {
		if (bool == null) {
			return defaultValue;
		}
		return bool;
	}

	public static Boolean and(Boolean... vals) {
		return Stream.of(vals).map(Booleans::val).reduce(Boolean::logicalAnd).orElse(false);
	}

	public static Boolean or(Boolean... vals) {
		return Stream.of(vals).map(Booleans::val).reduce(Boolean::logicalOr).orElse(false);
	}

	public static boolean not(Boolean bool) {
		return !val(bool);
	}
}
