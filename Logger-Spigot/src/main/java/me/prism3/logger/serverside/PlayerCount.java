package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.Bukkit;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.logger.utils.Data.playerCountNumber;

public class PlayerCount implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        int players = Bukkit.getServer().getOnlinePlayers().size();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%amount%", String.valueOf(playerCountNumber));

        if (players >= playerCountNumber) {

            // Log To Files
            if (Data.isLogToFiles)
                FileHandler.handleFileLog("Files.Server-Side.Player-Count", placeholders, FileHandler.getPlayerCountFile());

            // Discord
            if (this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Player-Count", placeholders, DiscordChannels.PLAYER_COUNT, "Player Count", null);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCount(Data.serverName, playerCountNumber);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCount(Data.serverName, playerCountNumber);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}