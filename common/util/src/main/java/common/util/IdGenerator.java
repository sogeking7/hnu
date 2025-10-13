package common.util;

import com.fasterxml.uuid.Generators;

import java.util.Random;
import java.util.UUID;

/**
 * @author Nurlan Rakhimzhanov <nurlan.rakhimzhanov@gmail.com>
 * @date 13.10.2017 12:03
 */
public class IdGenerator {

	private static final char[] _base62chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	private static final Random _random = new Random();

	public static String getBase62(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(_base62chars[_random.nextInt(62)]);
		}
		return sb.toString();
	}

	public static String getBase36(int length) {
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			sb.append(_base62chars[_random.nextInt(36)]);
		}
		return sb.toString();
	}

	public static UUID v7() {
		return Generators.timeBasedEpochGenerator().generate();
	}

}
