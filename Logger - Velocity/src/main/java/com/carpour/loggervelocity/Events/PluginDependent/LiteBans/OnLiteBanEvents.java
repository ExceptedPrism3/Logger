package com.carpour.loggervelocity.Events.PluginDependent.LiteBans;

import com.carpour.loggervelocity.Database.MySQL.MySQL;
import com.carpour.loggervelocity.Database.MySQL.MySQLData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Events.PluginDependent.LiteBans.Utils.UsernameFetcher;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.mysql.cj.Messages;
import com.velocitypowered.api.proxy.Player;
import litebans.api.Entry;
import litebans.api.Events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnLiteBanEvents implements Runnable{

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                final Main main = Main.getInstance();

                for (Player player : Main.getServer().getAllPlayers()) {

                    if (player.hasPermission("loggerproxy.exempt")) return;

                }

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

                            Main.getInstance().getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord Integration
                    if (!Messages.getString("Discord.Extra.LiteBans").isEmpty()) {

                        Discord.liteBans(Objects.requireNonNull(Messages.getString("Discord.Extra.LiteBans")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%on%", onWho).replaceAll("%duration%", duration).replaceAll("%reason%", reason).replaceAll("%executor%", executorName).replaceAll("%silent%", String.valueOf(isSilent)).replaceAll("%command%", entryType.toUpperCase()), false);

                    }

                    // MySQL Handling
                    if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                        try {

                            MySQLData.liteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite Handling
                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        try {

                            SQLiteData.insertLiteBans(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

                        } catch (Exception exception) { exception.printStackTrace(); }
                    }
                }
            }
        });
    }
}
