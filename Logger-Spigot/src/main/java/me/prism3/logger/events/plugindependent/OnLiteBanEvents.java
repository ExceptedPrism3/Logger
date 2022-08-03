package me.prism3.logger.events.plugindependent;

import litebans.api.Entry;
import litebans.api.Events;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.litebansutil.UsernameFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;


public class OnLiteBanEvents implements Listener, Runnable {

    private final Main main = Main.getInstance();

    @Override
    public void run() {

        Events.get().register(new Events.Listener() {

            @Override
            public void entryAdded(Entry entry) {

                if (main.getConfig().getBoolean("Log-Extras.LiteBans")) {

                    final String entryType = entry.getType().toUpperCase();
                    final String executorName = entry.getExecutorName();
                    final String duration = entry.getDurationString();
                    final String onWho = UsernameFetcher.playerNameFetcher(entry.getUuid());
                    final String reason = entry.getReason();
                    final boolean isSilent = entry.isSilent();{

                        assert executorName != null;
                        final Player player = Bukkit.getPlayer(executorName);
                        assert player != null;

                        // Log To Files
                        if (Data.isLogToFiles) {

                            if (Data.isStaffEnabled) {
                                if (player.hasPermission(Data.loggerStaffLog)) {

                                    if (!Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).isEmpty()) {

                                        main.getDiscord().staffChat(player, Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)), false);

                                    }

                                    try {

                                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                        out.write(Objects.requireNonNull(main.getMessages().get().getString("Files.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)) + "\n");
                                        out.close();

                                    } catch (IOException e) {

                                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                        e.printStackTrace();

                                    }

                                    if (Data.isExternal ) {

                                        Main.getInstance().getDatabase().insertLitebans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                                    }

                                    if (Data.isSqlite ) {

                                        Main.getInstance().getSqLite().insertLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                                    }

                                    return;

                                }
                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLiteBansFile(), true));
                                out.write(Objects.requireNonNull(main.getMessages().get().getString("Files.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }
                        }

                        // Discord Integration
                        if (player == null) { // This is essential when performed by the console

                            if (!Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).isEmpty()) {

                                main.getDiscord().advancedBan(Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)), false);
                            }
                        } else
                            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                if (!Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).isEmpty()) {

                                    main.getDiscord().staffChat(player, Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)), false);

                                }
                            } else {
                                if (!Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).isEmpty()) {

                                    main.getDiscord().advancedBan(Objects.requireNonNull(main.getMessages().get().getString("Discord.Extras.LiteBans")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executorName).replace("%executed_on%", onWho).replace("%reason%", reason).replace("%expiration%", duration).replace("%type%", entryType).replace("%silent%", String.valueOf(isSilent)), false);
                                }
                            }
                        }

                        // External
                        if (Data.isExternal ) {

                            try {

                                Main.getInstance().getDatabase().insertLitebans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                            } catch (Exception e) { e.printStackTrace(); }
                        }

                        // SQLite
                        if (Data.isSqlite ) {

                            try {

                                Main.getInstance().getSqLite().insertLiteBans(Data.serverName, executorName, entryType, onWho, duration, reason, isSilent);

                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    }

                }
            }
        });
    }
}