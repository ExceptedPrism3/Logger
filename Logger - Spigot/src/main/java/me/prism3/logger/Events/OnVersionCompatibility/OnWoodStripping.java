package me.prism3.logger.events.onversioncompatibility;

import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OnWoodStripping implements Listener {

    private final Main main = Main.getInstance();
    private final List<Material> logs;
    private final List<Material> axes;

    public OnWoodStripping() {

        logs = new ArrayList<>();
        axes = new ArrayList<>();

        logs.add(Material.ACACIA_LOG);
        logs.add(Material.BIRCH_LOG);
        logs.add(Material.DARK_OAK_LOG);
        logs.add(Material.JUNGLE_LOG);
        logs.add(Material.SPRUCE_LOG);
        logs.add(Material.OAK_LOG);
        logs.add(Material.ACACIA_WOOD);
        logs.add(Material.BIRCH_WOOD);
        logs.add(Material.DARK_OAK_WOOD);
        logs.add(Material.JUNGLE_WOOD);
        logs.add(Material.SPRUCE_WOOD);
        logs.add(Material.OAK_WOOD);

        axes.add(Material.WOODEN_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.GOLDEN_AXE);
        axes.add(Material.DIAMOND_AXE);
        if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_16_R1))
            axes.add(Material.NETHERITE_AXE);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onWoodStripped(final PlayerInteractEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Version-Exceptions.Wood-Stripping")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            // If Action is NOT A Right Click, Stop
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            if (event.getItem() == null) return;

            // If The Block Is NOT A Log, Stop
            if (!logs.contains(event.getClickedBlock().getType())) return;

            if (!axes.contains(event.getPlayer().getInventory().getItemInMainHand().getType())
                    && !axes.contains(event.getPlayer().getInventory().getItemInOffHand().getType()))
                return;

            final UUID playerUUID = player.getUniqueId();
            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();
            final int x = event.getClickedBlock().getX();
            final int y = event.getClickedBlock().getY();
            final int z = event.getClickedBlock().getZ();
            final String logName = event.getClickedBlock().getType().name();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%logname%", logName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Version-Exceptions.Wood-Stripping-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%logname%", logName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.woodStripping(Data.serverName, player, logName, x, y, z, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertWoodStripping(Data.serverName, player, logName, x, y, z, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getWoodStrippingFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Version-Exceptions.Wood-Stripping")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%logname%", logName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%logname%", logName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping")).isEmpty()) {

                        Discord.woodStripping(player, Objects.requireNonNull(Messages.get().getString("Discord.Version-Exceptions.Wood-Stripping")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%logname%", logName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.woodStripping(Data.serverName, player, logName, x, y, z, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertWoodStripping(Data.serverName, player, logName, x, y, z, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
