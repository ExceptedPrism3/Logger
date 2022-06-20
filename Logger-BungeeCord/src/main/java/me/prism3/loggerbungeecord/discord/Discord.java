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

    private TextChannel serverReloadChannel;
    private TextChannel serverStartChannel;
    private TextChannel serverStopChannel;
    private TextChannel ramChannel;

    private TextChannel liteBansChannel;

    public void run() {

        if (main.getDiscordFile().getBoolean("Discord.Enable")) {

            final String botToken = main.getDiscordFile().getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();

                if (main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) new DiscordStatus();

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            final String staffChannelID = main.getDiscordFile().getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = main.getDiscordFile().getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = main.getDiscordFile().getString("Discord.Player-Commands.Channel-ID");

            final String playerLoginChannelID = main.getDiscordFile().getString("Discord.Player-Login.Channel-ID");

            final String playerLeaveChannelID = main.getDiscordFile().getString("Discord.Player-Leave.Channel-ID");

            final String serverReloadChannelID = main.getDiscordFile().getString("Discord.Server-Side.Restart.Channel-ID");

            final String serverStartChannelID = main.getDiscordFile().getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = main.getDiscordFile().getString("Discord.Server-Side.Stop.Channel-ID");

            final String ramChannelID = main.getDiscordFile().getString("Discord.Server-Side.RAM.Channel-ID");

            final String liteBansChannelID = main.getDiscordFile().getString("Discord.Extra.LiteBans.Channel-ID");


            try {

                if (this.isValid(staffChannelID, "Staff.Enabled")) {

                    this.staffChannel = this.jda.getTextChannelById(staffChannelID);

                }

                if (this.isValid(playerChatChannelID, "Log-Player.Chat")) {

                    this.playerChatChannel = jda.getTextChannelById(playerChatChannelID);

                }

                if (this.isValid(playerCommandsChannelID, "Log-Player.Commands")) {

                    this.playerCommandsChannel = jda.getTextChannelById(playerCommandsChannelID);

                }

                if (this.isValid(playerLoginChannelID, "Log-Player.Login")) {

                    this.playerLoginChannel = jda.getTextChannelById(playerLoginChannelID);

                }

                if (this.isValid(playerLeaveChannelID, "Log-Player.Leave")) {

                    this.playerLeaveChannel = jda.getTextChannelById(playerLeaveChannelID);

                }

                if (this.isValid(serverReloadChannelID, "Log-Server.Reload")) {

                    this.serverReloadChannel = jda.getTextChannelById(serverReloadChannelID);

                }

                if (this.isValid(serverStartChannelID, "Log-Server.Start")) {

                    this.serverStartChannel = jda.getTextChannelById(serverStartChannelID);

                }

                if (this.isValid(serverStopChannelID, "Log-Server.Stop")) {

                    this.serverStopChannel = jda.getTextChannelById(serverStopChannelID);

                }

                if (this.isValid(ramChannelID, "Log-Server.RAM")) {

                    this.ramChannel = jda.getTextChannelById(ramChannelID);

                }

                if (this.isValid(liteBansChannelID, "Log-Extra.LiteBans")) {

                    this.liteBansChannel = jda.getTextChannelById(liteBansChannelID);

                }
            } catch (Exception e) {

                Main.getInstance().getLogger().severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    private boolean isValid(String channelID, String path) {

        if (channelID == null) return false;

        return (!channelID.isEmpty() && main.getConfig().getBoolean(path) && !channelID.equals("LINK_HERE"));

    }

    public void staffChat(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, this.staffChannel);

    }

    public void playerChat(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, this.playerChatChannel);
    }

    public void playerCommands(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, this.playerCommandsChannel);
    }

    public void playerLogin(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, this.playerLoginChannel);
    }

    public void playerLeave(ProxiedPlayer player, String content, boolean contentInAuthorLine) {

        discordUtil(player, content, contentInAuthorLine, this.playerLeaveChannel);
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

    private static void discordUtil(ProxiedPlayer player, String content, boolean contentInAuthorLine, TextChannel channel) {
        if (channel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(contentInAuthorLine ? content : player.getName(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentInAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (jda != null) {
            try {

                jda.shutdown();
                jda = null;
                if (main.getDiscordFile().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Main.getInstance().getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");
                e.printStackTrace();

            }
        }
    }
    public JDA getJda() { return jda; }
}
