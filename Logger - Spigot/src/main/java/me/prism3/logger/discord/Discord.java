package me.prism3.logger.discord;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

public class Discord {

    private final Main main = Main.getInstance();

    private JDA jda;

    private TextChannel staffChannel;
    private TextChannel playerChatChannel;
    private TextChannel playerCommandsChannel;
    private TextChannel playerSignTextChannel;
    private TextChannel playerJoinChannel;
    private TextChannel playerLeaveChannel;
    private TextChannel playerKickChannel;
    private TextChannel playerDeathChannel;
    private TextChannel playerTeleportChannel;
    private TextChannel playerLevelChannel;
    private TextChannel blockPlaceChannel;
    private TextChannel blockBreakChannel;
    private TextChannel bucketFillChannel;
    private TextChannel bucketEmptyChannel;
    private TextChannel anvilChannel;
    private TextChannel itemDropChannel;
    private TextChannel enchantingChannel;
    private TextChannel bookEditingChannel;
    private TextChannel itemPickupChannel;
    private TextChannel furnaceChannel;
    private TextChannel gameModeChannel;
    private TextChannel craftChannel;
    private TextChannel registrationChannel;
    private TextChannel primedTNTChannel;
    private TextChannel spawnEggChannel;

    private TextChannel serverStartChannel;
    private TextChannel serverStopChannel;
    private TextChannel consoleChannel;
    private TextChannel ramChannel;
    private TextChannel tpsChannel;
    private TextChannel portalCreationChannel;
    private TextChannel rConChannel;
    private TextChannel commandBlockChannel;

    private TextChannel afkChannel;
    private TextChannel wrongPasswordChannel;
    private TextChannel vaultChannel;
    private TextChannel liteBansChannel;
    private TextChannel advancedBanChannel;

    private TextChannel woodStrippingChannel;

    public void run() {

        if (DiscordFile.get().getBoolean("Discord.Enable")) {

            final String botToken = DiscordFile.get().getString("Discord.Bot-Token");

            try {

                this.jda = JDABuilder.createDefault(botToken).build().awaitReady();

                if (DiscordFile.get().getBoolean("ActivityCycling.Enabled")) new DiscordStatus();

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

            final String registrationChannelID = DiscordFile.get().getString("Discord.Registration.Channel-ID");

            final String primedTNTChannelID = DiscordFile.get().getString("Discord.Primed-TNT.Channel-ID");

            final String spawnEggChannelID = DiscordFile.get().getString("Discord.Spawn-Egg.Channel-ID");

            // Server Side Part
            final String serverStartChannelID = DiscordFile.get().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = DiscordFile.get().getString("Discord.Server-Side.Stop.Channel-ID");

            final String consoleChannelID = DiscordFile.get().getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String RAMChannelID = DiscordFile.get().getString("Discord.Server-Side.RAM.Channel-ID");

            final String TPSChannelID = DiscordFile.get().getString("Discord.Server-Side.TPS.Channel-ID");

            final String portalCreationChannelID = DiscordFile.get().getString("Discord.Server-Side.Portal-Creation.Channel-ID");

            final String rconChannelID = DiscordFile.get().getString("Discord.Server-Side.RCON.Channel-ID");

            final String commandBlockChannelID = DiscordFile.get().getString("Discord.Server-Side.Command-Block.Channel-ID");

            // Extras
            final String afkChannelID = DiscordFile.get().getString("Discord.Extras.AFK.Channel-ID");

            final String wrongPasswordChannelID = DiscordFile.get().getString("Discord.Extras.Wrong-Password.Channel-ID");

            final String vaultChannelID = DiscordFile.get().getString("Discord.Extras.Vault.Channel-ID");

            final String liteBansChannelID = DiscordFile.get().getString("Discord.Extras.LiteBans.Channel-ID");

            final String advancedBanChannelID = DiscordFile.get().getString("Discord.Extras.AdvancedBan.Channel-ID");

            // Version Exception
            final String woodStrippingChannelID = DiscordFile.get().getString("Discord.Version-Exceptions.Wood-Stripping.Channel-ID");


            try {

                // Player Side Part
                if (!(staffChannelID.isEmpty()) && Data.isStaffEnabled && !staffChannelID.equals("LINK_HERE")) {

                    staffChannel = this.jda.getTextChannelById(staffChannelID);

                }

                if (!(playerChatChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Chat") && !playerChatChannelID.equals("LINK_HERE")) {

                    playerChatChannel = this.jda.getTextChannelById(playerChatChannelID);

                }

                if (!(playerCommandsChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Commands") && !playerCommandsChannelID.equals("LINK_HERE")) {

                    playerCommandsChannel = this.jda.getTextChannelById(playerCommandsChannelID);

                }

                if (!(playerSignTextChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Sign-Text") && !playerSignTextChannelID.equals("LINK_HERE")) {

                    playerSignTextChannel = this.jda.getTextChannelById(playerSignTextChannelID);

                }

                if (!(playerJoinChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Join") && !playerJoinChannelID.equals("LINK_HERE")) {

                    playerJoinChannel = this.jda.getTextChannelById(playerJoinChannelID);

                }

                if (!(playerLeaveChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Leave") && !playerLeaveChannelID.equals("LINK_HERE")) {

                    playerLeaveChannel = this.jda.getTextChannelById(playerLeaveChannelID);

                }

                if (!(playerKickChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Kick") && !playerKickChannelID.equals("LINK_HERE")) {

                    playerKickChannel = this.jda.getTextChannelById(playerKickChannelID);

                }

                if (!(playerDeathChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Death") && !playerDeathChannelID.equals("LINK_HERE")) {

                    playerDeathChannel = this.jda.getTextChannelById(playerDeathChannelID);

                }

                if (!(playerTeleportChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Teleport") && !playerTeleportChannelID.equals("LINK_HERE")) {

                    playerTeleportChannel = this.jda.getTextChannelById(playerTeleportChannelID);

                }

                if (!(playerLevelChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Level") && !playerLevelChannelID.equals("LINK_HERE")) {

                    playerLevelChannel = this.jda.getTextChannelById(playerLevelChannelID);

                }

                if (!(blockPlaceChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Block-Place") && !blockPlaceChannelID.equals("LINK_HERE")) {

                    blockPlaceChannel = this.jda.getTextChannelById(blockPlaceChannelID);

                }

                if (!(blockBreakChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Block-Break") && !blockBreakChannelID.equals("LINK_HERE")) {

                    blockBreakChannel = this.jda.getTextChannelById(blockBreakChannelID);

                }

                if (!(bucketFillChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Bucket-Fill") && !bucketFillChannelID.equals("LINK_HERE")) {

                    bucketFillChannel = this.jda.getTextChannelById(bucketFillChannelID);

                }

                if (!(bucketEmptyChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Bucket-Empty") && !bucketEmptyChannelID.equals("LINK_HERE")) {

                    bucketEmptyChannel = this.jda.getTextChannelById(bucketEmptyChannelID);

                }

                if (!(anvilChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Anvil") && !anvilChannelID.equals("LINK_HERE")) {

                    anvilChannel = this.jda.getTextChannelById(anvilChannelID);

                }

                if (!(itemDropChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Item-Drop") && !itemDropChannelID.equals("LINK_HERE")) {

                    itemDropChannel = this.jda.getTextChannelById(itemDropChannelID);

                }

                if (!(enchantingChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Enchanting") && !enchantingChannelID.equals("LINK_HERE")) {

                    enchantingChannel = this.jda.getTextChannelById(enchantingChannelID);

                }

                if (!(bookEditingChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Book-Editing") && !bookEditingChannelID.equals("LINK_HERE")) {

                    bookEditingChannel = this.jda.getTextChannelById(bookEditingChannelID);

                }

                if (!(itemPickupChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Item-Pickup") && !itemPickupChannelID.equals("LINK_HERE")) {

                    itemPickupChannel = this.jda.getTextChannelById(itemPickupChannelID);

                }

                if (!(furnaceChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Furnace") && !furnaceChannelID.equals("LINK_HERE")) {

                    furnaceChannel = this.jda.getTextChannelById(furnaceChannelID);

                }

                if (!(gameModeChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Game-Mode") && !gameModeChannelID.equals("LINK_HERE")) {

                    gameModeChannel = this.jda.getTextChannelById(gameModeChannelID);

                }

                if (!(craftChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Craft") && !craftChannelID.equals("LINK_HERE")) {

                    craftChannel = this.jda.getTextChannelById(craftChannelID);

                }

                if (!(registrationChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Registration") && !registrationChannelID.equals("LINK_HERE")) {

                    registrationChannel = this.jda.getTextChannelById(registrationChannelID);

                }

                if (!(primedTNTChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Primed-TNT") && !primedTNTChannelID.equals("LINK_HERE")) {

                    primedTNTChannel = this.jda.getTextChannelById(primedTNTChannelID);

                }

                if (!(spawnEggChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Spawn-Egg") && !spawnEggChannelID.equals("LINK_HERE")) {

                    spawnEggChannel = this.jda.getTextChannelById(spawnEggChannelID);

                }

                // Server Side Part
                if (!(serverStartChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Start") && !serverStartChannelID.equals("LINK_HERE")) {

                    serverStartChannel = this.jda.getTextChannelById(serverStartChannelID);

                }

                if (!(serverStopChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Stop") && !serverStopChannelID.equals("LINK_HERE")) {

                    serverStopChannel = this.jda.getTextChannelById(serverStopChannelID);

                }

                if (!(consoleChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Console-Commands") && !consoleChannelID.equals("LINK_HERE")) {

                    consoleChannel = this.jda.getTextChannelById(consoleChannelID);

                }

                if (!(RAMChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.RAM") && !RAMChannelID.equals("LINK_HERE")) {

                    ramChannel = this.jda.getTextChannelById(RAMChannelID);

                }

                if (!(TPSChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.TPS") && !TPSChannelID.equals("LINK_HERE")) {

                    tpsChannel = this.jda.getTextChannelById(TPSChannelID);

                }

                if (!(portalCreationChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Portal-Creation") && !portalCreationChannelID.equals("LINK_HERE")) {

                    portalCreationChannel = this.jda.getTextChannelById(portalCreationChannelID);

                }

                if (!(rconChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.RCON") && !rconChannelID.equals("LINK_HERE")) {

                    rConChannel = this.jda.getTextChannelById(rconChannelID);

                }

                if (!(commandBlockChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Command-Block") && !commandBlockChannelID.equals("LINK_HERE")) {

                    commandBlockChannel = this.jda.getTextChannelById(commandBlockChannelID);

                }

                // Extra Checkers Part
                if (!(afkChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.Essentials-AFK") && !afkChannelID.equals("LINK_HERE")) {

                    afkChannel = this.jda.getTextChannelById(afkChannelID);

                }

                if (!(wrongPasswordChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.AuthMe-Wrong-Password") && !wrongPasswordChannelID.equals("LINK_HERE")) {

                    wrongPasswordChannel = this.jda.getTextChannelById(wrongPasswordChannelID);

                }

                if (!(vaultChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.Vault") && !vaultChannelID.equals("LINK_HERE")) {

                    vaultChannel = this.jda.getTextChannelById(vaultChannelID);

                }

                if (!(liteBansChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.LiteBans") && !liteBansChannelID.equals("LINK_HERE")) {

                    liteBansChannel = this.jda.getTextChannelById(liteBansChannelID);

                }

                if (!(advancedBanChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extras.AdvancedBan") && !advancedBanChannelID.equals("LINK_HERE")) {

                    advancedBanChannel = this.jda.getTextChannelById(advancedBanChannelID);

                }

                // Version Exception Part
                if (!(woodStrippingChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Version-Exceptions.Wood-Stripping") && !woodStrippingChannelID.equals("LINK_HERE")) {

                    woodStrippingChannel = this.jda.getTextChannelById(woodStrippingChannelID);

                }
            }catch (Exception e) {

                this.main.getLogger().severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    public void staffChat(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, staffChannel);

    }

    public void playerChat(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerChatChannel);
    }

    public void playerCommand(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerCommandsChannel);
    }

    public void console(String content, boolean contentInAuthorLine) {

        if (consoleChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentInAuthorLine) builder.setDescription(content);

        consoleChannel.sendMessage(builder.build()).queue();
    }

    public void commandBlock(String content, boolean contentInAuthorLine) {

        if (commandBlockChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Command Block");

        if (!contentInAuthorLine) builder.setDescription(content);

        commandBlockChannel.sendMessage(builder.build()).queue();
    }

    public void playerSignText(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerSignTextChannel);
    }

    public void playerJoin(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerJoinChannel);
    }

    public void playerLeave(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerLeaveChannel);
    }

    public void playerKick(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerKickChannel);
    }

    public void playerDeath(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerDeathChannel);
    }

    public void playerTeleport(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerTeleportChannel);
    }

    public void playerLevel(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, playerLevelChannel);
    }

    public void blockPlace(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, blockPlaceChannel);
    }

    public void blockBreak(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, blockBreakChannel);
    }

    public void portalCreation(String content, boolean contentInAuthorLine) {

        if (portalCreationChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

        if (!contentInAuthorLine) builder.setDescription(content);

        portalCreationChannel.sendMessage(builder.build()).queue();
    }

    public void bucketFill(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, bucketFillChannel);
    }

    public void bucketEmpty(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, bucketEmptyChannel);
    }

    public void anvil(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, anvilChannel);
    }

    public void tps(String content, boolean contentInAuthorLine) {

        if (tpsChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("TPS");

        if (!contentInAuthorLine) builder.setDescription(content);

        tpsChannel.sendMessage(builder.build()).queue();
    }

    public void ram(String content, boolean contentInAuthorLine) {

        if (ramChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentInAuthorLine) builder.setDescription(content);

        ramChannel.sendMessage(builder.build()).queue();
    }

    public void serverStart(String content, boolean contentInAuthorLine) {

        if (serverStartChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentInAuthorLine) builder.setDescription(content);

        serverStartChannel.sendMessage(builder.build()).queue();
    }

    public void rCon(String content, boolean contentInAuthorLine) {

        if (rConChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RCON");

        if (!contentInAuthorLine) builder.setDescription(content);

        rConChannel.sendMessage(builder.build()).queue();
    }

    public void serverStop(String content, boolean contentInAuthorLine) {

        if (serverStopChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentInAuthorLine) builder.setDescription(content);

        serverStopChannel.sendMessage(builder.build()).queue();
    }

    public void itemDrop(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, itemDropChannel);
    }

    public void enchanting(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, enchantingChannel);
    }

    public void bookEditing(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, bookEditingChannel);
    }

    public void afk(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, afkChannel);
    }

    public void wrongPassword(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, wrongPasswordChannel);
    }

    public void itemPickup(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, itemPickupChannel);
    }

    public void furnace(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, furnaceChannel);
    }

    public void gameMode(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, gameModeChannel);
    }

    public void playerCraft(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, craftChannel);
    }

    public void vault(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, vaultChannel);
    }

    public void playerRegistration(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, registrationChannel);
    }

    public void primedTNT(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, primedTNTChannel);
    }

    public void spawnEgg(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, spawnEggChannel);
    }

    public void liteBans(String content, boolean contentInAuthorLine) {

        if (liteBansChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("LiteBans");

        if (!contentInAuthorLine) builder.setDescription(content);

        liteBansChannel.sendMessage(builder.build()).queue();
    }

    public void advancedBan(String content, boolean contentInAuthorLine) {

        if (advancedBanChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("AdvancedBan");

        if (!contentInAuthorLine) builder.setDescription(content);

        advancedBanChannel.sendMessage(builder.build()).queue();
    }

    public void woodStripping(Player player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, woodStrippingChannel);
    }

    private static void discordUtil(Player player, String content, boolean contentInAuthorLine, TextChannel channel) {
        if (channel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(contentInAuthorLine ? content : player.getName(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentInAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (this.jda != null) {
            try {

                this.jda.shutdown();
                this.jda = null;
                if (DiscordFile.get().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                this.main.getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                this.main.getLogger().severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }

    public JDA getJda() { return this.jda; }
}
