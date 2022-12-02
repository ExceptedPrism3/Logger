package me.prism3.logger.events.plugindependent;

import litebans.api.Entry;
import litebans.api.Events;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.liteban.UsernameFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class OnLiteBanEvents implements Listener, Runnable {

    private final Main main = Main.getInstance();

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                final String entryType = entry.getType().toUpperCase();
                final String executorName = entry.getExecutorName();
                final String duration = entry.getDurationString();
                final String onWho = UsernameFetcher.playerNameFetcher(entry.getUuid());
                final String reason = entry.getReason();
                final boolean isSilent = entry.isSilent();

                final Player player = Bukkit.getPlayer(executorName);
                final String playerUUID = player == null ? "" : player.getUniqueId().toString();

                // Log To Files
                if (Data.isLogToFiles) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLiteBansFile(), true))) {

                        out.write(main.getMessages().get().getString("Files.Extras.LiteBans").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)).replace("%uuid%", playerUUID) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }

                // Discord Integration
                if (main.getDiscordFile().getBoolean("Discord.Enable") && !main.getMessages().get().getString("Discord.Extras.LiteBans").isEmpty())
                    main.getDiscord().liteBans(main.getMessages().get().getString("Discord.Extras.LiteBans").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", String.valueOf(duration)).replace("%type%", entryType).replace("%uuid%", playerUUID).replace("%silent%", String.valueOf(isSilent)), false);

                // External
                if (Data.isExternal) {

                    try {

                        Main.getInstance().getQueueManager().queueLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getQueueManager().queueLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }
            }
        });
    }
}