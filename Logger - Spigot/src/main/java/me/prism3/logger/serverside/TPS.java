package me.prism3.logger.serverside;

import me.prism3.logger.discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Messages;
import me.prism3.logger.utils.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TPS implements Runnable {

    private final Main main = Main.getInstance();

    private int tickCount = 0;
    final private long[] TICKS = new long[600];

    public void run() {

        // This should resolve the uncorrected TPS value e.g 0.125462498
        if (getTPS() < 1 ) return;

        if (this.main.getConfig().getBoolean("Log-Server.TPS")) {

            if (Data.tpsMedium <= 0 || Data.tpsMedium >= 20 || Data.tpsCritical <= 0 || Data.tpsCritical >= 20) return;

            TICKS[(tickCount % TICKS.length)] = System.currentTimeMillis();

            tickCount += 1;

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    if (getTPS() <= Data.tpsMedium) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Medium")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    } else if (getTPS() <= Data.tpsCritical) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Critical")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    }

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                // Discord
                if (getTPS() <= Data.tpsMedium) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);

                    }

                } else if (getTPS() <= Data.tpsCritical) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);
                    }
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        if (getTPS() <= Data.tpsMedium) {

                            ExternalData.tps(Data.serverName, getTPS());

                        } else if (getTPS() <= Data.tpsCritical) {

                            ExternalData.tps(Data.serverName, getTPS());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        if (getTPS() <= Data.tpsMedium) {

                            SQLiteData.insertTPS(Data.serverName, getTPS());

                        } else if (getTPS() <= Data.tpsCritical) {

                            SQLiteData.insertTPS(Data.serverName, getTPS());
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