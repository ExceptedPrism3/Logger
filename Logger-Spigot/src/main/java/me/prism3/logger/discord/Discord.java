package me.prism3.logger.discord;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
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
    private TextChannel chestInteractionChannel;
    private TextChannel entityDeathChannel;
    private TextChannel signChangeChannel;

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

        if (this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            final String botToken = this.main.getDiscordFile().get().getString("Discord.Bot-Token");

            try {

                this.jda = JDABuilder.createDefault(botToken).build().awaitReady();

                if (this.main.getDiscordFile().get().getBoolean("ActivityCycling.Enabled")) new DiscordStatus(this.jda);

            } catch (Exception e) {

                Log.severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            // Player Side Part
            final String staffChannelID = this.main.getDiscordFile().get().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Commands.Channel-ID");

            final String playerSignTextChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Sign-Text.Channel-ID");

            final String playerJoinChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Join.Channel-ID");

            final String playerLeaveChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Leave.Channel-ID");

            final String playerKickChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Kick.Channel-ID");

            final String playerDeathChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Death.Channel-ID");

            final String playerTeleportChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Teleport.Channel-ID");

            final String playerLevelChannelID = this.main.getDiscordFile().get().getString("Discord.Player-Level.Channel-ID");

            final String blockPlaceChannelID = this.main.getDiscordFile().get().getString("Discord.Block-Place.Channel-ID");

            final String blockBreakChannelID = this.main.getDiscordFile().get().getString("Discord.Block-Break.Channel-ID");

            final String bucketFillChannelID = this.main.getDiscordFile().get().getString("Discord.Bucket-Fill.Channel-ID");

            final String bucketEmptyChannelID = this.main.getDiscordFile().get().getString("Discord.Bucket-Empty.Channel-ID");

            final String anvilChannelID = this.main.getDiscordFile().get().getString("Discord.Anvil.Channel-ID");

            final String itemDropChannelID = this.main.getDiscordFile().get().getString("Discord.Item-Drop.Channel-ID");

            final String enchantingChannelID = this.main.getDiscordFile().get().getString("Discord.Enchanting.Channel-ID");

            final String bookEditingChannelID = this.main.getDiscordFile().get().getString("Discord.Book-Editing.Channel-ID");

            final String itemPickupChannelID = this.main.getDiscordFile().get().getString("Discord.Item-Pickup.Channel-ID");

            final String furnaceChannelID = this.main.getDiscordFile().get().getString("Discord.Furnace.Channel-ID");

            final String gameModeChannelID = this.main.getDiscordFile().get().getString("Discord.Game-Mode.Channel-ID");

            final String craftChannelID = this.main.getDiscordFile().get().getString("Discord.Craft.Channel-ID");

            final String registrationChannelID = this.main.getDiscordFile().get().getString("Discord.Registration.Channel-ID");

            final String primedTNTChannelID = this.main.getDiscordFile().get().getString("Discord.Primed-TNT.Channel-ID");

            final String chestInteractionChannelID = this.main.getDiscordFile().get().getString("Discord.Chest-Interaction.Channel-ID");

            final String entityDeathChannelID = this.main.getDiscordFile().get().getString("Discord.Entity-Death.Channel-ID");

            final String signChangeChannelID = this.main.getDiscordFile().get().getString("Discord.Sign-Change.Channel-ID");

            // Server Side Part
            final String serverStartChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.Stop.Channel-ID");

            final String consoleChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String ramChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.RAM.Channel-ID");

            final String tpsChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.TPS.Channel-ID");

            final String portalCreationChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.Portal-Creation.Channel-ID");

            final String rConChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.RCON.Channel-ID");

            final String commandBlockChannelID = this.main.getDiscordFile().get().getString("Discord.Server-Side.Command-Block.Channel-ID");

            // Extras
            final String afkChannelID = this.main.getDiscordFile().get().getString("Discord.Extras.AFK.Channel-ID");

            final String wrongPasswordChannelID = this.main.getDiscordFile().get().getString("Discord.Extras.Wrong-Password.Channel-ID");

            final String vaultChannelID = this.main.getDiscordFile().get().getString("Discord.Extras.Vault.Channel-ID");

            final String liteBansChannelID = this.main.getDiscordFile().get().getString("Discord.Extras.LiteBans.Channel-ID");

            final String advancedBanChannelID = this.main.getDiscordFile().get().getString("Discord.Extras.AdvancedBan.Channel-ID");

            // Version Exception
            final String woodStrippingChannelID = this.main.getDiscordFile().get().getString("Discord.Version-Exceptions.Wood-Stripping.Channel-ID");


            try {

                // Player Side Part
                if (this.isValid(staffChannelID, "Staff.Enabled")) {

                    this.staffChannel = this.jda.getTextChannelById(staffChannelID);

                }

                if (this.isValid(playerChatChannelID, "Log-Player.Chat")) {

                    this.playerChatChannel = this.jda.getTextChannelById(playerChatChannelID);

                }

                if (this.isValid(playerCommandsChannelID, "Log-Player.Commands")) {

                    this.playerCommandsChannel = this.jda.getTextChannelById(playerCommandsChannelID);

                }

                if (this.isValid(playerSignTextChannelID, "Log-Player.Sign-Text")) {

                    this.playerSignTextChannel = this.jda.getTextChannelById(playerSignTextChannelID);

                }

                if (this.isValid(playerJoinChannelID, "Log-Player.Join")) {

                    this.playerJoinChannel = this.jda.getTextChannelById(playerJoinChannelID);

                }

                if (this.isValid(playerLeaveChannelID, "Log-Player.Leave")) {

                    this.playerLeaveChannel = this.jda.getTextChannelById(playerLeaveChannelID);

                }

                if (this.isValid(playerKickChannelID, "Log-Player.Kick")) {

                    this.playerKickChannel = this.jda.getTextChannelById(playerKickChannelID);

                }

                if (this.isValid(playerDeathChannelID, "Log-Player.Death")) {

                    this.playerDeathChannel = this.jda.getTextChannelById(playerDeathChannelID);

                }

                if (this.isValid(playerTeleportChannelID, "Log-Player.Teleport")) {

                    this.playerTeleportChannel = this.jda.getTextChannelById(playerTeleportChannelID);

                }

                if (this.isValid(playerLevelChannelID, "Log-Player.Level")) {

                    this.playerLevelChannel = this.jda.getTextChannelById(playerLevelChannelID);

                }

                if (this.isValid(blockPlaceChannelID, "Log-Player.Block-Place")) {

                    this.blockPlaceChannel = this.jda.getTextChannelById(blockPlaceChannelID);

                }

                if (this.isValid(blockBreakChannelID, "Log-Player.Block-Break")) {

                    this.blockBreakChannel = this.jda.getTextChannelById(blockBreakChannelID);

                }

                if (this.isValid(bucketFillChannelID, "Log-Player.Bucket-Fill")) {

                    this.bucketFillChannel = this.jda.getTextChannelById(bucketFillChannelID);

                }

                if (this.isValid(bucketEmptyChannelID, "Log-Player.Bucket-Empty")) {

                    this.bucketEmptyChannel = this.jda.getTextChannelById(bucketEmptyChannelID);

                }

                if (this.isValid(anvilChannelID, "Log-Player.Anvil")) {

                    this.anvilChannel = this.jda.getTextChannelById(anvilChannelID);

                }

                if (this.isValid(itemDropChannelID, "Log-Player.Item-Drop")) {

                    this.itemDropChannel = this.jda.getTextChannelById(itemDropChannelID);

                }

                if (this.isValid(enchantingChannelID, "Log-Player.Enchanting")) {

                    this.enchantingChannel = this.jda.getTextChannelById(enchantingChannelID);

                }

                if (this.isValid(bookEditingChannelID, "Log-Player.Book-Editing")) {

                    this.bookEditingChannel = this.jda.getTextChannelById(bookEditingChannelID);

                }

                if (this.isValid(itemPickupChannelID, "Log-Player.Item-Pickup")) {

                    this.itemPickupChannel = this.jda.getTextChannelById(itemPickupChannelID);

                }

                if (this.isValid(furnaceChannelID, "Log-Player.Furnace")) {

                    this.furnaceChannel = this.jda.getTextChannelById(furnaceChannelID);

                }

                if (this.isValid(gameModeChannelID, "Log-Player.Game-Mode")) {

                    this.gameModeChannel = this.jda.getTextChannelById(gameModeChannelID);

                }

                if (this.isValid(craftChannelID, "Log-Player.Craft")) {

                    this.craftChannel = this.jda.getTextChannelById(craftChannelID);

                }

                if (this.isValid(registrationChannelID, "Log-Player.Registration")) {

                    this.registrationChannel = this.jda.getTextChannelById(registrationChannelID);

                }

                if (this.isValid(primedTNTChannelID, "Log-Player.Primed-TNT")) {

                    this.primedTNTChannel = this.jda.getTextChannelById(primedTNTChannelID);

                }

                if (this.isValid(chestInteractionChannelID, "Log-Player.Chest-Interaction")) {

                    this.chestInteractionChannel = this.jda.getTextChannelById(chestInteractionChannelID);

                }

                if (this.isValid(entityDeathChannelID, "Log-Player.Entity-Death")) {

                    this.entityDeathChannel = this.jda.getTextChannelById(entityDeathChannelID);

                }

                if (this.isValid(signChangeChannelID, "Log-Player.Sign-Change")) {

                    this.signChangeChannel = this.jda.getTextChannelById(signChangeChannelID);

                }

                // Server Side Part
                if (this.isValid(serverStartChannelID, "Log-Server.Start")) {

                    this.serverStartChannel = this.jda.getTextChannelById(serverStartChannelID);

                }

                if (this.isValid(serverStopChannelID, "Log-Server.Stop")) {

                    this.serverStopChannel = this.jda.getTextChannelById(serverStopChannelID);

                }

                if (this.isValid(consoleChannelID, "Log-Server.Console-Commands")) {

                    this.consoleChannel = this.jda.getTextChannelById(consoleChannelID);

                }

                if (this.isValid(ramChannelID, "Log-Server.RAM")) {

                    this.ramChannel = this.jda.getTextChannelById(ramChannelID);

                }

                if (this.isValid(tpsChannelID, "Log-Server.TPS")) {

                    this.tpsChannel = this.jda.getTextChannelById(tpsChannelID);

                }

                if (this.isValid(portalCreationChannelID, "Log-Server.Portal-Creation")) {

                    this.portalCreationChannel = this.jda.getTextChannelById(portalCreationChannelID);

                }

                if (this.isValid(rConChannelID, "Log-Server.RCON")) {

                    this.rConChannel = this.jda.getTextChannelById(rConChannelID);

                }

                if (this.isValid(commandBlockChannelID, "Log-Server.Command-Block")) {

                    this.commandBlockChannel = this.jda.getTextChannelById(commandBlockChannelID);

                }

                // Extra Checkers Part
                if (this.isValid(afkChannelID, "Log-Extras.Essentials-AFK")) {

                    this.afkChannel = this.jda.getTextChannelById(afkChannelID);

                }

                if (this.isValid(wrongPasswordChannelID, "Log-Extras.AuthMe-Wrong-Password")) {

                    this.wrongPasswordChannel = this.jda.getTextChannelById(wrongPasswordChannelID);

                }

                if (this.isValid(vaultChannelID, "Log-Extras.Vault")) {

                    this.vaultChannel = this.jda.getTextChannelById(vaultChannelID);

                }

                if (this.isValid(liteBansChannelID, "Log-Extras.LiteBans")) {

                    this.liteBansChannel = this.jda.getTextChannelById(liteBansChannelID);

                }

                if (this.isValid(advancedBanChannelID, "Log-Extras.AdvancedBan")) {

                    this.advancedBanChannel = this.jda.getTextChannelById(advancedBanChannelID);

                }

                // Version Exception Part
                if (this.isValid(woodStrippingChannelID, "Log-Version-Exceptions.Wood-Stripping")) {

                    this.woodStrippingChannel = this.jda.getTextChannelById(woodStrippingChannelID);

                }
            } catch (Exception e) {

                Log.severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");
                e.printStackTrace();

            }
        }
    }

    private boolean isValid(String channelID, String path) {

        if (channelID == null) return false;

        return (!channelID.isEmpty() && this.main.getConfig().getBoolean(path) && (!channelID.equals("CHANNEL_ID") && !channelID.equals("LINK_HERE")));
    }

    public void staffChat(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.staffChannel);

    }

    public void playerChat(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerChatChannel);
    }

    public void playerCommand(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerCommandsChannel);
    }

    public void console(String content, boolean contentInAuthorLine) {

        if (this.consoleChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.consoleChannel.sendMessage(builder.build()).queue();
    }

    public void commandBlock(String content, boolean contentInAuthorLine) {

        if (this.commandBlockChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Command Block");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.commandBlockChannel.sendMessage(builder.build()).queue();
    }

    public void playerSignText(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerSignTextChannel);
    }

    public void playerJoin(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerJoinChannel);
    }

    public void playerLeave(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerLeaveChannel);
    }

    public void playerKick(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerKickChannel);
    }

    public void playerDeath(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerDeathChannel);
    }

    public void playerTeleport(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerTeleportChannel);
    }

    public void playerLevel(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerLevelChannel);
    }

    public void blockPlace(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.blockPlaceChannel);
    }

    public void blockBreak(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.blockBreakChannel);
    }

    public void portalCreation(String content, boolean contentInAuthorLine) {

        if (this.portalCreationChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.portalCreationChannel.sendMessage(builder.build()).queue();
    }

    public void bucketFill(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.bucketFillChannel);
    }

    public void bucketEmpty(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.bucketEmptyChannel);
    }

    public void anvil(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.anvilChannel);
    }

    public void tps(String content, boolean contentInAuthorLine) {

        if (this.tpsChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("TPS");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.tpsChannel.sendMessage(builder.build()).queue();
    }

    public void ram(String content, boolean contentInAuthorLine) {

        if (this.ramChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.ramChannel.sendMessage(builder.build()).queue();
    }

    public void serverStart(String content, boolean contentInAuthorLine) {

        if (this.serverStartChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.serverStartChannel.sendMessage(builder.build()).queue();
    }

    public void rCon(String content, boolean contentInAuthorLine) {

        if (this.rConChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RCON");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.rConChannel.sendMessage(builder.build()).queue();
    }

    public void serverStop(String content, boolean contentInAuthorLine) {

        if (this.serverStopChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.serverStopChannel.sendMessage(builder.build()).queue();
    }

    public void itemDrop(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.itemDropChannel);
    }

    public void enchanting(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.enchantingChannel);
    }

    public void bookEditing(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.bookEditingChannel);
    }

    public void afk(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.afkChannel);
    }

    public void wrongPassword(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.wrongPasswordChannel);
    }

    public void itemPickup(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.itemPickupChannel);
    }

    public void furnace(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.furnaceChannel);
    }

    public void gameMode(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.gameModeChannel);
    }

    public void playerCraft(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.craftChannel);
    }

    public void vault(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.vaultChannel);
    }

    public void playerRegistration(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.registrationChannel);
    }

    public void primedTNT(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.primedTNTChannel);
    }

    public void chestInteraction(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.chestInteractionChannel);
    }

    public void liteBans(String content, boolean contentInAuthorLine) {

        if (this.liteBansChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("LiteBans");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.liteBansChannel.sendMessage(builder.build()).queue();
    }

    public void advancedBan(String content, boolean contentInAuthorLine) {

        if (this.advancedBanChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("AdvancedBan");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.advancedBanChannel.sendMessage(builder.build()).queue();
    }

    public void woodStripping(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.woodStrippingChannel);
    }

    public void entityDeath(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.entityDeathChannel);
    }

    public void signChange(Player player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.signChangeChannel);
    }

    private void discordUtil(Player player, String content, boolean contentInAuthorLine, TextChannel channel) {
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
                if (this.main.getDiscordFile().get().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Log.info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Log.severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }
}
