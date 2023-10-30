package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


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

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%TPS%", String.valueOf(this.getTPS()));

        // Log To Files
        if (Data.isLogToFiles) {
            if (this.getTPS() <= Data.tpsMedium) {
                this.main.getFileHandler().handleFileLog(LogCategory.TPS, "Files.Server-Side.TPS-Medium", placeholders);
            } else if (this.getTPS() <= Data.tpsCritical) {
                this.main.getFileHandler().handleFileLog(LogCategory.TPS, "Files.Server-Side.TPS-Critical", placeholders);
            }
        }

        // Discord
        if (this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {
            if (this.getTPS() <= Data.tpsMedium) {
                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.TPS-Medium", placeholders, DiscordChannels.TPS, "TPS Medium", null);
            } else if (this.getTPS() <= Data.tpsCritical) {
                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.TPS-Critical", placeholders, DiscordChannels.TPS, "TPS Critical", null);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                if (this.getTPS() <= Data.tpsMedium)
                    Main.getInstance().getDatabase().getDatabaseQueue().queueTps(Data.serverName, this.getTPS());

                else if (this.getTPS() <= Data.tpsCritical)
                    Main.getInstance().getDatabase().getDatabaseQueue().queueTps(Data.serverName, this.getTPS());

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                if (this.getTPS() <= Data.tpsMedium)
                    Main.getInstance().getDatabase().getDatabaseQueue().queueTps(Data.serverName, this.getTPS());

                else if (this.getTPS() <= Data.tpsCritical)
                    Main.getInstance().getDatabase().getDatabaseQueue().queueTps(Data.serverName, this.getTPS());

            } catch (final Exception e) {
                e.printStackTrace();
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