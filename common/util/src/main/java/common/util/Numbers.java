package common.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;

public class Numbers {

	public static @NotNull Double toDouble(BigDecimal value, double def) {
		if (value == null) {
			return def;
		}
		return value.doubleValue();
	}

	public static Double toDouble(BigDecimal value) {
		if (value == null) {
			return null;
		}
		return value.doubleValue();
	}

	@NotNull
	public static BigDecimal toBigDecimal(Double value, @NotNull BigDecimal def) {
		if (value == null) {
			return def;
		}
		return BigDecimal.valueOf(value);
	}

	@NotNull
	public static BigDecimal toBigDecimal(Integer value, @NotNull BigDecimal def) {
		if (value == null) {
			return def;
		}
		return BigDecimal.valueOf(value);
	}

	public static BigDecimal toBigDecimal(Double value) {
		if (value == null) {
			return null;
		}
		return BigDecimal.valueOf(value);
	}

	public static BigDecimal toBigDecimal(Optional<Double> value) {
		return toBigDecimal(value.orElse(null));
	}

	@NotNull
	public static Double val(Double value, double defaultValue) {
		return value == null ? defaultValue : value;
	}

	public static long val(Long value) {
		return value == null ? 0 : value;
	}

	public static double val(Double value) {
		return value == null ? 0 : value;
	}

	public static BigDecimal val(BigDecimal value) {
		return value == null ? ZERO : value;
	}

	public static long val(Integer value) {
		return value == null ? 0 : value;
	}

	public static Integer toInteger(BigDecimal bd) {
		if (bd == null) {
			return null;
		}
		return bd.intValue();
	}

	@NotNull
	public static Integer toInteger(BigDecimal bd, int defaultValue) {
		if (bd == null) {
			return defaultValue;
		}
		return bd.intValue();
	}

	public static BigDecimal toBigDecimal(Integer amount) {
		if (amount == null) {
			return null;
		}
		return BigDecimal.valueOf(amount);
	}

	public static Double negate(Double value) {
		return value * -1;
	}

	public static BigDecimal negate(BigDecimal value) {
		return value.negate();
	}

	public static Double negative(Double val) {
		if (val == null) {
			return null;
		}
		if (val < 0) {
			return val;
		}
		return negate(val);
	}

	public static BigDecimal val(BigDecimal value, BigDecimal def) {
		if (value == null) {
			return def;
		}
		return value;
	}

	public static Integer toInteger(Double d) {
		if (d == null) {
			return null;
		}
		return d.intValue();
	}

	@NotNull
	public static Double abs(Double value) {
		return Math.abs(val(value));
	}

	@NotNull
	public static Double abs(Double value, Double def) {
		return Math.abs(val(value, def));
	}

	public static boolean isNegative(BigDecimal n) {
		return n != null && n.compareTo(ZERO) < 0;
	}

	public static boolean isPositive(BigDecimal n) {
		return n != null && n.compareTo(ZERO) > 0;
	}

	@NotNull
	public static Double add(Double... numbers) {
		return Stream.of(numbers)
			.filter(Objects::nonNull)
			.map(Numbers::toBigDecimal)
			.reduce(BigDecimal::add)
			.map(BigDecimal::doubleValue)
			.orElse(0.0);
	}

	@NotNull
	public static Integer add(Integer... numbers) {
		return Stream.of(numbers)
			.filter(Objects::nonNull)
			.reduce(Integer::sum)
			.orElse(0);
	}

	public static Double subtract(Double n, Double... numbers) {
		var sum = toBigDecimal(add(numbers));
		return toBigDecimal(n, ZERO).subtract(sum).doubleValue();
	}

	@NotNull
	public static BigDecimal subtract(@NotNull BigDecimal n, BigDecimal... numbers) {
		var sum = add(numbers);
		return n.subtract(sum);
	}

	public static Double subtract(Integer n, Double... numbers) {
		var sum = toBigDecimal(add(numbers));
		return toBigDecimal(n, ZERO).subtract(sum).doubleValue();
	}

	public static BigDecimal add(BigDecimal... numbers) {
		return Stream.of(numbers)
			.filter(Objects::nonNull)
			.reduce(BigDecimal::add)
			.orElse(ZERO);
	}

	public static Double multiply(Integer n, Double... args) {
		return BigDecimal.valueOf(multiply(args)).multiply(BigDecimal.valueOf(n)).doubleValue();
	}

	public static BigDecimal multiply(BigDecimal... args) {
		return Stream.of(args)
			.reduce(BigDecimal::multiply)
			.orElse(ZERO);
	}

	public static Double multiply(Long n, Double... args) {
		return BigDecimal.valueOf(multiply(args)).multiply(BigDecimal.valueOf(n)).doubleValue();
	}

	public static Double multiply(Double... args) {
		return Stream.of(args).map(Numbers::toBigDecimal).reduce(BigDecimal::multiply)
			.map(BigDecimal::doubleValue)
			.orElse(0.0);
	}

	public static Integer toInteger(Long l) {
		if (l == null) {
			return null;
		}
		return l.intValue();
	}

	public static boolean isPositive(Double d) {
		if (d == null) {
			return false;
		}
		return d > 0;
	}

	@NotNull
	public static Double round(@NotNull Double num) {
		return Math.round(num * 100.0) / 100.0;
	}

	@NotNull
	public static BigDecimal round(@NotNull BigDecimal num) {
		return round(num.multiply(toBigDecimal(100)), 0).divide(toBigDecimal(100));
	}

	public static Double positive(Double n) {
		if (n == null) {
			return null;
		}
		if (n > 0) {
			return n;
		}
		return n * (-1);
	}

	public static @NotNull Double toFixed(@NotNull Double d, int scale) {
		return BigDecimal.valueOf(d).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	public static @NotNull Double toFixed(@NotNull Double d) {
		return toFixed(d, 2);
	}

	public static @NotNull BigDecimal toFixed(BigDecimal d) {
		return round(d, 2);
	}

	public static boolean gt(@NotNull BigDecimal a, @NotNull Integer b) {
		return a.compareTo(Numbers.toBigDecimal(b)) > 0;
	}

	public static boolean gt(@NotNull Double a, @NotNull Integer b) {
		return Numbers.toBigDecimal(a).compareTo(Numbers.toBigDecimal(b)) > 0;
	}

	public static boolean gt(@NotNull BigDecimal a, @NotNull BigDecimal b) {
		return a.compareTo(b) > 0;
	}

	public static boolean le(@NotNull BigDecimal a, @NotNull Integer b) {
		return a.compareTo(Numbers.toBigDecimal(b)) <= 0;
	}

	public static boolean le(@NotNull Double a, @NotNull Double b) {
		return le(toBigDecimal(a), toBigDecimal(b));
	}

	public static boolean le(@NotNull Double a, @NotNull Integer b) {
		return le(toBigDecimal(a), toBigDecimal(b));
	}

	public static boolean le(@NotNull BigDecimal a, @NotNull Double b) {
		return a.compareTo(Numbers.toBigDecimal(b)) <= 0;
	}

	public static boolean le(@NotNull BigDecimal a, @NotNull BigDecimal b) {
		return a.compareTo(b) <= 0;
	}

	public static Double divide(Double n1, Double n2, RoundingMode roundingMode, Integer precision) {
		return BigDecimal.valueOf(n1).divide(BigDecimal.valueOf(n2), precision, roundingMode).doubleValue();
	}

	public static BigDecimal divide(BigDecimal n1, BigDecimal n2, RoundingMode roundingMode, Integer precision) {
		return n1.divide(n2, precision, roundingMode);
	}

	public static boolean isEqual(Double d1, Double d2) {
		return eq(d1, d2);
	}

	public static boolean isEqual(Integer d1, Integer d2) {
		return eq(d1, d2);
	}

	public static boolean isNotEqual(Double d1, Double d2) {
		return ne(d1, d2);
	}

	public static boolean isNotEqual(Integer d1, Integer d2) {
		return ne(d1, d2);
	}

	public static boolean ne(BigDecimal d1, BigDecimal d2) {
		return !eq(d1, d2);
	}

	public static boolean isNotNull(Double value) {
		return value != null && value != 0;
	}

	public static boolean eq(Double d1, Double d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null || d2 == null) {
			return false;
		}
		return BigDecimal.valueOf(d1).compareTo(BigDecimal.valueOf(d2)) == 0;
	}

	public static boolean eq(BigDecimal d1, BigDecimal d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null || d2 == null) {
			return false;
		}
		return d1.compareTo(d2) == 0;
	}

	public static boolean eq(BigDecimal d1, Double d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null || d2 == null) {
			return false;
		}
		return d1.compareTo(BigDecimal.valueOf(d2)) == 0;
	}

	public static boolean eq(Integer d1, Integer d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null || d2 == null) {
			return false;
		}
		return Objects.equals(d1, d2);
	}

	public static boolean ne(Double d1, Double d2) {
		return !eq(d1, d2);
	}

	public static boolean ne(Double d1, Integer d2) {
		return !eq(toBigDecimal(d1), toBigDecimal(d2));
	}

	public static boolean eq(Double d1, Integer d2) {
		return eq(toBigDecimal(d1), toBigDecimal(d2));
	}

	public static boolean ne(Integer d1, Integer d2) {
		return !eq(d1, d2);
	}

	public static boolean ne(BigDecimal d1, Double d2) {
		return ne(d1.doubleValue(), d2);
	}

	public static BigDecimal negative(BigDecimal n) {
		return n == null ? null : (isNegative(n) ? n : n.negate());
	}

	@Null
	public static BigDecimal positive(BigDecimal n) {
		return n == null ? null : (isPositive(n) ? n : n.negate());
	}

	public static boolean isNotNullOrZero(BigDecimal d) {
		return d != null && (d.compareTo(ZERO) != 0);
	}

	public static boolean isNotZero(@NotNull BigDecimal value) {
		return value.compareTo(ZERO) != 0;
	}

	public static BigDecimal multiply(@NotNull Integer d1, @NotNull BigDecimal... d2) {
		return multiply(d2).multiply(new BigDecimal(d1));
	}

	public static boolean isZero(BigDecimal d) {
		return d.compareTo(ZERO) == 0;
	}

	public static boolean ge(BigDecimal d1, BigDecimal d2) {
		return d1.compareTo(d2) >= 0;
	}

	public static boolean ge(BigDecimal d1, Integer d2) {
		return d1.compareTo(toBigDecimal(d2)) >= 0;
	}

	@NotNull
	public static BigDecimal round(@NotNull BigDecimal d, int scale) {
		return d.setScale(scale, RoundingMode.HALF_UP);
	}

	@NotNull
	public static Double round(@NotNull Double d, int scale) {
		return toBigDecimal(d).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	public static boolean lt(BigDecimal n1, int n2) {
		return lt(n1, toBigDecimal(n2));
	}

	public static boolean lt(Double n1, int n2) {
		return lt(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static boolean lt(BigDecimal n1, BigDecimal n2) {
		return n1.compareTo(n2) < 0;
	}

	public static boolean lt(Integer n1, BigDecimal n2) {
		return toBigDecimal(n1).compareTo(n2) < 0;
	}

	public static boolean lt(Integer n1, Double n2) {
		return toBigDecimal(n1).compareTo(toBigDecimal(n2)) < 0;
	}

	public static boolean eq(BigDecimal n1, int n2) {
		return eq(n1, toBigDecimal(n2));
	}

	public static boolean gt(Double n1, Double n2) {
		return gt(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static boolean ge(Double n1, Double n2) {
		return ge(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static Double divide(Double n1, int n2) {
		return divide(n1, (double) n2, RoundingMode.HALF_UP, 3);
	}

	public static Double divide(Double n1, Double n2) {
		return divide(n1, n2, RoundingMode.HALF_UP, 3);
	}

	public static boolean lt(Double n1, Double n2) {
		return lt(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static boolean ge(Double n1, int n2) {
		return ge(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static boolean ne(Integer n1, Double n2) {
		return ne(toBigDecimal(n1), toBigDecimal(n2));
	}

	public static @Nullable BigDecimal coalesce(BigDecimal... numbers) {
		return Arrays.stream(numbers).filter(Objects::nonNull)
			.filter(n -> ge(n, ZERO))
			.findFirst()
			.orElse(null);
	}

	public static @Nullable Double coalesce(Double... numbers) {
		return Arrays.stream(numbers).filter(Objects::nonNull)
			.filter(n -> ge(n, 0.0))
			.findFirst()
			.orElse(null);
	}
}
