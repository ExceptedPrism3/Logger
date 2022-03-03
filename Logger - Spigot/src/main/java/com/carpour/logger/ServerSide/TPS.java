package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class TPS implements Runnable {

    private final Main main = Main.getInstance();

    private int tickCount = 0;
    final private long[] TICKS = new long[600];

    public void run() {

        // This should resolve the uncorrected TPS value e.g 0.125462498
        if (getTPS() < 1 ) return;

        if (this.main.getConfig().getBoolean("Log-Server.TPS")) {

            if (tpsMedium <= 0 || tpsMedium >= 20 || tpsCritical <= 0 || tpsCritical >= 20) return;

            TICKS[(tickCount % TICKS.length)] = System.currentTimeMillis();

            tickCount += 1;

            // Log To Files
            if (isLogToFiles) {

                try {

                    if (getTPS() <= tpsMedium) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Medium")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    } else if (getTPS() <= tpsCritical) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Critical")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    }

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                // Discord
                if (getTPS() <= tpsMedium) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);

                    }

                } else if (getTPS() <= tpsCritical) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);
                    }
                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        if (getTPS() <= tpsMedium) {

                            ExternalData.TPS(serverName, getTPS());

                        } else if (getTPS() <= tpsCritical) {

                            ExternalData.TPS(serverName, getTPS());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        if (getTPS() <= tpsMedium) {

                            SQLiteData.insertTPS(serverName, getTPS());

                        } else if (getTPS() <= tpsCritical) {

                            SQLiteData.insertTPS(serverName, getTPS());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }

    public double getTPS() {
        return getTPS(100);
    }

    public double getTPS(int ticks) {

        if (tickCount <= ticks) return 20.0D;
        int target = (tickCount - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[target];
        return ticks / (elapsed / 1000.0D);

    }
}