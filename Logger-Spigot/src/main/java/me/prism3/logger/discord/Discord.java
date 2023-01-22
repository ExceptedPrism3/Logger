package me.prism3.logger.discord;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Discord {

    private final Main main = Main.getInstance();

    private DiscordStatus discordStatus;

    private Map<DiscordChannels, TextChannel> channels;

    private JDA jda;

    public void run() {
        if (this.main.getDiscordFile().getBoolean("Discord.Enable")) {
            try {
                this.jda = connectToBot();
                this.createChannelsMap(this.jda);

                if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled"))
                    this.discordStatus = new DiscordStatus(this.jda);

            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (final Exception e) {
                Log.severe("An error has occurred whilst connecting to the Bot. Is the Bot Key Valid?");
                e.printStackTrace();
            }
        }
    }

    private JDA connectToBot() throws LoginException, InterruptedException {
        final String botToken = this.main.getDiscordFile().getString("Discord.Bot-Token");
        return JDABuilder.createDefault(botToken).build().awaitReady();
    }

    private void createChannelsMap(JDA jda) {

        this.channels = new EnumMap<>(DiscordChannels.class);

        for (final DiscordChannels channel : DiscordChannels.values()) {

            final String channelId = this.main.getDiscordFile().getString(channel.getKey());

            if (channelId == null || channelId.isEmpty() || channelId.equalsIgnoreCase("CHANNEL_ID"))
                continue;

            try {
                final long id = Long.parseLong(channelId);
                this.channels.put(channel, jda.getTextChannelById(id));
            } catch (final NumberFormatException e) {
                Log.severe("The channel id for " + channel + " is not a valid Discord ID!");
            }
        }
    }

    public void handleDiscordLog(String messagePath, Map<String, String> placeholders, DiscordChannels channel,
                                 String author, UUID playerUUID) {

        final TextChannel textChannel = channels.get(channel);

        if (textChannel == null)
            return;

        String discordMessage = this.main.getMessages().get().getString(messagePath);

        if (discordMessage == null) {
            Log.severe("Invalid message path: " + messagePath +
                    ", make sure that it's not removed or renamed. If the issue persists re-create the discord.yml file");
            return;
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet())
            discordMessage = discordMessage.replace(entry.getKey(), entry.getValue());

        final EmbedBuilder builder = new EmbedBuilder().setAuthor(author);

        if (playerUUID != null)
            builder.setAuthor(author, null, "https://crafatar.com/avatars/" + playerUUID + "?overlay=1");

        builder.setDescription(discordMessage);

        textChannel.sendMessage(builder.build()).queue();
    }

    public void disconnect() {

        this.jda.shutdownNow();
        this.jda = null;

        if (this.main.getDiscordFile().getBoolean("ActivityCycling.Enabled") && this.discordStatus != null)
            this.discordStatus.shutdownThreadPool();

        Log.info("Discord Bot Bridge has been closed!");
    }
}
