package org.carour.loggercore.discord;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;

@Getter
public class Discord {

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

    public Discord(DiscordOptions options) {
        this.options = options;
    }

    public void run() {

        if (DiscordFile.get().getBoolean("Discord.Enable")) {

            String botToken = DiscordFile.get().getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();

            } catch (InterruptedException | LoginException e) {

                Bukkit.getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            String staffChannelID = DiscordFile.get().getString("Discord.Staff.Channel-ID");

            String playerChatChannelID = DiscordFile.get().getString("Discord.Player-Chat.Channel-ID");

            String playerCommandsChannelID = DiscordFile.get().getString("Discord.Player-Commands.Channel-ID");

            String consoleChannelID = DiscordFile.get().getString("Discord.Console-Commands.Channel-ID");

            String playerSignTextChannelID = DiscordFile.get().getString("Discord.Player-Sign-Text.Channel-ID");

            String playerJoinChannelID = DiscordFile.get().getString("Discord.Player-Join.Channel-ID");

            String playerLeaveChannelID = DiscordFile.get().getString("Discord.Player-Leave.Channel-ID");

            String playerKickChannelID = DiscordFile.get().getString("Discord.Player-Kick.Channel-ID");

            String playerDeathChannelID = DiscordFile.get().getString("Discord.Player-Death.Channel-ID");

            String playerTeleportChannelID = DiscordFile.get().getString("Discord.Player-Teleport.Channel-ID");

            String playerLevelChannelID = DiscordFile.get().getString("Discord.Player-Level.Channel-ID");

            String blockPlaceChannelID = DiscordFile.get().getString("Discord.Block-Place.Channel-ID");

            String blockBreakChannelID = DiscordFile.get().getString("Discord.Block-Break.Channel-ID");

            String portalCreationChannelID = DiscordFile.get().getString("Discord.Portal-Creation.Channel-ID");

            String bucketPlaceChannelID = DiscordFile.get().getString("Discord.Bucket-Place.Channel-ID");

            String anvilChannelID = DiscordFile.get().getString("Discord.Anvil.Channel-ID");

            String TPSChannelID = DiscordFile.get().getString("Discord.TPS.Channel-ID");

            String RAMChannelID = DiscordFile.get().getString("Discord.RAM.Channel-ID");

            String serverStartChannelID = DiscordFile.get().getString("Discord.Server-Start.Channel-ID");

            String serverStopChannelID = DiscordFile.get().getString("Discord.Server-Stop.Channel-ID");

            String itemDropChannelID = DiscordFile.get().getString("Discord.Item-Drop.Channel-ID");

            String enchantingChannelID = DiscordFile.get().getString("Discord.Enchanting.Channel-ID");

            String bookEditingChannelID = DiscordFile.get().getString("Discord.Book-Editing.Channel-ID");

            String afkChannelID = DiscordFile.get().getString("Discord.AFK.Channel-ID");

            String itemPickupChannelID = DiscordFile.get().getString("Discord.Item-Pickup.Channel-ID");

            String furnaceChannelID = DiscordFile.get().getString("Discord.Furnace.Channel-ID");


            if (staffChannelID != null && options.isStaffEnabled() && !staffChannelID.equals("LINK_HERE")) {

                staffChannel = jda.getTextChannelById(staffChannelID);

            }

            if (playerChatChannelID != null && options.isPlayerChatEnabled() && !playerChatChannelID.equals("LINK_HERE")) {

                playerChatChannel = jda.getTextChannelById(playerChatChannelID);

            }

            if (playerCommandsChannelID != null && options.isPlayerCommandsEnabled() && !playerCommandsChannelID.equals("LINK_HERE")) {

                playerCommandsChannel = jda.getTextChannelById(playerCommandsChannelID);

            }

            if (consoleChannelID != null && options.isConsoleCommandsEnabled() && !consoleChannelID.equals("LINK_HERE")) {

                consoleChannel = jda.getTextChannelById(consoleChannelID);

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

    public void staffChat(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, staffChannel);

    }

    public void sendPlayerChat(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerChatChannel);
    }

    public void sendPlayerCommand(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerCommandsChannel);
    }

    public void sendConsole(String content, boolean contentinAuthorLine) {

        if (consoleChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentinAuthorLine) builder.setDescription(content);

        consoleChannel.sendMessage(builder.build()).queue();
    }

    public void sendPlayerSignText(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerSignTextChannel);
    }

    public void sendPlayerJoin(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerJoinChannel);
    }

    public void sendPlayerLeave(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLeaveChannel);
    }

    public void sendPlayerKick(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerKickChannel);
    }

    public void sendPlayerDeath(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerDeathChannel);
    }

    public void sendPlayerTeleport(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerTeleportChannel);
    }

    public void sendPlayerLevel(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLevelChannel);
    }

    public void sendBlockPlace(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, blockPlaceChannel);
    }

    public void sendBlockBreak(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, blockBreakChannel);
    }

    public void sendPortalCreation(String content, boolean contentinAuthorLine) {

        if (portalCreationChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

        if (!contentinAuthorLine) builder.setDescription(content);

        portalCreationChannel.sendMessage(builder.build()).queue();
    }

    public void sendBucketPlace(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, bucketPlaceChannel);
    }

    public void sendAnvil(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, anvilChannel);
    }

    public void sendTPS(String content, boolean contentinAuthorLine) {

        if (TPSChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("TPS");

        if (!contentinAuthorLine) builder.setDescription(content);

        TPSChannel.sendMessage(builder.build()).queue();
    }

    public void sendRAM(String content, boolean contentinAuthorLine) {

        if (RAMChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentinAuthorLine) builder.setDescription(content);

        RAMChannel.sendMessage(builder.build()).queue();
    }

    public void sendServerStart(String content, boolean contentinAuthorLine) {

        if (serverStartChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStartChannel.sendMessage(builder.build()).queue();
    }

    public void sendServerStop(String content, boolean contentinAuthorLine) {

        if (serverStopChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStopChannel.sendMessage(builder.build()).queue();
    }

    public void sendItemDrop(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, itemDropChannel);
    }

    public void sendEnchanting(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, enchantingChannel);
    }

    public void sendBookEditing(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, bookEditingChannel);
    }

    public void sendAfk(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, afkChannel);
    }

    public void sendItemPickup(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, itemPickupChannel);
    }

    public void sendFurnace(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, furnaceChannel);
    }

    private void discordUtil(Player player, String content, boolean contentinAuthorLine, TextChannel channel) {
        if (channel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(contentinAuthorLine ? content : player.getName(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentinAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (jda != null) {
            try {

                jda.shutdown();
                jda = null;
                Bukkit.getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Bukkit.getServer().getLogger().warning("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        "If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }
}
