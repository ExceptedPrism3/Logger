package com.carpour.loggervelocity.Events.PluginDependent.LiteBans;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Events.PluginDependent.LiteBans.Utils.UsernameFetcher;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.mysql.cj.Messages;
import litebans.api.Entry;
import litebans.api.Events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.loggervelocity.Utils.Data.*;

public class OnLiteBanEvents implements Runnable{

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
                            out.write(Messages.getString("Files.Extra.LiteBans").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%on%", onWho).replaceAll("%duration%", duration).replaceAll("%reason%", reason).replaceAll("%executor%", executorName).replaceAll("%silent%", String.valueOf(isSilent)).replaceAll("%command%", entryType.toUpperCase()) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord
                    if (!Messages.getString("Discord.Extra.LiteBans").isEmpty()) {

                        Discord.liteBans(Objects.requireNonNull(Messages.getString("Discord.Extra.LiteBans")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%on%", onWho).replaceAll("%duration%", duration).replaceAll("%reason%", reason).replaceAll("%executor%", executorName).replaceAll("%silent%", String.valueOf(isSilent)).replaceAll("%command%", entryType.toUpperCase()), false);

                    }

                    // External
                    if (isExternal && External.isConnected()) {

                        try {

                            ExternalData.liteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite
                    if (isSqlite && SQLite.isConnected()) {

                        try {

                            SQLiteData.insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
            }
        });
    }
}
