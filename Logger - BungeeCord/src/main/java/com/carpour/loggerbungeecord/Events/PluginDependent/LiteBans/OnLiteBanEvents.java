package com.carpour.loggerbungeecord.Events.PluginDependent.LiteBans;

import com.carpour.loggerbungeecord.Database.External.ExternalData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Events.PluginDependent.LiteBans.Utils.UsernameFetcher;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import litebans.api.Entry;
import litebans.api.Events;
import net.md_5.bungee.api.plugin.Listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnLiteBanEvents implements Listener, Runnable{

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                final Main main = Main.getInstance();

                String entryType = entry.getType().toLowerCase();
                String executorName = entry.getExecutorName();
                String duration = entry.getDurationString();
                String uuid = entry.getUuid();
                String onWho = UsernameFetcher.playerNameFetcher(uuid);
                String reason = entry.getReason();
                boolean isSilent = entry.isSilent();
                String serverName = main.getConfig().getString("Server-Name");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                File fileLog = null;

                if (main.getConfig().getBoolean("Log-Extra.LiteBans")) {

                    switch (entryType) {

                        case "ban":

                            if (!main.getConfig().getBoolean("LiteBans.IP-Ban")) return;
                            if (!main.getConfig().getBoolean("LiteBans.Temp-IP-Ban")) return;
                            if (!main.getConfig().getBoolean("LiteBans.Ban")) return;
                            if (!main.getConfig().getBoolean("LiteBans.Temp-Ban")) return;

                            fileLog = FileHandler.getLiteBansBansLogFile();

                            break;

                        case "mute":

                            if (!main.getConfig().getBoolean("LiteBans.Mute")) return;
                            if (!main.getConfig().getBoolean("LiteBans.Temp-Mute")) return;

                            fileLog = FileHandler.getLiteBansMuteLogFile();

                            break;

                        case "kick":

                            if (!main.getConfig().getBoolean("LiteBans.Kick")) return;

                            fileLog = FileHandler.getLiteBansKickLogFile();

                            break;

                        default:
                            break;

                    }

                    assert executorName != null;

                    // Log To Files Handling
                    if (main.getConfig().getBoolean("Log-to-Files")) {

                        assert fileLog != null;

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(fileLog, true));
                            out.write(Messages.getString("Files.Extra.LiteBans").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%on%", onWho).replaceAll("%duration%", duration).replaceAll("%reason%", reason).replaceAll("%executor%", executorName).replaceAll("%silent%", String.valueOf(isSilent)).replaceAll("%command%", entryType.toUpperCase()) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord Integration
                    if (!Messages.getString("Discord.Extra.LiteBans").isEmpty()) {

                        Discord.liteBans(Objects.requireNonNull(Messages.getString("Discord.Extra.LiteBans")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%on%", onWho).replaceAll("%duration%", duration).replaceAll("%reason%", reason).replaceAll("%executor%", executorName).replaceAll("%silent%", String.valueOf(isSilent)).replaceAll("%command%", entryType.toUpperCase()), false);

                    }

                    // MySQL Handling
                    if (main.getConfig().getBoolean("External.Enable") && main.external.isConnected()) {

                        try {

                            ExternalData.liteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }

                    // SQLite Handling
                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }
        });
    }
}
