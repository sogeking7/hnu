package common.util;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Strings {

	public static boolean isEmpty(String str) {
		return str == null || str.strip().isBlank();
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isNumeric(String cs) {
		if (isEmpty(cs)) {
			return false;
		} else {
			int sz = cs.length();

			for (int i = 0; i < sz; ++i) {
				if (!Character.isDigit(cs.charAt(i))) {
					return false;
				}
			}

			return true;
		}
	}

	public static String randomUpperCaseLetter() {
		return String.valueOf("abcdefghijklmnopqrstuvwxyz".charAt(new Random().nextInt(25)));
	}

	public static @NotNull String concat(@NotNull String delimiter, String... strings) {
		return Stream.of(strings).filter(Objects::nonNull).collect(Collectors.joining(delimiter)).trim();
	}

	public static String right(String value, int length) {
		if (value != null && value.length() > length) {
			return value.substring(value.length() - length);
		}
		return value;
	}

	public static String left(String value, int length) {
		if (value != null && value.length() > length) {
			return value.substring(0, length);
		}
		return value;
	}

	public static boolean hasLetters(String str) {
		if (str == null) {
			return false;
		}
		return str.chars().anyMatch(Character::isLetter);
	}

	@Nullable
	public static String getDigits(String text) {
		return StringUtils.getDigits(text);
	}

	/**
	 * Copied from apache commons text
	 * https://github.com/apache/commons-text/blob/master/src/main/java/org/apache/commons/text/CaseUtils.java
	 * <p>Converts all the delimiter separated words in a String into camelCase,
	 * that is each word is made up of a title case character and then a series of
	 * lowercase characters.</p>
	 *
	 * <p>The delimiters represent a set of characters understood to separate words.
	 * The first non-delimiter character after a delimiter will be capitalized. The first String
	 * character may or may not be capitalized and it's determined by the user input for capitalizeFirstLetter
	 * variable.</p>
	 *
	 * <p>A {@code null} input String returns {@code null}.
	 * Capitalization uses the Unicode title case, normally equivalent to
	 * upper case and cannot perform locale-sensitive mappings.</p>
	 *
	 * <pre>
	 * CaseUtils.toCamelCase(null, false)                                 = null
	 * CaseUtils.toCamelCase("", false, *)                                = ""
	 * CaseUtils.toCamelCase(*, false, null)                              = *
	 * CaseUtils.toCamelCase(*, true, new char[0])                        = *
	 * CaseUtils.toCamelCase("To.Camel.Case", false, new char[]{'.'})     = "toCamelCase"
	 * CaseUtils.toCamelCase(" to @ Camel case", true, new char[]{'@'})   = "ToCamelCase"
	 * CaseUtils.toCamelCase(" @to @ Camel case", false, new char[]{'@'}) = "toCamelCase"
	 * </pre>
	 *
	 * @param str                   the String to be converted to camelCase, may be null
	 * @param capitalizeFirstLetter boolean that determines if the first character of first word should be title case.
	 * @param delimiters            set of characters to determine capitalization, null and/or empty array means whitespace
	 * @return camelCase of String, {@code null} if null String input
	 */
	public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		str = str.toLowerCase();
		final int strLen = str.length();
		final int[] newCodePoints = new int[strLen];
		int outOffset = 0;
		final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
		boolean capitalizeNext = false;
		if (capitalizeFirstLetter) {
			capitalizeNext = true;
		}
		for (int index = 0; index < strLen; ) {
			final int codePoint = str.codePointAt(index);

			if (delimiterSet.contains(codePoint)) {
				capitalizeNext = outOffset != 0;
				index += Character.charCount(codePoint);
			} else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
				final int titleCaseCodePoint = Character.toTitleCase(codePoint);
				newCodePoints[outOffset++] = titleCaseCodePoint;
				index += Character.charCount(titleCaseCodePoint);
				capitalizeNext = false;
			} else {
				newCodePoints[outOffset++] = codePoint;
				index += Character.charCount(codePoint);
			}
		}
		if (outOffset != 0) {
			return new String(newCodePoints, 0, outOffset);
		}
		return str;
	}

	public static String snakeCaseToCamelCase(String str) {
		return toCamelCase(str, false, '_');
	}

	public static String concatFIO(String lastname, String firstname, String patronymic) {
		return ((lastname == null) ? StringUtils.EMPTY : lastname)
			   + StringUtils.SPACE
			   + ((firstname == null) ? StringUtils.EMPTY : firstname)
			   + StringUtils.SPACE
			   + ((patronymic == null) ? StringUtils.EMPTY : patronymic);
	}

	/**
	 * <p>Converts an array of delimiters to a hash set of code points. Code point of space(32) is added
	 * as the default value. The generated hash set provides O(1) lookup time.</p>
	 *
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return Set<Integer>
	 */
	private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
		final Set<Integer> delimiterHashSet = new HashSet<>();
		delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
		if (ArrayUtils.isEmpty(delimiters)) {
			return delimiterHashSet;
		}

		for (int index = 0; index < delimiters.length; index++) {
			delimiterHashSet.add(Character.codePointAt(delimiters, index));
		}
		return delimiterHashSet;
	}

	@Nullable
	public static String trim(String str) {
		if (str == null) {
			return null;
		}
		return str.trim();
	}

	@Nullable
	public static String nullIfEmpty(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return trim(str);
	}

	public static String concat(String delimiter, Collection<String> collection) {
		return collection.stream().filter(Objects::nonNull).collect(Collectors.joining(delimiter)).trim();
	}

	public static Optional<String> nonEmpty(String str) {
		if (isEmpty(str)) {
			return Optional.empty();
		}
		return Optional.of(str);
	}
}
