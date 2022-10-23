package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.logger.utils.Data.playerCountNumber;

public class PlayerCount implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        int players = Bukkit.getServer().getOnlinePlayers().size();

        if (players >= playerCountNumber) {

            // Log To Files
            if (Data.isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCountFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Server-Side.Player-Count").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%amount%", String.valueOf(playerCountNumber)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }

            // Discord
            if (!this.main.getMessages().get().getString("Discord.Server-Side.Player-Count").isEmpty() && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().playerCount(this.main.getMessages().get().getString("Discord.Server-Side.Player-Count").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%amount%", String.valueOf(playerCountNumber)), false);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getQueueManager().queuePlayerCount(Data.serverName, playerCountNumber);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getQueueManager().queuePlayerCount(Data.serverName, playerCountNumber);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}