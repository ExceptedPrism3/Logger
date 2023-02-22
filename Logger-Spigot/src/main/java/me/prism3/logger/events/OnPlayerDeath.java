package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.db.PlayerInventoryDB;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;
import static me.prism3.logger.utils.Data.isStaffEnabled;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) {

        final Player player = event.getEntity();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        // ******
        // Player Inventory Backup Part
        if (isPlayerDeathBackup && PlayerInventoryDB.isAllowed(player.getUniqueId().toString())) {

            final ItemStack[] invContent = Arrays.stream(event.getEntity().getInventory().getContents()) // Turn the contents array into a Stream.
                    .map(i -> i == null ? new ItemStack(Material.AIR) : i) // Map replaces the element with something else, so here if the item is null, we replace it with air, and if it isn't null, we set it to itself.
                    .toArray(ItemStack[]::new); // Turn it back into an array.

            final ItemStack[] armorContent = Arrays.stream(event.getEntity().getInventory().getArmorContents())
                    .map(i -> i == null ? new ItemStack(Material.AIR) : i)
                    .toArray(ItemStack[]::new);

            PlayerInventoryDB.insertInventory(player.getUniqueId().toString(),
                    player.getName(),
                    player.getWorld().getName(),
                    player.getLastDamageCause().getCause().name() != null ? player.getLastDamageCause().getCause().name() : "",
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    player.getTotalExperience(),
                    invContent,
                    armorContent);
        }
        // ******

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();
        final int playerLevel = player.getLevel();
        String cause = player.getLastDamageCause().getCause().name().replace("\\", "\\\\");
        String killer = "";

        if (player.getKiller() != null) {

            if (player.getLastDamageCause().getEntity() instanceof Player)
                cause = "Player";

            killer = player.getKiller().getName();
        }

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%cause%", cause);
        placeholders.put("%killer%", killer);
        placeholders.put("%level%", String.valueOf(playerLevel));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Death-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Death", placeholders, FileHandler.getPlayerDeathLogFile());
            }
        }

        // Discord
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Death-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Death", placeholders, DiscordChannels.PLAYER_DEATH, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerDeath(Data.serverName, playerName, playerUUID.toString(), playerLevel, cause, killer, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerDeath(Data.serverName, playerName, playerUUID.toString(), playerLevel, cause, killer, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
