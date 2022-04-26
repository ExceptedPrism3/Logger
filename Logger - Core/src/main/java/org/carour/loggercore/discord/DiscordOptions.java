package org.carour.loggercore.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.File;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class DiscordOptions {

	File file;

	boolean enabled;
	String token;

	boolean staffEnabled;
	boolean playerChatEnabled;
	boolean playerCommandsEnabled;
	boolean consoleCommandsEnabled;
	boolean playerSignTextEnabled;
	boolean playerJoinEnabled;
	boolean playerLeaveEnabled;
	boolean playerKickEnabled;
	boolean playerDeathEnabled;
	boolean playerTeleportEnabled;
	boolean playerLevelEnabled;
	boolean blockBreakEnabled;
	boolean blockPlaceEnabled;
	boolean portalCreateEnabled;
	boolean bucketPlaceEnabled;
	boolean anvilUseEnabled;
	boolean tpsEnabled;
	boolean ramEnabled;
	boolean serverStartEnabled;
	boolean serverStopEnabled;
	boolean itemDropEnabled;
	boolean itemPickupEnabled;
	boolean enchantEnabled;
	boolean bookEditEnabled;
	boolean furnaceEnabled;
	boolean afkEnabled;

	String staffChannelID;
	String playerChatChannelID;
	String playerCommandsChannelID;
	String consoleCommandsChannelID;
	String playerSignTextChannelID;
	String playerJoinChannelID;
	String playerLeaveChannelID;
	String playerKickChannelID;
	String playerDeathChannelID;
	String playerTeleportChannelID;
	String playerLevelChannelID;
	String blockBreakChannelID;
	String blockPlaceChannelID;
	String portalCreateChannelID;
	String bucketPlaceChannelID;
	String anvilUseChannelID;
	String tpsChannelID;
	String ramChannelID;
	String serverStartChannelID;
	String serverStopChannelID;
	String itemDropChannelID;
	String itemPickupChannelID;
	String enchantChannelID;
	String bookEditChannelID;
	String furnaceChannelID;
	String afkChannelID;

	public DiscordOptions(File file) {
		this.file = file;
	}

	protected abstract void construct();

}
