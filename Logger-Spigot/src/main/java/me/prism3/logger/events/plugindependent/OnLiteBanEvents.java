package me.prism3.logger.events.plugindependent;

import litebans.api.Entry;
import litebans.api.Events;
import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import me.prism3.logger.utils.liteban.UsernameFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.logger.utils.Data.*;


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

                final Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
                placeholders.put("%uuid%", playerUUID);
                placeholders.put("%executor%", executorName);
                placeholders.put("%executed_on%", onWho);
                placeholders.put("%reason%", reason);
                placeholders.put("%expiration%", duration);
                placeholders.put("%type%", entryType);
                placeholders.put("%silent%", String.valueOf(isSilent));

                // Log To Files
                if (Data.isLogToFiles)
                    main.getFileHandler().handleFileLog(LogCategory.LITEBANS, "Files.Extras.LiteBans", placeholders);

                // Discord Integration
                if (!player.hasPermission(loggerExemptDiscord) && main.getDiscordFile().get().getBoolean("Discord.Enable"))
                    main.getDiscord().handleDiscordLog("Discord.Extra.LiteBans", placeholders, DiscordChannels.LITE_BANS, executorName, player.getUniqueId());

                // External
                if (Data.isExternal) {

                    try {

                        Main.getInstance().getDatabase().getDatabaseQueue().queueLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getDatabase().getDatabaseQueue().queueLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }
            }
        });
    }
}