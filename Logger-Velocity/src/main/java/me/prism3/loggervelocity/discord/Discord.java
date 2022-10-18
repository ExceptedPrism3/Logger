package me.prism3.loggervelocity.discord;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.UUID;

public class Discord {

    private JDA jda;

    private final Main main = Main.getInstance();

    private TextChannel staffChannel;
    private TextChannel playerChatChannel;
    private TextChannel playerCommandsChannel;
    private TextChannel playerLoginChannel;
    private TextChannel playerLeaveChannel;

    private TextChannel consoleCommandsChannel;
    private TextChannel serverStartChannel;
    private TextChannel serverStopChannel;
    private TextChannel ramChannel;

    private TextChannel liteBansChannel;

    public void run() {

        if (this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            final String botToken = this.main.getDiscordFile().getString("Discord.Bot-Token");

            try {

                this.jda = JDABuilder.createDefault(botToken).build().awaitReady();
                if (this.main.getDiscordFile().get().getBoolean("ActivityCycling.Enabled")) new DiscordStatus(this.jda);

            } catch (final Exception e) {

                Log.error("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;
            }

            final String staffChannelID = this.main.getDiscordFile().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = this.main.getDiscordFile().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = this.main.getDiscordFile().getString("Discord.Player-Commands.Channel-ID");

            final String playerLoginChannelID = this.main.getDiscordFile().getString("Discord.Player-Login.Channel-ID");

            final String playerLeaveChannelID = this.main.getDiscordFile().getString("Discord.Player-Leave.Channel-ID");

            final String consoleCommandsChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String serverStartChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Stop.Channel-ID");

            final String ramChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.RAM.Channel-ID");

            final String liteBansChannelID = this.main.getDiscordFile().getString("Discord.Extra.LiteBans.Channel-ID");

            try {

                if (this.isValid(staffChannelID, "Staff.Enabled"))
                    this.staffChannel = this.jda.getTextChannelById(staffChannelID);

                if (this.isValid(playerChatChannelID, "Log-Player.Chat"))
                    this.playerChatChannel = this.jda.getTextChannelById(playerChatChannelID);

                if (this.isValid(playerCommandsChannelID, "Log-Player.Commands"))
                    this.playerCommandsChannel = this.jda.getTextChannelById(playerCommandsChannelID);

                if (this.isValid(playerLoginChannelID, "Log-Player.Login"))
                    this.playerLoginChannel = this.jda.getTextChannelById(playerLoginChannelID);

                if (this.isValid(playerLeaveChannelID, "Log-Player.Leave"))
                    this.playerLeaveChannel = this.jda.getTextChannelById(playerLeaveChannelID);

                if (this.isValid(consoleCommandsChannelID, "Log-Server.Console-Commands"))
                    this.consoleCommandsChannel = this.jda.getTextChannelById(consoleCommandsChannelID);

                if (this.isValid(serverStartChannelID, "Log-Server.Start"))
                    this.serverStartChannel = this.jda.getTextChannelById(serverStartChannelID);

                if (this.isValid(serverStopChannelID, "Log-Server.Stop"))
                    this.serverStopChannel = this.jda.getTextChannelById(serverStopChannelID);

                if (this.isValid(ramChannelID, "Log-Server.RAM"))
                    this.ramChannel = this.jda.getTextChannelById(ramChannelID);

                if (this.isValid(liteBansChannelID, "Log-Extra.LiteBans"))
                    this.liteBansChannel = this.jda.getTextChannelById(liteBansChannelID);

            } catch (final Exception e) {

                Log.error("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");
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

    public void playerCommands(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerCommandsChannel);
    }

    public void playerLogin(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerLoginChannel);
    }

    public void playerLeave(String playerName, UUID playerUUID, String content, boolean contentInAuthorLine) {
        this.discordUtil(playerName, playerUUID, content, contentInAuthorLine, this.playerLeaveChannel);
    }

    public void console(String content, boolean contentInAuthorLine) {

        if (this.consoleCommandsChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.consoleCommandsChannel.sendMessage(builder.build()).queue();
    }

    public void serverStart(String content, boolean contentInAuthorLine) {

        if (this.serverStartChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.serverStartChannel.sendMessage(builder.build()).queue();
    }

    public void serverStop(String content, boolean contentInAuthorLine) {

        if (this.serverStopChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.serverStopChannel.sendMessage(builder.build()).queue();
    }

    public void ram(String content, boolean contentInAuthorLine) {

        if (this.ramChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.ramChannel.sendMessage(builder.build()).queue();
    }

    public void liteBans(String content, boolean contentInAuthorLine) {

        if (this.liteBansChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("LiteBans");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.liteBansChannel.sendMessage(builder.build()).queue();
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
                if (this.main.getDiscordFile().get().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Log.info("Discord Bot Bridge has been closed!");

            } catch (final Exception e) {

                Log.error("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");
                e.printStackTrace();
            }
        }
    }
}
