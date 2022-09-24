package me.prism3.logger.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

	private Log() {}

	private static Logger logger;

	public static void setup(final Logger log) {
		Log.logger = log;
	}

	public static void info(final String message) {
		logger.info(message);
	}

	public static void severe(final String message) {
		logger.severe(message);
	}

	public static void severe(final String message, final Throwable thrown) {
		logger.log(Level.SEVERE, message, thrown);
	}

	public static void warning(final String message) {
		logger.warning(message);
	}

	public static void warning(final String message, final Throwable thrown) {
		logger.log(Level.WARNING, message, thrown);
	}
}
