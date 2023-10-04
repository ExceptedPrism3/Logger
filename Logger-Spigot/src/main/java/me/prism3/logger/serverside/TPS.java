package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TPS implements Runnable {

    private final Main main = Main.getInstance();

    private int tickCount = 0;
    private final long[] ticks = new long[600];

    public void run() {

        // This should resolve the uncorrected TPS value e.g 0.125462498
        if (this.getTPS() <= 1 ) return;

        if (this.main.getConfig().getBoolean("Log-Server.TPS")) {

            if (Data.tpsMedium <= 0 || Data.tpsMedium >= 20 || Data.tpsCritical <= 0 || Data.tpsCritical >= 20) return;

            this.ticks[(this.tickCount % this.ticks.length)] = System.currentTimeMillis();

            this.tickCount += 1;

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    if (this.getTPS() <= Data.tpsMedium) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.TPS-Medium")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    } else if (this.getTPS() <= Data.tpsCritical) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.TPS-Critical")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    }

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                // Discord
                if (this.getTPS() <= Data.tpsMedium) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Medium")).isEmpty()) {

                        this.main.getDiscord().tps(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Medium")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())), false);

                    }

                } else if (this.getTPS() <= Data.tpsCritical) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Critical")).isEmpty()) {

                        this.main.getDiscord().tps(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Critical")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())), false);
                    }
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        if (this.getTPS() <= Data.tpsMedium) {

                            ExternalData.tps(Data.serverName, this.getTPS());

                        } else if (this.getTPS() <= Data.tpsCritical) {

                            ExternalData.tps(Data.serverName, this.getTPS());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        if (this.getTPS() <= Data.tpsMedium) {

                            SQLiteData.insertTPS(Data.serverName, this.getTPS());

                        } else if (this.getTPS() <= Data.tpsCritical) {

                            SQLiteData.insertTPS(Data.serverName, this.getTPS());
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }

    private double getTPS() {
        return getTPS(100);
    }

    private double getTPS(int ticks) {

        if (this.tickCount <= ticks) return 20.0D;
        int target = (this.tickCount - 1 - ticks) % this.ticks.length;
        long elapsed = System.currentTimeMillis() - this.ticks[target];
        return ticks / (elapsed / 1000.0D);

    }
}