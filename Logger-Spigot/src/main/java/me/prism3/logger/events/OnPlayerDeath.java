package me.prism3.logger.events;

import com.carpour.loggercore.database.entity.Coordinates;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import me.prism3.logger.utils.playerdeathutils.PlayerFolder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import static me.prism3.logger.utils.Data.isPlayerDeathBackup;
import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();
    private final PlayerFolder playerDeathBackup = new PlayerFolder();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) throws IOException, InvalidConfigurationException {

        final Player player = event.getEntity();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        // ******
        // Player Inventory Backup Part
        if (isPlayerDeathBackup && this.playerDeathBackup.isAllowed(player)) {

            this.playerDeathBackup.create(event.getEntity());

            final File f1 = playerDeathBackup.getPlayerFile(); // Gets the file location
            final FileConfiguration f = YamlConfiguration.loadConfiguration(f1); // Loads the file with Yaml functions
            f.load(f1); // Loads the file for use

            final ItemStack[] invContent = Arrays.stream(event.getEntity().getInventory().getContents()) // Turn the contents array into a Stream.
                    .map(i -> i == null ? new ItemStack(Material.AIR) : i) // Map replaces the element with something else, so here if the item is null, we replace it with air, and if it isn't null, we set it to itself.
                    .toArray(ItemStack[]::new); // Turn it back into an array.

            final ItemStack[] armorContent = Arrays.stream(event.getEntity().getInventory().getArmorContents()) // Turn the contents array into a Stream.
                    .map(i -> i == null ? new ItemStack(Material.AIR) : i) // Map replaces the element with something else, so here if the item is null, we replace it with air, and if it isn't null, we set it to itself.
                    .toArray(ItemStack[]::new); // Turn it back into an array.

            f.set("inventory", InventoryToBase64.toBase64(invContent)); // Converts the Player's Inventory into base64
            f.set("armor", InventoryToBase64.toBase64(armorContent)); // Converts the Player's Armor into base64
            f.save(f1); // Save and Closes the file after editing
        }
        // ******

        final World world = player.getWorld();
        final String worldName = world.getName();
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

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write(this.main.getMessages().get().getString("Files.Player-Death-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%cause%", cause).replace("%killer%", killer).replace("%level%", String.valueOf(playerLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                    out.write(this.main.getMessages().get().getString("Files.Player-Death").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%cause%", cause).replace("%killer%", killer).replace("%level%", String.valueOf(playerLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Player-Death-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Death-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%cause%", cause).replace("%killer%", killer).replace("%level%", String.valueOf(playerLevel)), false);

                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Player-Death").isEmpty()) {

                    this.main.getDiscord().playerDeath(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Death").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%cause%", cause).replace("%killer%", killer).replace("%level%", String.valueOf(playerLevel)), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertPlayerDeath(Data.serverName, playerName, playerUUID.toString(), playerLevel, cause, killer, coordinates, player.hasPermission(loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertPlayerDeath(Data.serverName, playerName, playerUUID.toString(), playerLevel, cause, killer, coordinates, player.hasPermission(loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
