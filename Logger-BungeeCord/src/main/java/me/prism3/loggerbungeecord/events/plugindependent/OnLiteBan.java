package me.prism3.loggerbungeecord.events.plugindependent;

import litebans.api.Entry;
import litebans.api.Events;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import me.prism3.loggerbungeecord.utils.liteban.UsernameFetcher;
import net.md_5.bungee.api.plugin.Listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class OnLiteBan implements Listener, Runnable {

    private final Main main = Main.getInstance();

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                final String entryType = entry.getType().toLowerCase();
                final String executorName = entry.getExecutorName();
                final String duration = entry.getDurationString();
                final String uuid = entry.getUuid();
                final String onWho = UsernameFetcher.playerNameFetcher(uuid);
                final String reason = entry.getReason();
                final boolean isSilent = entry.isSilent();

                File fileLog = null;

                switch (entryType) {

                    case "ban":

                        if (!isLiteBansIpBan) return;
                        if (!isLiteBansTempIpBan) return;
                        if (!isLiteBansBan) return;
                        if (!isLiteBansTempBan) return;

                        fileLog = FileHandler.getLiteBansBansLogFile();

                        break;

                    case "mute":

                        if (!isLiteBansMute) return;
                        if (!isLiteBansTempMute) return;

                        fileLog = FileHandler.getLiteBansMuteLogFile();

                        break;

                    case "kick":

                        if (!isLiteBansKick) return;

                        fileLog = FileHandler.getLiteBansKickLogFile();

                        break;

                    default:
                        break;

                }

                assert executorName != null;

                // Log To Files
                if (isLogToFiles) {

                    assert fileLog != null;

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(fileLog, true))) {

                        out.write(main.getMessages().getString("Files.Extras.LiteBans").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%on%", onWho).replace("%duration%", duration).replace("%reason%", reason).replace("%executor%", executorName).replace("%silent%", String.valueOf(isSilent)).replace("%command%", entryType.toUpperCase()) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!main.getMessages().getString("Discord.Extras.LiteBans").isEmpty())
                    main.getDiscord().liteBans(main.getMessages().getString("Discord.Extras.LiteBans").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%on%", onWho).replace("%duration%", duration).replace("%reason%", reason).replace("%executor%", executorName).replace("%silent%", String.valueOf(isSilent)).replace("%command%", entryType.toUpperCase()), false);

                // External
                if (isExternal) {

                    try {

                        Main.getInstance().getDatabase().insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite) {

                    try {

                        Main.getInstance().getSqLite().insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                    } catch (final Exception e) { e.printStackTrace(); }
                }
            }
        });
    }
}
