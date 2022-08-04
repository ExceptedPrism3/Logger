package me.prism3.loggerbungeecord.discord;

import me.prism3.loggerbungeecord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Discord {

    private final Main main = Main.getInstance();

    private JDA jda;

    private TextChannel staffChannel;
    private TextChannel playerChatChannel;
    private TextChannel playerCommandsChannel;
    private TextChannel playerLoginChannel;
    private TextChannel playerLeaveChannel;

    private TextChannel serverSwitchChannel;
    private TextChannel serverReloadChannel;
    private TextChannel serverStartChannel;
    private TextChannel serverStopChannel;
    private TextChannel ramChannel;
    private TextChannel liteBansChannel;

    public void run() {

        if (this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            final String botToken = this.main.getDiscordFile().getString("Discord.Bot-Token");

            try {

                this.jda = JDABuilder.createDefault(botToken).build().awaitReady();

                if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) new DiscordStatus();

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            final String staffChannelID = this.main.getDiscordFile().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = this.main.getDiscordFile().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = this.main.getDiscordFile().getString("Discord.Player-Commands.Channel-ID");

            final String playerLoginChannelID = this.main.getDiscordFile().getString("Discord.Player-Login.Channel-ID");

            final String playerLeaveChannelID = this.main.getDiscordFile().getString("Discord.Player-Leave.Channel-ID");
            final String serverSwitchChannelID = this.main.getDiscordFile().getString("Discord.Server-Switch-Channel-ID");

            final String serverReloadChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Restart.Channel-ID");

            final String serverStartChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.Stop.Channel-ID");

            final String ramChannelID = this.main.getDiscordFile().getString("Discord.Server-Side.RAM.Channel-ID");

            final String liteBansChannelID = this.main.getDiscordFile().getString("Discord.Extra.LiteBans.Channel-ID");


            try {

                if (this.isValid(staffChannelID, "Staff.Enabled")) {

                    this.staffChannel = this.jda.getTextChannelById(staffChannelID);

                }

                if (this.isValid(playerChatChannelID, "Log-Player.Chat")) {

                    this.playerChatChannel = this.jda.getTextChannelById(playerChatChannelID);

                }

                if (this.isValid(playerCommandsChannelID, "Log-Player.Commands")) {

                    this.playerCommandsChannel = this.jda.getTextChannelById(playerCommandsChannelID);

                }

                if (this.isValid(playerLoginChannelID, "Log-Player.Login")) {

                    this.playerLoginChannel = this.jda.getTextChannelById(playerLoginChannelID);

                }

                if (this.isValid(playerLeaveChannelID, "Log-Player.Leave")) {

                    this.playerLeaveChannel = this.jda.getTextChannelById(playerLeaveChannelID);

                }

                if (this.isValid(serverSwitchChannelID, "Log-Player.Switch")) {
                    this.serverSwitchChannel = this.jda.getTextChannelById(serverSwitchChannelID);
                }

                if (this.isValid(serverReloadChannelID, "Log-Server.Reload")) {

                    this.serverReloadChannel = this.jda.getTextChannelById(serverReloadChannelID);

                }

                if (this.isValid(serverStartChannelID, "Log-Server.Start")) {

                    this.serverStartChannel = this.jda.getTextChannelById(serverStartChannelID);

                }

                if (this.isValid(serverStopChannelID, "Log-Server.Stop")) {

                    this.serverStopChannel = this.jda.getTextChannelById(serverStopChannelID);

                }

                if (this.isValid(ramChannelID, "Log-Server.RAM")) {

                    this.ramChannel = this.jda.getTextChannelById(ramChannelID);

                }

                if (this.isValid(liteBansChannelID, "Log-Extra.LiteBans")) {

                    this.liteBansChannel = this.jda.getTextChannelById(liteBansChannelID);

                }
            } catch (Exception e) {

                this.main.getLogger().severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    private boolean isValid(String channelID, String path) {

        if (channelID == null) return false;

        return (!channelID.isEmpty() && this.main.getConfig().getBoolean(path) && !channelID.equals("LINK_HERE"));

    }

    public void staffChat(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.staffChannel);

    }

    public void playerChat(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerChatChannel);
    }

    public void playerCommands(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerCommandsChannel);
    }

    public void playerLogin(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerLoginChannel);
    }

    public void playerLeave(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.playerLeaveChannel);
    }

    public void playerServerSwitch(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        this.discordUtil(player, content, contentInAuthorLine, this.serverSwitchChannel);
    }

    public void serverReload(String player, String content, boolean contentInAuthorLine) {

        if (this.serverReloadChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console Reload");

        if (!contentInAuthorLine) builder.setDescription(content);

        this.serverReloadChannel.sendMessage(builder.build()).queue();
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

    private void discordUtil(ProxiedPlayer player, String content, boolean contentInAuthorLine, TextChannel channel) {
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
                if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Main.getInstance().getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");
                e.printStackTrace();

            }
        }
    }
    public JDA getJda() { return this.jda; }
}
