package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class ServerAddress implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerAddress(final PreLoginEvent event) {

        if (!event.isCancelled()) {

            final String playerName = event.getConnection().getName();
            final String address = event.getConnection().getVirtualHost().getHostString() + ":" +
                    event.getConnection().getVirtualHost().getPort();

            // Log To Files
            if (Data.isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerAddressLogFile(), true))) {

                    out.write(this.main.getMessages().getString("Files.Server-Side.Server-Address").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%address%", address) + "\n");

                } catch (final IOException e) {

                    Log.severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }

            // Discord
            if (!this.main.getMessages().getString("Discord.Server-Side.Server-Address").isEmpty() && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().serverAddress(this.main.getMessages().getString("Discord.Server-Side.Server-Address").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%address%", address), false);

            // External
            if (Data.isExternal) {

                try {

//                        Main.getInstance().getDatabase().insertServerStart(Data.serverName);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

//                        Main.getInstance().getSqLite().insertServerStart(Data.serverName);
//TODO DB
                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
