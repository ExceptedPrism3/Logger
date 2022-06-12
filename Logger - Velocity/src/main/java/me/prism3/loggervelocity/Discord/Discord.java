package me.prism3.loggervelocity.Discord;

import me.prism3.loggervelocity.Main;
import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import static me.prism3.loggervelocity.Utils.Data.isStaffEnabled;

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

    private static TextChannel liteBansChannel;

    public void run() {

        DiscordFile discordFile = new DiscordFile();

        if (discordFile.getBoolean("Discord.Enable")) {

            final String botToken = discordFile.getString("Discord.Bot-Token");

            try {

                jda = JDABuilder.createDefault(botToken).build().awaitReady();
                if (DiscordFile.get().getBoolean("ActivityCycling.Enabled")) new DiscordStatus();

            } catch (Exception e) {

                Main.getInstance().getLogger().error("An error has occurred whilst connecting to the Bot." +
                        " Is the Bot Key Valid?");
                return;

            }

            final String staffChannelID = discordFile.getString("Discord.Staff.Channel-ID");

            final String playerChatChannelID = discordFile.getString("Discord.Player-Chat.Channel-ID");

            final String playerCommandsChannelID = discordFile.getString("Discord.Player-Commands.Channel-ID");

            final String playerLoginChannelID = discordFile.getString("Discord.Player-Login.Channel-ID");

            final String playerLeaveChannelID = discordFile.getString("Discord.Player-Leave.Channel-ID");

            final String consoleCommandsChannelID = discordFile.getString("Discord.Server-Side.Console-Commands.Channel-ID");

            final String serverStartChannelID = discordFile.getString("Discord.Server-Side.Start.Channel-ID");

            final String serverStopChannelID = discordFile.getString("Discord.Server-Side.Stop.Channel-ID");

            final String ramChannelID = discordFile.getString("Discord.Server-Side.RAM.Channel-ID");

            final String liteBansChannelID = discordFile.getString("Discord.Extra.LiteBans.Channel-ID");

            try {

                if (!(staffChannelID.isEmpty()) && isStaffEnabled && !staffChannelID.equals("LINK_HERE")) {

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

                if (!(liteBansChannelID.isEmpty()) && main.getConfig().getBoolean("Log-Extra.LiteBans") && !liteBansChannelID.equals("LINK_HERE")) {

                    liteBansChannel = jda.getTextChannelById(liteBansChannelID);

                }
            }catch (Exception e){

                main.getLogger().error("A Discord Channel ID is not Valid. Discord Logging Features has been Disabled.");

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

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Console");

        if (!contentinAuthorLine) builder.setDescription(content);

        consoleCommandsChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStart(String content, boolean contentinAuthorLine) {

        if (serverStartChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Start");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStartChannel.sendMessage(builder.build()).queue();
    }

    public static void serverStop(String content, boolean contentinAuthorLine) {

        if (serverStopChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Server Stop");

        if (!contentinAuthorLine) builder.setDescription(content);

        serverStopChannel.sendMessage(builder.build()).queue();
    }

    public static void ram(String content, boolean contentinAuthorLine) {

        if (ramChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("RAM");

        if (!contentinAuthorLine) builder.setDescription(content);

        ramChannel.sendMessage(builder.build()).queue();
    }

    public static void liteBans(String content, boolean contentinAuthorLine) {

        if (liteBansChannel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("LiteBans");

        if (!contentinAuthorLine) builder.setDescription(content);

        liteBansChannel.sendMessage(builder.build()).queue();
    }

    private static void discordUtil(Player player, String content, boolean contentinAuthorLine, TextChannel channel) {
        if (channel == null) return;

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(contentinAuthorLine ? content : player.getUsername(),
                null, "https://crafatar.com/avatars/" + player.getUniqueId() + "?overlay=1");

        if (!contentinAuthorLine) builder.setDescription(content);

        channel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        if (jda != null) {
            try {

                jda.shutdown();
                jda = null;
                if (DiscordFile.get().getBoolean("ActivityCycling.Enabled")) DiscordStatus.getThreadPool().shutdown();
                Main.getInstance().getLogger().info("Discord Bot Bridge has been closed!");

            } catch (Exception e) {

                Main.getInstance().getLogger().error("The Connection between the Server and the Discord Bot didn't Shutdown down Safely." +
                        " If this Issue Persists, Contact the Authors!");

                e.printStackTrace();

            }
        }
    }
    public JDA getJda(){ return jda; }
}
