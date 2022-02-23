package com.carpour.logger.Discord;

import com.carpour.logger.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

import static com.carpour.logger.Utils.Data.isStaffEnabled;

public class Discord {

    private final Main main = Main.getInstance();

    private static JDA jda;

    private static TextChannel staffChannel;
    private static TextChannel playerChatChannel;
    private static TextChannel playerCommandsChannel;
    private static TextChannel playerSignTextChannel;
    private static TextChannel playerJoinChannel;
    private static TextChannel playerLeaveChannel;
    private static TextChannel playerKickChannel;
    private static TextChannel playerDeathChannel;
    private static TextChannel playerTeleportChannel;
    private static TextChannel playerLevelChannel;
    private static TextChannel blockPlaceChannel;
    private static TextChannel blockBreakChannel;
    private static TextChannel bucketFillChannel;
    private static TextChannel bucketEmptyChannel;
    private static TextChannel anvilChannel;
    private static TextChannel itemDropChannel;
    private static TextChannel enchantingChannel;
    private static TextChannel bookEditingChannel;
    private static TextChannel itemPickupChannel;
    private static TextChannel furnaceChannel;
    private static TextChannel gameModeChannel;
    private static TextChannel craftChannel;

    private static TextChannel serverStartChannel;
    private static TextChannel serverStopChannel;
    private static TextChannel consoleChannel;
    private static TextChannel RAMChannel;
    private static TextChannel TPSChannel;
    private static TextChannel portalCreationChannel;
    private static TextChannel rconChannel;

    private static TextChannel afkChannel;
    private static TextChannel wrongPasswordChannel;
    private static TextChannel vaultChannel;

    public void run() {

        if (DiscordFile.get().getBoolean("Discord.Enable")) {

            final String botToken = DiscordFile.get().getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();

                new DiscordStatus();

            } catch (Exception e) {

                this.main.getLogger().severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            // Player Side Part
            final String staffChannelID = DiscordFile.get().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = DiscordFile.get().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = DiscordFile.get().getString("Discord.Player-Commands.Channel-ID");

            final String playerSignTextChannelID = DiscordFile.get().getString("Discord.Player-Sign-Text.Channel-ID");

            final String playerJoinChannelID = DiscordFile.get().getString("Discord.Player-Join.Channel-ID");

            final String playerLeaveChannelID = DiscordFile.get().getString("Discord.Player-Leave.Channel-ID");

            final String playerKickChannelID = DiscordFile.get().getString("Discord.Player-Kick.Channel-ID");

            final String playerDeathChannelID = DiscordFile.get().getString("Discord.Player-Death.Channel-ID");

            final String playerTeleportChannelID = DiscordFile.get().getString("Discord.Player-Teleport.Channel-ID");

            final String playerLevelChannelID = DiscordFile.get().getString("Discord.Player-Level.Channel-ID");

            final String blockPlaceChannelID = DiscordFile.get().getString("Discord.Block-Place.Channel-ID");

            final String blockBreakChannelID = DiscordFile.get().getString("Discord.Block-Break.Channel-ID");

            final String bucketFillChannelID = DiscordFile.get().getString("Discord.Bucket-Fill.Channel-ID");

            final String bucketEmptyChannelID = DiscordFile.get().getString("Discord.Bucket-Empty.Channel-ID");

            final String anvilChannelID = DiscordFile.get().getString("Discord.Anvil.Channel-ID");

            final String itemDropChannelID = DiscordFile.get().getString("Discord.Item-Drop.Channel-ID");

            final String enchantingChannelID = DiscordFile.get().getString("Discord.Enchanting.Channel-ID");

            final String bookEditingChannelID = DiscordFile.get().getString("Discord.Book-Editing.Channel-ID");

            final String itemPickupChannelID = DiscordFile.get().getString("Discord.Item-Pickup.Channel-ID");

            final String furnaceChannelID = DiscordFile.get().getString("Discord.Furnace.Channel-ID");

            final String gameModeChannelID = DiscordFile.get().getString("Discord.Game-Mode.Channel-ID");

            final String craftChannelID = DiscordFile.get().getString("Discord.Craft.Channel-ID");

            // Server Side Part
            final String serverStartChannelID = DiscordFile.get().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = DiscordFile.get().getString("Discord.Server-Side.Stop.Channel-ID");

            final String consoleChannelID = DiscordFile.get().getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String RAMChannelID = DiscordFile.get().getString("Discord.Server-Side.RAM.Channel-ID");

            final String TPSChannelID = DiscordFile.get().getString("Discord.Server-Side.TPS.Channel-ID");

            final String portalCreationChannelID = DiscordFile.get().getString("Discord.Server-Side.Portal-Creation.Channel-ID");

            final String rconChannelID = DiscordFile.get().getString("Discord.Server-Side.RCON.Channel-ID");

            // Extras
            final String afkChannelID = DiscordFile.get().getString("Discord.Extras.AFK.Channel-ID");

            final String wrongPasswordChannelID = DiscordFile.get().getString("Discord.Extras.Wrong-Password.Channel-ID");

            final String vaultChannelID = DiscordFile.get().getString("Discord.Extras.Vault.Channel-ID");


            try {

                assert false;

                // Player Side Part
                if (!(staffChannelID.isEmpty()) && isStaffEnabled && !staffChannelID.equals("LINK_HERE")) {

                    staffChannel = jda.getTextChannelById(staffChannelID);

                }

                if (!(playerChatChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Chat") && !playerChatChannelID.equals("LINK_HERE")) {

                    playerChatChannel = jda.getTextChannelById(playerChatChannelID);

                }

                if (!(playerCommandsChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Commands") && !playerCommandsChannelID.equals("LINK_HERE")) {

                    playerCommandsChannel = jda.getTextChannelById(playerCommandsChannelID);

                }

                if (!(playerSignTextChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Sign-Text") && !playerSignTextChannelID.equals("LINK_HERE")) {

                    playerSignTextChannel = jda.getTextChannelById(playerSignTextChannelID);

                }

                if (!(playerJoinChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Join") && !playerJoinChannelID.equals("LINK_HERE")) {

                    playerJoinChannel = jda.getTextChannelById(playerJoinChannelID);

                }

                if (!(playerLeaveChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Leave") && !playerLeaveChannelID.equals("LINK_HERE")) {

                    playerLeaveChannel = jda.getTextChannelById(playerLeaveChannelID);

                }

                if (!(playerKickChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Kick") && !playerKickChannelID.equals("LINK_HERE")) {

                    playerKickChannel = jda.getTextChannelById(playerKickChannelID);

                }

                if (!(playerDeathChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Death") && !playerDeathChannelID.equals("LINK_HERE")) {

                    playerDeathChannel = jda.getTextChannelById(playerDeathChannelID);

                }

                if (!(playerTeleportChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Teleport") && !playerTeleportChannelID.equals("LINK_HERE")) {

                    playerTeleportChannel = jda.getTextChannelById(playerTeleportChannelID);

                }

                if (!(playerLevelChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Level") && !playerLevelChannelID.equals("LINK_HERE")) {

                    playerLevelChannel = jda.getTextChannelById(playerLevelChannelID);

                }

                if (!(blockPlaceChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Block-Place") && !blockPlaceChannelID.equals("LINK_HERE")) {

                    blockPlaceChannel = jda.getTextChannelById(blockPlaceChannelID);

                }

                if (!(blockBreakChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Block-Break") && !blockBreakChannelID.equals("LINK_HERE")) {

                    blockBreakChannel = jda.getTextChannelById(blockBreakChannelID);

                }

                if (!(bucketFillChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Bucket-Fill") && !bucketFillChannelID.equals("LINK_HERE")) {

                    bucketFillChannel = jda.getTextChannelById(bucketFillChannelID);

                }

                if (!(bucketEmptyChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Bucket-Empty") && !bucketEmptyChannelID.equals("LINK_HERE")) {

                    bucketEmptyChannel = jda.getTextChannelById(bucketEmptyChannelID);

                }

                if (!(anvilChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Anvil") && !anvilChannelID.equals("LINK_HERE")) {

                    anvilChannel = jda.getTextChannelById(anvilChannelID);

                }

                if (!(itemDropChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Item-Drop") && !itemDropChannelID.equals("LINK_HERE")) {

                    itemDropChannel = jda.getTextChannelById(itemDropChannelID);

                }

                if (!(enchantingChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Enchanting") && !enchantingChannelID.equals("LINK_HERE")) {

                    enchantingChannel = jda.getTextChannelById(enchantingChannelID);

                }

                if (!(bookEditingChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Book-Editing") && !bookEditingChannelID.equals("LINK_HERE")) {

                    bookEditingChannel = jda.getTextChannelById(bookEditingChannelID);

                }

                if (!(itemPickupChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Item-Pickup") && !itemPickupChannelID.equals("LINK_HERE")) {

                    itemPickupChannel = jda.getTextChannelById(itemPickupChannelID);

                }

                if (!(furnaceChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Furnace") && !furnaceChannelID.equals("LINK_HERE")) {

                    furnaceChannel = jda.getTextChannelById(furnaceChannelID);

                }

                if (!(gameModeChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Game-Mode") && !gameModeChannelID.equals("LINK_HERE")) {

                    gameModeChannel = jda.getTextChannelById(gameModeChannelID);

                }

                if (!(craftChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Craft") && !craftChannelID.equals("LINK_HERE")) {

                    craftChannel = jda.getTextChannelById(craftChannelID);

                }

                // Server Side Part
                if (!(serverStartChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Start") && !serverStartChannelID.equals("LINK_HERE")) {

                    serverStartChannel = jda.getTextChannelById(serverStartChannelID);

                }

                if (!(serverStopChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Stop") && !serverStopChannelID.equals("LINK_HERE")) {

                    serverStopChannel = jda.getTextChannelById(serverStopChannelID);

                }

                if (!(consoleChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Console-Commands") && !consoleChannelID.equals("LINK_HERE")) {

                    consoleChannel = jda.getTextChannelById(consoleChannelID);

                }

                if (!(RAMChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.RAM") && !RAMChannelID.equals("LINK_HERE")) {

                    RAMChannel = jda.getTextChannelById(RAMChannelID);

                }

                if (!(TPSChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.TPS") && !TPSChannelID.equals("LINK_HERE")) {

                    TPSChannel = jda.getTextChannelById(TPSChannelID);

                }

                if (!(portalCreationChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Portal-Creation") && !portalCreationChannelID.equals("LINK_HERE")) {

                    portalCreationChannel = jda.getTextChannelById(portalCreationChannelID);

                }

                if (!(rconChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.RCON") && !rconChannelID.equals("LINK_HERE")) {

                    rconChannel = jda.getTextChannelById(rconChannelID);

                }

                // Extra Checkers Part
                if (!(afkChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.AFK") && !afkChannelID.equals("LINK_HERE")) {

                    afkChannel = jda.getTextChannelById(afkChannelID);

                }

                if (!(wrongPasswordChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.Essentials-AFK") && !wrongPasswordChannelID.equals("LINK_HERE")) {

                    wrongPasswordChannel = jda.getTextChannelById(wrongPasswordChannelID);

                }

                if (!(vaultChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.Vault") && !vaultChannelID.equals("LINK_HERE")) {

                    vaultChannel = jda.getTextChannelById(vaultChannelID);

                }
            }catch (Exception e){

                this.main.getLogger().severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    public static void staffChat(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, staffChannel);

    }

    public static void playerChat(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerChatChannel);
    }

    public static void playerCommand(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerCommandsChannel);
    }

    public static void console(String content, boolean contentinAuthorLine) {

        if (consoleChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentinAuthorLine) builder.setDescription(content);

        consoleChannel.sendMessage(builder.build()).queue();
    }

    public static void playerSignText(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerSignTextChannel);
    }

    public static void playerJoin(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerJoinChannel);
    }

    public static void playerLeave(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLeaveChannel);
    }

    public static void playerKick(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerKickChannel);
    }

    public static void playerDeath(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerDeathChannel);
    }

    public static void playerTeleport(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerTeleportChannel);
    }

    public static void playerLevel(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLevelChannel);
    }

    public static void blockPlace(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, blockPlaceChannel);
    }

    public static void blockBreak(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, blockBreakChannel);
    }

    public static void portalCreation(String content, boolean contentinAuthorLine) {

        if (portalCreationChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

        if (!contentinAuthorLine) builder.setDescription(content);

        portalCreationChannel.sendMessage(builder.build()).queue();
    }

    public static void bucketFill(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, bucketFillChannel);
    }

    public static void bucketEmpty(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, bucketEmptyChannel);
    }

    public static void anvil(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, anvilChannel);
    }

    public static void TPS(String content, boolean contentinAuthorLine) {

        if (TPSChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("TPS");

        if (!contentinAuthorLine) builder.setDescription(content);

        TPSChannel.sendMessage(builder.build()).queue();
    }

    public static void RAM(String content, boolean contentinAuthorLine) {

        if (RAMChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentinAuthorLine) builder.setDescription(content);

        RAMChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStart(String content, boolean contentinAuthorLine) {

        if (serverStartChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStartChannel.sendMessage(builder.build()).queue();
    }

    public static void rcon(String content, boolean contentinAuthorLine) {

        if (rconChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RCON");

        if (!contentinAuthorLine) builder.setDescription(content);

        rconChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStop(String content, boolean contentinAuthorLine) {

        if (serverStopChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStopChannel.sendMessage(builder.build()).queue();
    }

    public static void itemDrop(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, itemDropChannel);
    }

    public static void enchanting(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, enchantingChannel);
    }

    public static void bookEditing(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, bookEditingChannel);
    }

    public static void afk(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, afkChannel);
    }

    public static void wrongPassword(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, wrongPasswordChannel);
    }

    public static void itemPickup(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, itemPickupChannel);
    }

    public static void furnace(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, furnaceChannel);
    }

    public static void gameMode(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, gameModeChannel);
    }

    public static void playerCraft(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, craftChannel);
    }

    public static void vault(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, vaultChannel);
    }

    private static void discordUtil(Player player, String content, boolean contentinAuthorLine, TextChannel channel) {
        if (channel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(contentinAuthorLine ? content : player.getName(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentinAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (jda != null) {
            try {

                jda.shutdown();
                jda = null;
                DiscordStatus.getThreadPool().shutdown();
                this.main.getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                this.main.getLogger().severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }

    public JDA getJda(){ return jda; }
}
