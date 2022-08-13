package me.prism3.logger.events.oncommands;

import com.carpour.loggercore.database.entity.EntityPlayer;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWhitelistedCommand(final PlayerCommandPreprocessEvent event) {

        final Player player = event.getPlayer();
        final World world = player.getWorld();
        final String worldName = world.getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String command = event.getMessage().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));
        
        final EntityPlayer entityPlayer = new EntityPlayer(playerName, playerUUID.toString());

        for (String m : Data.commandsToLog) {

            if (commandParts.get(0).equalsIgnoreCase(m)) {

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                            out.write(this.main.getMessages().get().getString("Files.Player-Commands-Whitelisted-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    } else {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write(this.main.getMessages().get().getString("Files.Player-Commands-Whitelisted").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }
                }

                // Discord
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted-Staff").isEmpty()) {

                            this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Player-Commands-Staff-Whitelisted").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command), false);

                        }
                    } else {

                        if (this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted").isEmpty()) {

                            this.main.getDiscord().playerCommand(player, this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command), false);
                        }
                    }
                }

                // External
                if (Data.isExternal) {

                    try {

                        Main.getInstance().getDatabase().insertPlayerCommands(Data.serverName, entityPlayer, worldName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getSqLite().insertPlayerCommands(Data.serverName, entityPlayer, worldName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
