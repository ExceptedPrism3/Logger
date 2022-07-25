package me.prism3.loggervelocity.events.plugindependent.litebans;

import com.mysql.cj.Messages;
import litebans.api.Entry;
import litebans.api.Events;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.utils.litebansutil.UsernameFetcher;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLiteBanEvents implements Runnable {

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                final Main main = Main.getInstance();

                if (main.getConfig().getBoolean("Log-Extra.LiteBans")) {

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

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(fileLog, true));
                            out.write(Messages.getString("Files.Extra.LiteBans").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%on%", onWho).replace("%duration%", duration).replace("%reason%", reason).replace("%executor%", executorName).replace("%silent%", String.valueOf(isSilent)).replace("%command%", entryType.toUpperCase()) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord
                    if (!Messages.getString("Discord.Extra.LiteBans").isEmpty()) {

                        main.getDiscord().liteBans(Objects.requireNonNull(Messages.getString("Discord.Extra.LiteBans")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%on%", onWho).replace("%duration%", duration).replace("%reason%", reason).replace("%executor%", executorName).replace("%silent%", String.valueOf(isSilent)).replace("%command%", entryType.toUpperCase()), false);

                    }

                    // External
                    if (isExternal && main.getExternal().isConnected()) {

                        try {

                            ExternalData.liteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite
                    if (isSqlite && main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
            }
        });
    }
}
