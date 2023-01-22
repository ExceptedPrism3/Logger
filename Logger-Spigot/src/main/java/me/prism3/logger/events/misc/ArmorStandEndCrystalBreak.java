package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.entity.enums.ArmorStandActionType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;
import static me.prism3.logger.utils.Data.isStaffEnabled;

public class ArmorStandEndCrystalBreak implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandEndCrystalBreak(final EntityDamageByEntityEvent event) {

        if (event.isCancelled())
            return;

        final Entity damager = event.getDamager();

        if ((event.getEntity() instanceof ArmorStand || event.getEntity() instanceof EnderCrystal)
                && (damager != null && damager instanceof Player)) {

            final Player player = (Player) damager;

            if (player.hasPermission(loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final Entity entity = event.getEntity();
            final int x = entity.getLocation().getBlockX();
            final int y = entity.getLocation().getBlockY();
            final int z = entity.getLocation().getBlockZ();

            final String path;
            final File getter;
            final DiscordChannels discordChannels;

            if (entity instanceof ArmorStand) {
                path = "ArmorStand-Break";
                getter = FileHandler.getArmorStandBreakFile();
                discordChannels = DiscordChannels.ARMOR_STAND_BREAK;
            } else {
                path = "EndCrystal-Break";
                getter = FileHandler.getEndCrystalBreakFile();
                discordChannels = DiscordChannels.END_CRYSTAL_BREAK;
            }

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%world%", worldName);
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);
            placeholders.put("%x%", String.valueOf(x));
            placeholders.put("%y%", String.valueOf(y));
            placeholders.put("%z%", String.valueOf(z));

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    FileHandler.handleFileLog("Files." + path + "-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files." + path, placeholders, getter);
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord." + path + "-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord." + path, placeholders, discordChannels, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueStandCrystal(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog), ArmorStandActionType.ARMORSTAND_BREAK, event.getEntity().getType().name());

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueStandCrystal(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog), ArmorStandActionType.ARMORSTAND_BREAK, event.getEntity().getType().name());

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
