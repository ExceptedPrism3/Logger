package com.carpour.loggervelocity.Discord;

import com.carpour.loggervelocity.Main;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;

public class Discord {

    private static JDA jda;

    private final Main main = Main.getInstance();

    private static TextChannel staffChannel;
    private static TextChannel playerChatChannel;
    private static TextChannel playerCommandsChannel;
    private static TextChannel playerLoginChannel;
    private static TextChannel playerLeaveChannel;
    private static TextChannel consoleCommandsChannel;
    private static TextChannel serverStartChannel;
    private static TextChannel serverStopChannel;
    private static TextChannel ramChannel;

    public void run() {

        DiscordFile discordFile = new DiscordFile();

        if (discordFile.getBoolean("Discord.Enable")) {

            String botToken = discordFile.getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();

                new DiscordStatus();

            } catch (InterruptedException | LoginException e) {

                Main.getInstance().getLogger().error("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            String staffChannelID = discordFile.getString("Discord.Staff.Channel-ID");

            String playerChatChannelID = discordFile.getString("Discord.Player-Chat.Channel-ID");

            String playerCommandsChannelID = discordFile.getString("Discord.Player-Commands.Channel-ID");

            String playerLoginChannelID = discordFile.getString("Discord.Player-Login.Channel-ID");

            String playerLeaveChannelID = discordFile.getString("Discord.Player-Leave.Channel-ID");

            String consoleCommandsChannelID = discordFile.getString("Discord.Server-Side.Console-Commands.Channel-ID");

            String serverStartChannelID = discordFile.getString("Discord.Server-Side.Start.Channel-ID");

            String serverStopChannelID = discordFile.getString("Discord.Server-Side.Stop.Channel-ID");

            String ramChannelID = discordFile.getString("Discord.Server-Side.RAM.Channel-ID");


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

            if (!(consoleCommandsChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Server.Console-Commands") && !consoleCommandsChannelID.equals("LINK_HERE")) {

                consoleCommandsChannel = jda.getTextChannelById(consoleCommandsChannelID);

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
        }
    }

    public static void staffChat(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, staffChannel);

    }

    public static void playerChat(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerChatChannel);
    }

    public static void playerCommands(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerCommandsChannel);
    }

    public static void playerLogin(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLoginChannel);
    }

    public static void playerLeave(Player player, String content, boolean contentinAuthorLine) {

        discordUtil(player, content, contentinAuthorLine, playerLeaveChannel);
    }

    public static void console(String content, boolean contentinAuthorLine) {

        if (consoleCommandsChannel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentinAuthorLine) builder.setDescription(content);

        consoleCommandsChannel.sendMessage(builder.build()).queue();
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

    private static void discordUtil(Player player, String content, boolean contentinAuthorLine, TextChannel channel) {
        if (channel == null) return;

        EmbedBuilder builder = new EmbedBuilder().setAuthor(contentinAuthorLine ? content : player.getUsername(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentinAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (jda != null) {
            try {

                jda.shutdown();
                jda = null;
                DiscordStatus status = new DiscordStatus();
                status.getThreadPool().shutdown();
                Main.getInstance().getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Main.getInstance().getLogger().error("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        "If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }
    public JDA getJda(){ return jda; }
}
