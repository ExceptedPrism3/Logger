package org.carour.loggercore.discord;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.util.UUID;
import java.util.logging.Logger;

@Getter
public class Discord {

	private final Logger logger;

	private final DiscordOptions options;
	private static JDA jda;

	private static TextChannel staffChannel;
	private static TextChannel playerChatChannel;
	private static TextChannel playerCommandsChannel;
	private static TextChannel consoleChannel;
	private static TextChannel playerSignTextChannel;
	private static TextChannel playerJoinChannel;
	private static TextChannel playerLeaveChannel;
	private static TextChannel playerKickChannel;
	private static TextChannel playerDeathChannel;
	private static TextChannel playerTeleportChannel;
	private static TextChannel playerLevelChannel;
	private static TextChannel blockPlaceChannel;
	private static TextChannel blockBreakChannel;
	private static TextChannel portalCreationChannel;
	private static TextChannel bucketPlaceChannel;
	private static TextChannel anvilChannel;
	private static TextChannel TPSChannel;
	private static TextChannel RAMChannel;
	private static TextChannel serverStartChannel;
	private static TextChannel serverStopChannel;
	private static TextChannel itemDropChannel;
	private static TextChannel enchantingChannel;
	private static TextChannel bookEditingChannel;
	private static TextChannel afkChannel;
	private static TextChannel itemPickupChannel;
	private static TextChannel furnaceChannel;

	public Discord(DiscordOptions options, Logger logger) {
		this.options = options;
		this.logger = logger;
	}

	public void run() {

		if (options.isEnabled()) {

			String botToken = options.getToken();

			try {

				jda = JDABuilder.createDefault(botToken).build().awaitReady();

			} catch (InterruptedException | LoginException e) {

				logger.severe("An error has occurred whilst connecting to the Bot. Is the Bot Key Valid?");

				return;

			}

			String staffChannelID = options.getStaffChannelID();

			String playerChatChannelID = options.getPlayerChatChannelID();

			String playerCommandsChannelID = options.getPlayerCommandsChannelID();

			String consoleCommandsChannelID = options.getConsoleCommandsChannelID();

			String playerSignTextChannelID = options.getPlayerSignTextChannelID();

			String playerJoinChannelID = options.getPlayerJoinChannelID();

			String playerLeaveChannelID = options.getPlayerLeaveChannelID();

			String playerKickChannelID = options.getPlayerKickChannelID();

			String playerDeathChannelID = options.getPlayerDeathChannelID();

			String playerTeleportChannelID = options.getPlayerTeleportChannelID();

			String playerLevelChannelID = options.getPlayerLevelChannelID();

			String blockPlaceChannelID = options.getBlockPlaceChannelID();

			String blockBreakChannelID = options.getBlockBreakChannelID();

			String portalCreationChannelID = options.getPortalCreateChannelID();

			String bucketPlaceChannelID = options.getBucketPlaceChannelID();

			String anvilChannelID = options.getAnvilUseChannelID();

			String TPSChannelID = options.getTpsChannelID();

			String RAMChannelID = options.getRamChannelID();

			String serverStartChannelID = options.getServerStartChannelID();

			String serverStopChannelID = options.getServerStopChannelID();

			String itemDropChannelID = options.getItemDropChannelID();

			String enchantingChannelID = options.getEnchantChannelID();

			String bookEditingChannelID = options.getBookEditChannelID();

			String afkChannelID = options.getAfkChannelID();

			String itemPickupChannelID = options.getItemPickupChannelID();

			String furnaceChannelID = options.getFurnaceChannelID();

			if (staffChannelID != null && options.isStaffEnabled() && !staffChannelID.equals("LINK_HERE")) {

				staffChannel = jda.getTextChannelById(options.getStaffChannelID());

			}

			if (playerChatChannelID != null && options.isPlayerChatEnabled() && !playerChatChannelID.equals("LINK_HERE")) {

				playerChatChannel = jda.getTextChannelById(playerChatChannelID);

			}

			if (playerCommandsChannelID != null && options.isPlayerCommandsEnabled() && !playerCommandsChannelID.equals("LINK_HERE")) {

				playerCommandsChannel = jda.getTextChannelById(playerCommandsChannelID);

			}

			if (consoleCommandsChannelID != null && options.isConsoleCommandsEnabled() && !consoleCommandsChannelID.equals("LINK_HERE")) {

				consoleChannel = jda.getTextChannelById(consoleCommandsChannelID);

			}

			if (playerSignTextChannelID != null && options.isPlayerSignTextEnabled() && !playerSignTextChannelID.equals("LINK_HERE")) {

				playerSignTextChannel = jda.getTextChannelById(playerSignTextChannelID);

			}

			if (playerJoinChannelID != null && options.isPlayerJoinEnabled() && !playerJoinChannelID.equals("LINK_HERE")) {

				playerJoinChannel = jda.getTextChannelById(playerJoinChannelID);

			}

			if (playerLeaveChannelID != null && options.isPlayerLeaveEnabled() && !playerLeaveChannelID.equals("LINK_HERE")) {

				playerLeaveChannel = jda.getTextChannelById(playerLeaveChannelID);

			}

			if (playerKickChannelID != null && options.isPlayerKickEnabled() && !playerKickChannelID.equals("LINK_HERE")) {

				playerKickChannel = jda.getTextChannelById(playerKickChannelID);

			}

			if (playerDeathChannelID != null && options.isPlayerDeathEnabled() && !playerDeathChannelID.equals("LINK_HERE")) {

				playerDeathChannel = jda.getTextChannelById(playerDeathChannelID);

			}

			if (playerTeleportChannelID != null && options.isPlayerTeleportEnabled() && !playerTeleportChannelID.equals("LINK_HERE")) {

				playerTeleportChannel = jda.getTextChannelById(playerTeleportChannelID);

			}

			if (playerLevelChannelID != null && options.isPlayerLevelEnabled() && !playerLevelChannelID.equals("LINK_HERE")) {

				playerLevelChannel = jda.getTextChannelById(playerLevelChannelID);

			}

			if (blockPlaceChannelID != null && options.isBlockPlaceEnabled() && !blockPlaceChannelID.equals("LINK_HERE")) {

				blockPlaceChannel = jda.getTextChannelById(blockPlaceChannelID);

			}

			if (blockBreakChannelID != null && options.isBlockBreakEnabled() && !blockBreakChannelID.equals("LINK_HERE")) {

				blockBreakChannel = jda.getTextChannelById(blockBreakChannelID);

			}

			if (portalCreationChannelID != null && options.isPortalCreateEnabled() && !portalCreationChannelID.equals("LINK_HERE")) {

				portalCreationChannel = jda.getTextChannelById(portalCreationChannelID);

			}

			if (bucketPlaceChannelID != null && options.isBucketPlaceEnabled() && !bucketPlaceChannelID.equals("LINK_HERE")) {

				bucketPlaceChannel = jda.getTextChannelById(bucketPlaceChannelID);

			}

			if (anvilChannelID != null && options.isAnvilUseEnabled() && !anvilChannelID.equals("LINK_HERE")) {

				anvilChannel = jda.getTextChannelById(anvilChannelID);

			}

			if (TPSChannelID != null && options.isTpsEnabled() && !TPSChannelID.equals("LINK_HERE")) {

				TPSChannel = jda.getTextChannelById(TPSChannelID);

			}

			if (RAMChannelID != null && options.isRamEnabled() && !RAMChannelID.equals("LINK_HERE")) {

				RAMChannel = jda.getTextChannelById(RAMChannelID);

			}

			if (serverStartChannelID != null && options.isServerStartEnabled() && !serverStartChannelID.equals("LINK_HERE")) {

				serverStartChannel = jda.getTextChannelById(serverStartChannelID);

			}

			if (serverStopChannelID != null && options.isServerStopEnabled() && !serverStopChannelID.equals("LINK_HERE")) {

				serverStopChannel = jda.getTextChannelById(serverStopChannelID);

			}

			if (itemDropChannelID != null && options.isItemDropEnabled() && !itemDropChannelID.equals("LINK_HERE")) {

				itemDropChannel = jda.getTextChannelById(itemDropChannelID);

			}

			if (enchantingChannelID != null && options.isEnchantEnabled() && !enchantingChannelID.equals("LINK_HERE")) {

				enchantingChannel = jda.getTextChannelById(enchantingChannelID);

			}

			if (bookEditingChannelID != null && options.isBookEditEnabled() && !bookEditingChannelID.equals("LINK_HERE")) {

				bookEditingChannel = jda.getTextChannelById(bookEditingChannelID);

			}

			if (afkChannelID != null && options.isAfkEnabled() && !afkChannelID.equals("LINK_HERE")) {

				afkChannel = jda.getTextChannelById(afkChannelID);

			}

			if (itemPickupChannelID != null && options.isItemPickupEnabled() && !itemPickupChannelID.equals("LINK_HERE")) {

				itemPickupChannel = jda.getTextChannelById(itemPickupChannelID);

			}

			if (furnaceChannelID != null && options.isFurnaceEnabled() && !furnaceChannelID.equals("LINK_HERE")) {

				furnaceChannel = jda.getTextChannelById(furnaceChannelID);

			}
		}
	}

	public void sendStaffChat(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, staffChannel);

	}

	public void sendPlayerChat(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerChatChannel);
	}

	public void sendPlayerCommand(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerCommandsChannel);
	}

	public void sendConsole(String content, boolean contentInAuthorLine) {

		if (consoleChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		consoleChannel.sendMessage(builder.build()).queue();
	}

	public void sendPlayerSignText(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerSignTextChannel);
	}

	public void sendPlayerJoin(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerJoinChannel);
	}

	public void sendPlayerLeave(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerLeaveChannel);
	}

	public void sendPlayerKick(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerKickChannel);
	}

	public void sendPlayerDeath(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerDeathChannel);
	}

	public void sendPlayerTeleport(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerTeleportChannel);
	}

	public void sendPlayerLevel(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, playerLevelChannel);
	}

	public void sendBlockPlace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, blockPlaceChannel);
	}

	public void sendBlockBreak(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, blockBreakChannel);
	}

	public void sendPortalCreation(String content, boolean contentInAuthorLine) {

		if (portalCreationChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		portalCreationChannel.sendMessage(builder.build()).queue();
	}

	public void sendBucketPlace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, bucketPlaceChannel);
	}

	public void sendAnvil(String playerName, UUID playerUUID,String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, anvilChannel);
	}

	public void sendTPS(String content, boolean contentInAuthorLine) {

		if (TPSChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("TPS");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		TPSChannel.sendMessage(builder.build()).queue();
	}

	public void sendRAM(String content, boolean contentInAuthorLine) {

		if (RAMChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		RAMChannel.sendMessage(builder.build()).queue();
	}

	public void sendServerStart(String content, boolean contentInAuthorLine) {

		if (serverStartChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		serverStartChannel.sendMessage(builder.build()).queue();
	}

	public void sendServerStop(String content, boolean contentInAuthorLine) {

		if (serverStopChannel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		serverStopChannel.sendMessage(builder.build()).queue();
	}

	public void sendItemDrop(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, itemDropChannel);
	}

	public void sendEnchanting(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, enchantingChannel);
	}

	public void sendBookEditing(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, bookEditingChannel);
	}

	public void sendAfk(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, afkChannel);
	}

	public void sendItemPickup(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, itemPickupChannel);
	}

	public void sendFurnace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {

		discordUtil(playerName,playerUUID, content, contentInAuthorLine, furnaceChannel);
	}

	private void discordUtil(String playerName, UUID uuid, String content, boolean contentInAuthorLine, TextChannel channel) {
		if (channel == null)
			return;

		EmbedBuilder builder = new EmbedBuilder().setAuthor(contentInAuthorLine ? content : playerName,
				null, "https://crafatar.com/avatars/" + uuid + "?overlay=1");

		if (!contentInAuthorLine)
			builder.setDescription(content);

		channel.sendMessage(builder.build()).queue();
	}

	public void disconnect() {

		if (jda != null) {
			try {

				jda.shutdown();
				jda = null;

				logger.info("Discord Bot Bridge has been closed!");
				
			} catch (Exception e) {
				
				logger.severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely. If this Issue Persists, Contact the Authors!");
				

				e.printStackTrace();

			}
		}
	}
}
