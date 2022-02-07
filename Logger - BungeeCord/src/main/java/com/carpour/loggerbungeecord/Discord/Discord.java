package com.carpour.loggerbungeecord.Discord;

import com.carpour.loggerbungeecord.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Discord {

    private final Main main = Main.getInstance();

    private static JDA jda;

    private static TextChannel staffChannel;
    private static TextChannel playerChatChannel;
    private static TextChannel playerCommandsChannel;
    private static TextChannel playerLoginChannel;
    private static TextChannel playerLeaveChannel;

    private static TextChannel serverReloadChannel;
    private static TextChannel serverStartChannel;
    private static TextChannel serverStopChannel;
    private static TextChannel ramChannel;

    private static TextChannel liteBansChannel;

    public void run() {

        if (DiscordFile.getBoolean("Discord.Enable")) {

            String botToken = DiscordFile.getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();

                new DiscordStatus();

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            String staffChannelID = DiscordFile.getString("Discord.Staff.Channel-ID");

            String playerChatChannelID = DiscordFile.getString("Discord.Player-Chat.Channel-ID");

            String playerCommandsChannelID = DiscordFile.getString("Discord.Player-Commands.Channel-ID");

            String playerLoginChannelID = DiscordFile.getString("Discord.Player-Login.Channel-ID");

            String playerLeaveChannelID = DiscordFile.getString("Discord.Player-Leave.Channel-ID");

            String serverReloadChannelID = DiscordFile.getString("Discord.Server-Side.Restart.Channel-ID");

            String serverStartChannelID = DiscordFile.getString("Discord.Server-Side.Start.Channel-ID");

            String serverStopChannelID = DiscordFile.getString("Discord.Server-Side.Stop.Channel-ID");

            String ramChannelID = DiscordFile.getString("Discord.Server-Side.RAM.Channel-ID");

            String liteBansChannelID = DiscordFile.getString("Discord.Extra.LiteBans.Channel-ID");


            try {

                if (!(staffChannelID.isEmpty()) && main.getConfig().getBoolean("Staff.Enabled") && !staffChannelID.equals("LINK_HERE")) {

                    staffChannel = jda.getTextChannelById(staffChannelID);

                }

                if (!(playerChatChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Chat") && !playerChatChannelID.equals("LINK_HERE")) {

                    playerChatChannel = jda.getTextChannelById(playerChatChannelID);

                }

                if (!(playerCommandsChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Commands") && !playerCommandsChannelID.equals("LINK_HERE")) {

                    playerCommandsChannel = jda.getTextChannelById(playerCommandsChannelID);

                }

                if (!(playerLoginChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Login") && !playerLoginChannelID.equals("LINK_HERE")) {

                    playerLoginChannel = jda.getTextChannelById(playerLoginChannelID);

                }

                if (!(playerLeaveChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Player.Leave") && !playerLeaveChannelID.equals("LINK_HERE")) {

                    playerLeaveChannel = jda.getTextChannelById(playerLeaveChannelID);

                }

                if (!(serverReloadChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Reload") && !serverReloadChannelID.equals("LINK_HERE")) {

                    serverReloadChannel = jda.getTextChannelById(serverReloadChannelID);

                }

                if (!(serverStartChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Start") && !serverStartChannelID.equals("LINK_HERE")) {

                    serverStartChannel = jda.getTextChannelById(serverStartChannelID);

                }

                if (!(serverStopChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Stop") && !serverStopChannelID.equals("LINK_HERE")) {

                    serverStopChannel = jda.getTextChannelById(serverStopChannelID);

                }

                if (!(ramChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Stop") && !ramChannelID.equals("LINK_HERE")) {

                    ramChannel = jda.getTextChannelById(ramChannelID);

                }

                if (!(liteBansChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extra.LiteBans") && !liteBansChannelID.equals("LINK_HERE")) {

                    liteBansChannel = jda.getTextChannelById(liteBansChannelID);

                }
            }catch (Exception e){

                main.getLogger().severe("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

            }
        }
    }

    public static void staffChat(ProxiedPlayer player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, staffChannel);

    }

    public static void playerChat(ProxiedPlayer player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerChatChannel);
    }

    public static void playerCommands(ProxiedPlayer player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerCommandsChannel);
    }

    public static void playerLogin(ProxiedPlayer player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLoginChannel);
    }

    public static void playerLeave(ProxiedPlayer player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLeaveChannel);
    }

    public static void serverReload(String player, String content, boolean contentinAuthorLine) {

        if (serverReloadChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Console Reload");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverReloadChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStart(String content, boolean contentinAuthorLine) {

        if (serverStartChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStartChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStop(String content, boolean contentinAuthorLine) {

        if (serverStopChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStopChannel.sendMessage(builder.build()).queue();
    }

    public static void ram(String content, boolean contentinAuthorLine) {

        if (ramChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentinAuthorLine) builder.setDescription(content);

        ramChannel.sendMessage(builder.build()).queue();
    }

    public static void liteBans(String content, boolean contentinAuthorLine) {

        if (liteBansChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("LiteBans");

        if (!contentinAuthorLine) builder.setDescription(content);

        liteBansChannel.sendMessage(builder.build()).queue();
    }

    private static void discordUtil(ProxiedPlayer player, String content, boolean contentinAuthorLine, TextChannel channel) {
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
                DiscordStatus.getThreadPool().shutdown();
                Main.getInstance().getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Main.getInstance().getLogger().severe("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");
                e.printStackTrace();

            }
        }
    }
    public JDA getJda(){ return jda; }
}
