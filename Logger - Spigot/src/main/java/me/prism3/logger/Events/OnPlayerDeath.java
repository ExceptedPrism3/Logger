package me.prism3.logger.Events;

import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.*;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
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

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.Utils.Data.isPlayerDeathBackup;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();
    private final PlayerFolder playerDeathBackup = new PlayerFolder();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) throws IOException, InvalidConfigurationException {

        if (this.main.getConfig().getBoolean("Log-Player.Death")) {

            final Player player = event.getEntity();

            if (player.hasPermission(Data.loggerExempt)) return;

            // ******
            // This Part almost gave me brain tumor while figuring out how to make it
            if (isPlayerDeathBackup && this.playerDeathBackup.isAllowed(player)) {

                this.playerDeathBackup.create(event.getEntity());

                final File f1 = PlayerFolder.getPlayerFile(); // Gets the file location
                final FileConfiguration f = YamlConfiguration.loadConfiguration(f1); // Loads the file with Yaml functions
                f.load(f1); // Loads the file for use

                ItemStack[] invContent = Arrays.stream(event.getEntity().getInventory().getContents()) // Turn the contents array into a Stream.
                        .map(i -> i == null ? new ItemStack(Material.AIR) : i) // Map replaces the element with something else, so here if the item is null, we replace it with air, and if it isn't null, we set it to itself.
                        .toArray(ItemStack[]::new); // Turn it back into an array.

                ItemStack[] armorContent = Arrays.stream(event.getEntity().getInventory().getArmorContents()) // Turn the contents array into a Stream.
                        .map(i -> i == null ? new ItemStack(Material.AIR) : i) // Map replaces the element with something else, so here if the item is null, we replace it with air, and if it isn't null, we set it to itself.
                        .toArray(ItemStack[]::new); // Turn it back into an array.

                f.set("Inventory", InventoryToBase64.toBase64(invContent)); // Converts the Player's Inventory into base64
                f.set("Armor", InventoryToBase64.toBase64(armorContent)); // Converts the Player's Armor into base64
                f.save(f1); // Save and Closes the file after editing
            }
            // ******

            final World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();
            final int playerLevel = player.getLevel();
            String cause = Objects.requireNonNull(player.getLastDamageCause()).getCause().name().replace("\\", "\\\\");
            String killer = "";

            if (player.getKiller() != null) {

                if (player.getLastDamageCause().getEntity() instanceof Player) {
                    cause = "Player";
                }

                killer = player.getKiller().getName();

            }

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%level%", String.valueOf(playerLevel)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%level%", String.valueOf(playerLevel)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerDeath(Data.serverName, worldName, playerName, playerLevel, x, y, z, cause, killer, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerDeath(Data.serverName, player, cause, killer, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%level%", String.valueOf(playerLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%level%", String.valueOf(playerLevel)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death")).isEmpty()) {

                        Discord.playerDeath(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%level%", String.valueOf(playerLevel)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerDeath(Data.serverName, worldName, playerName, playerLevel, x, y, z, cause, killer, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerDeath(Data.serverName, player, cause, killer, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
