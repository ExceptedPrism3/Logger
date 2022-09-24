package me.prism3.logger.discord;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.UUID;

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
    private TextChannel itemFramePlaceChannel;
    private TextChannel itemFrameBreakChannel;
    private TextChannel armorStandPlaceChannel;
    private TextChannel armorStandBreakChannel;
    private TextChannel leverInteractionChannel;
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

        if (this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            final String botToken = this.main.getDiscordFile().getString("Discord.Bot-Token");

            try {

                this.jda = JDABuilder.createDefault(botToken).build().awaitReady();

                if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) new DiscordStatus(this.jda);

            } catch (final Exception e) {

                Log.severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;
            }

            // Player Side Part
            final String staffChannelID = this.main.getDiscordFile().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = this.main.getDiscordFile().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = this.main.getDiscordFile().getString("Discord.Player-Commands.Channel-ID");

            final String playerSignTextChannelID = this.main.getDiscordFile().getString("Discord.Player-Sign-Text.Channel-ID");

            final String playerJoinChannelID = this.main.getDiscordFile().getString("Discord.Player-Join.Channel-ID");

            final String playerLeaveChannelID = this.main.getDiscordFile().getString("Discord.Player-Leave.Channel-ID");

            final String playerKickChannelID = this.main.getDiscordFile().getString("Discord.Player-Kick.Channel-ID");

            final String playerDeathChannelID = this.main.getDiscordFile().getString("Discord.Player-Death.Channel-ID");

            final String playerTeleportChannelID = this.main.getDiscordFile().getString("Discord.Player-Teleport.Channel-ID");

            final String playerLevelChannelID = this.main.getDiscordFile().getString("Discord.Player-Level.Channel-ID");

            final String blockPlaceChannelID = this.main.getDiscordFile().getString("Discord.Block-Place.Channel-ID");

            final String blockBreakChannelID = this.main.getDiscordFile().getString("Discord.Block-Break.Channel-ID");

            final String bucketFillChannelID = this.main.getDiscordFile().getString("Discord.Bucket-Fill.Channel-ID");

            final String bucketEmptyChannelID = this.main.getDiscordFile().getString("Discord.Bucket-Empty.Channel-ID");

            final String anvilChannelID = this.main.getDiscordFile().getString("Discord.Anvil.Channel-ID");

            final String itemDropChannelID = this.main.getDiscordFile().getString("Discord.Item-Drop.Channel-ID");

            final String enchantingChannelID = this.main.getDiscordFile().getString("Discord.Enchanting.Channel-ID");

            final String bookEditingChannelID = this.main.getDiscordFile().getString("Discord.Book-Editing.Channel-ID");

            final String itemPickupChannelID = this.main.getDiscordFile().getString("Discord.Item-Pickup.Channel-ID");

            final String furnaceChannelID = this.main.getDiscordFile().getString("Discord.Furnace.Channel-ID");

            final String gameModeChannelID = this.main.getDiscordFile().getString("Discord.Game-Mode.Channel-ID");

            final String craftChannelID = this.main.getDiscordFile().getString("Discord.Craft.Channel-ID");

            final String registrationChannelID = this.main.getDiscordFile().getString("Discord.Registration.Channel-ID");

            final String primedTNTChannelID = this.main.getDiscordFile().getString("Discord.Primed-TNT.Channel-ID");

            final String chestInteractionChannelID = this.main.getDiscordFile().getString("Discord.Chest-Interaction.Channel-ID");

            final String entityDeathChannelID = this.main.getDiscordFile().getString("Discord.Entity-Death.Channel-ID");

            final String itemFramePlaceChannelID = this.main.getDiscordFile().getString("Discord.Item-Frame-Place.Channel-ID");

            final String itemFrameBreakChannelID = this.main.getDiscordFile().getString("Discord.Item-Frame-Break.Channel-ID");

            final String armorStandPlaceChannelID = this.main.getDiscordFile().getString("Discord.ArmorStand-Place.Channel-ID");

            final String armorStandBreakChannelID = this.main.getDiscordFile().getString("Discord.ArmorStand-Break.Channel-ID");

            final String leverInteractionChannelID = this.main.getDiscordFile().getString("Discord.Lever-Interaction.Channel-ID");

            final String spawnEggChannelID = this.main.getDiscordFile().getString("Discord.Spawn-Egg.Channel-ID");

            // Server Side Part
            final String serverStartChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Stop.Channel-ID");

            final String consoleChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String ramChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.RAM.Channel-ID");

            final String tpsChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.TPS.Channel-ID");

            final String portalCreationChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Portal-Creation.Channel-ID");

            final String rConChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.RCON.Channel-ID");

            final String commandBlockChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Command-Block.Channel-ID");

            // Extras
            final String afkChannelID = this.main.getDiscordFile().getString("Discord.Extras.AFK.Channel-ID");

            final String wrongPasswordChannelID = this.main.getDiscordFile().getString("Discord.Extras.Wrong-Password.Channel-ID");

            final String vaultChannelID = this.main.getDiscordFile().getString("Discord.Extras.Vault.Channel-ID");

            final String liteBansChannelID = this.main.getDiscordFile().getString("Discord.Extras.LiteBans.Channel-ID");

            final String advancedBanChannelID = this.main.getDiscordFile().getString("Discord.Extras.AdvancedBan.Channel-ID");

            // Version Exception
            final String woodStrippingChannelID = this.main.getDiscordFile().getString("Discord.Version-Exceptions.Wood-Stripping.Channel-ID");

            try {

                // Player Side Part
                if (this.isValid(staffChannelID, "Staff.Enabled"))
                    this.staffChannel = this.jda.getTextChannelById(staffChannelID);

                if (this.isValid(playerChatChannelID, "Log-Player.Chat"))
                    this.playerChatChannel = this.jda.getTextChannelById(playerChatChannelID);

                if (this.isValid(playerCommandsChannelID, "Log-Player.Commands"))
                    this.playerCommandsChannel = this.jda.getTextChannelById(playerCommandsChannelID);

                if (this.isValid(playerSignTextChannelID, "Log-Player.Sign-Text"))
                    this.playerSignTextChannel = this.jda.getTextChannelById(playerSignTextChannelID);

                if (this.isValid(playerJoinChannelID, "Log-Player.Join"))
                    this.playerJoinChannel = this.jda.getTextChannelById(playerJoinChannelID);

                if (this.isValid(playerLeaveChannelID, "Log-Player.Leave"))
                    this.playerLeaveChannel = this.jda.getTextChannelById(playerLeaveChannelID);

                if (this.isValid(playerKickChannelID, "Log-Player.Kick"))
                    this.playerKickChannel = this.jda.getTextChannelById(playerKickChannelID);

                if (this.isValid(playerDeathChannelID, "Log-Player.Death"))
                    this.playerDeathChannel = this.jda.getTextChannelById(playerDeathChannelID);

                if (this.isValid(playerTeleportChannelID, "Log-Player.Teleport"))
                    this.playerTeleportChannel = this.jda.getTextChannelById(playerTeleportChannelID);

                if (this.isValid(playerLevelChannelID, "Log-Player.Level"))
                    this.playerLevelChannel = this.jda.getTextChannelById(playerLevelChannelID);

                if (this.isValid(blockPlaceChannelID, "Log-Player.Block-Place"))
                    this.blockPlaceChannel = this.jda.getTextChannelById(blockPlaceChannelID);

                if (this.isValid(blockBreakChannelID, "Log-Player.Block-Break"))
                    this.blockBreakChannel = this.jda.getTextChannelById(blockBreakChannelID);

                if (this.isValid(bucketFillChannelID, "Log-Player.Bucket-Fill\""))
                    this.bucketFillChannel = this.jda.getTextChannelById(bucketFillChannelID);

                if (this.isValid(bucketEmptyChannelID, "Log-Player.Bucket-Empty"))
                    this.bucketEmptyChannel = this.jda.getTextChannelById(bucketEmptyChannelID);

                if (this.isValid(anvilChannelID, "Log-Player.Anvil"))
                    this.anvilChannel = this.jda.getTextChannelById(anvilChannelID);

                if (this.isValid(itemDropChannelID, "Log-Player.Item-Drop"))
                    this.itemDropChannel = this.jda.getTextChannelById(itemDropChannelID);

                if (this.isValid(enchantingChannelID, "Log-Player.Enchanting"))
                    this.enchantingChannel = this.jda.getTextChannelById(enchantingChannelID);

                if (this.isValid(bookEditingChannelID, "Log-Player.Book-Editing"))
                    this.bookEditingChannel = this.jda.getTextChannelById(bookEditingChannelID);

                if (this.isValid(itemPickupChannelID, "Log-Player.Item-Pickup"))
                    this.itemPickupChannel = this.jda.getTextChannelById(itemPickupChannelID);

                if (this.isValid(furnaceChannelID, "Log-Player.Furnace"))
                    this.furnaceChannel = this.jda.getTextChannelById(furnaceChannelID);

                if (this.isValid(gameModeChannelID, "Log-Player.Game-Mode"))
                    this.gameModeChannel = this.jda.getTextChannelById(gameModeChannelID);

                if (this.isValid(craftChannelID, "Log-Player.Craft"))
                    this.craftChannel = this.jda.getTextChannelById(craftChannelID);

                if (this.isValid(registrationChannelID, "Log-Player.Registration"))
                    this.registrationChannel = this.jda.getTextChannelById(registrationChannelID);

                if (this.isValid(primedTNTChannelID, "Log-Player.Primed-TNT"))
                    this.primedTNTChannel = this.jda.getTextChannelById(primedTNTChannelID);

                if (this.isValid(chestInteractionChannelID, "Log-Player.Chest-Interaction"))
                    this.chestInteractionChannel = this.jda.getTextChannelById(chestInteractionChannelID);

                if (this.isValid(entityDeathChannelID, "Log-Player.Entity-Death"))
                    this.entityDeathChannel = this.jda.getTextChannelById(entityDeathChannelID);

                if (this.isValid(itemFramePlaceChannelID, "Log-Player.Item-Frame-Place"))
                    this.itemFramePlaceChannel = this.jda.getTextChannelById(itemFramePlaceChannelID);

                if (this.isValid(itemFrameBreakChannelID, "Log-Player.Item-Frame-Break"))
                    this.itemFrameBreakChannel = this.jda.getTextChannelById(itemFrameBreakChannelID);

                if (this.isValid(armorStandPlaceChannelID, "Log-Player.ArmorStand-Place"))
                    this.armorStandPlaceChannel = this.jda.getTextChannelById(armorStandPlaceChannelID);

                if (this.isValid(armorStandBreakChannelID, "Log-Player.ArmorStand-Break"))
                    this.armorStandBreakChannel = this.jda.getTextChannelById(armorStandBreakChannelID);

                if (this.isValid(leverInteractionChannelID, "Log-Player.Lever-Interaction"))
                    this.leverInteractionChannel = this.jda.getTextChannelById(leverInteractionChannelID);

                if (this.isValid(spawnEggChannelID, "Log-Player.Spawn-Egg"))
                    this.spawnEggChannel = this.jda.getTextChannelById(spawnEggChannelID);

                // Server Side Part
                if (this.isValid(serverStartChannelID, "Log-Server.Start"))
                    this.serverStartChannel = this.jda.getTextChannelById(serverStartChannelID);

                if (this.isValid(serverStopChannelID, "Log-Server.Stop"))
                    this.serverStopChannel = this.jda.getTextChannelById(serverStopChannelID);

                if (this.isValid(consoleChannelID, "Log-Server.Console-Commands"))
                    this.consoleChannel = this.jda.getTextChannelById(consoleChannelID);

                if (this.isValid(ramChannelID, "Log-Server.RAM"))
                    this.ramChannel = this.jda.getTextChannelById(ramChannelID);

                if (this.isValid(tpsChannelID, "Log-Server.TPS"))
                    this.tpsChannel = this.jda.getTextChannelById(tpsChannelID);

                if (this.isValid(portalCreationChannelID, "Log-Server.Portal-Creation"))
                    this.portalCreationChannel = this.jda.getTextChannelById(portalCreationChannelID);

                if (this.isValid(rConChannelID, "Log-Server.RCON"))
                    this.rConChannel = this.jda.getTextChannelById(rConChannelID);

                if (this.isValid(commandBlockChannelID, "Log-Server.Command-Block"))
                    this.commandBlockChannel = this.jda.getTextChannelById(commandBlockChannelID);

                // Extra Checkers Part
                if (this.isValid(afkChannelID, "Log-Extras.Essentials-AFK"))
                    this.afkChannel = this.jda.getTextChannelById(afkChannelID);

                if (this.isValid(wrongPasswordChannelID, "Log-Extras.AuthMe-Wrong-Password"))
                    this.wrongPasswordChannel = this.jda.getTextChannelById(wrongPasswordChannelID);

                if (this.isValid(vaultChannelID, "Log-Extras.Vault"))
                    this.vaultChannel = this.jda.getTextChannelById(vaultChannelID);

                if (this.isValid(liteBansChannelID, "Log-Extras.LiteBans"))
                    this.liteBansChannel = this.jda.getTextChannelById(liteBansChannelID);

                if (this.isValid(advancedBanChannelID, "Log-Extras.AdvancedBan"))
                    this.advancedBanChannel = this.jda.getTextChannelById(advancedBanChannelID);

                // Version Exception Part
                if (this.isValid(woodStrippingChannelID, "Log-Version-Exceptions.Wood-Stripping"))
                    this.woodStrippingChannel = this.jda.getTextChannelById(woodStrippingChannelID);

            } catch (final Exception e) {

                Log.severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    private boolean isValid(String channelID, String path) {

        if (channelID == null) return false;

        return (!channelID.isEmpty() && this.main.getConfig().getBoolean(path) && !channelID.equals("LINK_HERE"));

    }

    public void staffChat(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.staffChannel);
    }

    public void playerChat(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerChatChannel);
    }

    public void playerCommand(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerCommandsChannel);
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

    public void playerSignText(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerSignTextChannel);
    }

    public void playerJoin(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerJoinChannel);
    }

    public void playerLeave(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerLeaveChannel);
    }

    public void playerKick(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerKickChannel);
    }

    public void playerDeath(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerDeathChannel);
    }

    public void playerTeleport(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerTeleportChannel);
    }

    public void playerLevel(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerLevelChannel);
    }

    public void blockPlace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.blockPlaceChannel);
    }

    public void blockBreak(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.blockBreakChannel);
    }

    public void portalCreation(String content, boolean contentInAuthorLine) {

        if (this.portalCreationChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Portal Creation");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.portalCreationChannel.sendMessage(builder.build()).queue();
    }

    public void bucketFill(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.bucketFillChannel);
    }

    public void bucketEmpty(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.bucketEmptyChannel);
    }

    public void anvil(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.anvilChannel);
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

    public void itemDrop(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.itemDropChannel);
    }

    public void enchanting(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.enchantingChannel);
    }

    public void bookEditing(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.bookEditingChannel);
    }

    public void afk(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.afkChannel);
    }

    public void wrongPassword(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.wrongPasswordChannel);
    }

    public void itemPickup(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.itemPickupChannel);
    }

    public void furnace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.furnaceChannel);
    }

    public void gameMode(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.gameModeChannel);
    }

    public void playerCraft(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.craftChannel);
    }

    public void vault(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.vaultChannel);
    }

    public void playerRegistration(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.registrationChannel);
    }

    public void primedTNT(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.primedTNTChannel);
    }

    public void chestInteraction(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.chestInteractionChannel);
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

    public void woodStripping(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.woodStrippingChannel);
    }

    public void entityDeath(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.entityDeathChannel);
    }

    public void itemFramePlace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.itemFramePlaceChannel);
    }

    public void itemFrameBreak(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.itemFrameBreakChannel);
    }

    public void armorStandPlace(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.armorStandPlaceChannel);
    }

    public void armorStandBreak(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.armorStandBreakChannel);
    }

    public void leverInteraction(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.leverInteractionChannel);
    }

    public void spawnEgg(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.spawnEggChannel);
    }

    private void discordUtil(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine, TextChannel channel) {

        if (channel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(contentInAuthorLine ? content : playerName,
                null, "https://crafatar.com/avatars/" + playerUUID + "?overlay=1");

        if (!contentInAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (this.jda != null) {
            try {

                this.jda.shutdown();
                this.jda = null;
                if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Log.info("Discord Bot Bridge has been closed!");

            } catch (final Exception e) {

                Log.severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");
                e.printStackTrace();

            }
        }
    }
}
