package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class TPS implements Runnable {

    private final Main main = Main.getInstance();

    private int tickCount = 0;
    private final long[] ticks = new long[600];

    public void run() {

        // This should resolve the uncorrected TPS value e.g 0.125462498
        if (this.getTPS() <= 1) return;

        if (Data.tpsMedium <= 0 || Data.tpsMedium >= 20 || Data.tpsCritical <= 0 || Data.tpsCritical >= 20) return;

        this.ticks[(this.tickCount % this.ticks.length)] = System.currentTimeMillis();

        this.tickCount += 1;

        // Log To Files
        if (Data.isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true))) {

                if (this.getTPS() <= Data.tpsMedium) {

                    out.write(this.main.getMessages().get().getString("Files.Server-Side.TPS-Medium").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");

                } else if (this.getTPS() <= Data.tpsCritical) {

                    out.write(this.main.getMessages().get().getString("Files.Server-Side.TPS-Critical").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())) + "\n");

                }

            } catch (final IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

            // Discord
            if (this.main.getDiscordFile().getBoolean("Discord.Enable")) {
                if (this.getTPS() <= Data.tpsMedium && !this.main.getMessages().get().getString("Discord.Server-Side.TPS-Medium").isEmpty())
                    this.main.getDiscord().tps(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Medium").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())), false);

                else if (this.getTPS() <= Data.tpsCritical && !this.main.getMessages().get().getString("Discord.Server-Side.TPS-Critical").isEmpty())
                    this.main.getDiscord().tps(this.main.getMessages().get().getString("Discord.Server-Side.TPS-Critical").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%TPS%", String.valueOf(getTPS())), false);
            }

            // External
            if (Data.isExternal) {

                try {

                    if (this.getTPS() <= Data.tpsMedium)
                        Main.getInstance().getDatabase().insertTps(Data.serverName, this.getTPS());

                    else if (this.getTPS() <= Data.tpsCritical)
                        Main.getInstance().getDatabase().insertTps(Data.serverName, this.getTPS());

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    if (this.getTPS() <= Data.tpsMedium)
                        Main.getInstance().getSqLite().insertTps(Data.serverName, this.getTPS());

                    else if (this.getTPS() <= Data.tpsCritical)
                        Main.getInstance().getSqLite().insertTps(Data.serverName, this.getTPS());

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }

    private double getTPS() { return getTPS(100); }

    private double getTPS(int ticks) {

        if (this.tickCount <= ticks) return 20.0D;
        int target = (this.tickCount - 1 - ticks) % this.ticks.length;
        long elapsed = System.currentTimeMillis() - this.ticks[target];
        return ticks / (elapsed / 1000.0D);
    }
}