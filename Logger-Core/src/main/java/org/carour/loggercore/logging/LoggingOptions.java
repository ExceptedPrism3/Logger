package org.carour.loggercore.logging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.File;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LoggingOptions {

	File configFile;

	//Player Loggers
	boolean chatLoggingEnabled;
	boolean commandLoggingEnabled;
	boolean signTextLoggingEnabled;
	boolean joinLoggingEnabled;
	boolean leaveLoggingEnabled;
	boolean kickLoggingEnabled;
	boolean deathLoggingEnabled;
	boolean teleportLoggingEnabled;
	boolean levelChangeLoggingEnabled;
	boolean blockPlaceLoggingEnabled;
	boolean blockBreakLoggingEnabled;
	boolean bucketFillLoggingEnabled;
	boolean bucketEmptyLoggingEnabled;
	boolean anvilUseLoggingEnabled;
	boolean itemPickupLoggingEnabled;
	boolean itemDropLoggingEnabled;
	boolean enchantingLoggingEnabled;
	boolean furnaceLoggingEnabled;
	boolean changeGameModeLoggingEnabled;
	boolean craftingLoggingEnabled;

	//Server Loggers
	boolean startLoggingEnabled;
	boolean stopLoggingEnabled;
	boolean consoleCommandLoggingEnabled;
	boolean ramLoggingEnabled;
	boolean tpsLoggingEnabled;
	boolean portalCreateLoggingEnabled;
	boolean rconLoggingEnabled;

	//Extra Loggers
	boolean essentialsEnabled;
	boolean authMeWrongPasswordEnabled;
	boolean vaultEnabled;

	//Other
	boolean playerIpLoggingEnabled;

	int tableCleanupTreshold;

	public LoggingOptions(File configFile) {
		this.configFile = configFile;
	}

	protected abstract void construct();

}
