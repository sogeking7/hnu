package common.enumeration;

import org.eclipse.microprofile.config.ConfigProvider;

public enum AppMode {
	DEV("dev"),
	PROD("prod"),
	STAGING("staging"),
	TEST("test");

	private final String mode;

	AppMode(String mode) {
		this.mode = mode;
	}

	public static AppMode of(String mode) {
		return AppMode.valueOf(mode.toUpperCase());
	}

	public static boolean isProd() {
		return ConfigProvider.getConfig().getOptionalValue("quarkus.profile", String.class)
			.orElse("dev")
			.equals("prod");
	}

	public static boolean isStaging() {
		return ConfigProvider.getConfig().getOptionalValue("quarkus.profile", String.class)
			.orElse("dev")
			.equals("staging");
	}

	public String value() {
		return this.mode;
	}
}
