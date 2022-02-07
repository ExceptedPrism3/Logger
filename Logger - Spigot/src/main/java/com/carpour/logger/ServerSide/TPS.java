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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TPS implements Runnable {

    private final Main main = Main.getInstance();

    public static int tickCount = 0;
    public static long[] TICKS = new long[600];
    String serverName = main.getConfig().getString("Server-Name");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(int ticks) {

        if (tickCount <= ticks) return 20.0D;
        int target = (tickCount - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[target];
        return ticks / (elapsed / 1000.0D);

    }

    public void run() {

        if (main.getConfig().getBoolean("Log-Server.TPS")) {

            if (main.getConfig().getInt("TPS.Value-Medium") <= 0 || main.getConfig().getInt("TPS.Value-Medium") >= 20 ||
                    main.getConfig().getInt("TPS.Value-Critical") <= 0 || main.getConfig().getInt("TPS.Value-Critical") >= 20) {

                return;

            }

            TICKS[(tickCount % TICKS.length)] = System.currentTimeMillis();

            tickCount += 1;

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    if (getTPS() <= main.getConfig().getInt("TPS.Value-Medium")) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Medium")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    } else if (getTPS() <= main.getConfig().getInt("TPS.Value-Critical")) {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getTPSLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.TPS-Critical")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())) + "\n");
                        out.close();

                    }

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                // Discord
                if (getTPS() <= main.getConfig().getInt("TPS.Value-Medium")) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Medium")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);

                    }

                } else if (getTPS() <= main.getConfig().getInt("TPS.Value-Critical")) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).isEmpty()) {

                        Discord.TPS(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.TPS-Critical")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%TPS%", String.valueOf(getTPS())), false);
                    }
                }

                // MySQL
                if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                    try {

                        if (getTPS() <= main.getConfig().getInt("TPS.Value-Medium")) {

                            ExternalData.TPS(serverName, getTPS());

                        } else if (getTPS() <= main.getConfig().getInt("TPS.Value-Critical")) {

                            ExternalData.TPS(serverName, getTPS());
                        }

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        if (getTPS() <= main.getConfig().getInt("TPS.Value-Medium")) {

                            SQLiteData.insertTPS(serverName, getTPS());

                        } else if (getTPS() <= main.getConfig().getInt("TPS.Value-Critical")) {

                            SQLiteData.insertTPS(serverName, getTPS());
                        }

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}