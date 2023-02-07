package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.commands.subcommands.PlayerInventory;
import me.prism3.logger.database.sqlite.global.registration.SQLiteDataRegistration;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.events.plugindependent.OnViaVer;
import me.prism3.logger.hooks.ViaVersionUtil;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static me.prism3.logger.utils.Data.*;

public class OnPlayerJoin implements Listener {

    private final Main main = Main.getInstance();

    private static final Map<String, ItemStack> headCache = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {

        if (Data.isRegistration && this.main.getSqLiteReg() != null && !SQLiteDataRegistration.playerExists(event.getPlayer())) {

            SQLiteDataRegistration.insertRegistration(event.getPlayer());
            new OnPlayerRegister();
        }

        if (isPlayerDeathBackup && !headCache.containsKey(event.getPlayer().getName())) {
            System.out.println("1");

            headCache.put(event.getPlayer().getName(), new PlayerInventory().createSkull(event.getPlayer()));

        }

        if (Data.isViaVersion && ViaVersionUtil.isAllowed)
            new OnViaVer(event.getPlayer());

        final Player player = event.getPlayer();

        if (Data.isCommandsToBlock && Data.isCommandsToLog && player.hasPermission(Data.loggerStaff)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&bLogger &8&l| &cEnabling both Whitelist" +
                            " and Blacklist isn't supported. Disable one of them" +
                            " to continue logging Player Commands."));
        }

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        InetSocketAddress ip = player.getAddress();
        if (!Data.isPlayerIP) ip = null;
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%IP%", String.valueOf(ip));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Join-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Join", placeholders, FileHandler.getPlayerJoinLogFile());
            }
        }

        // Discord
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Join-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Join", placeholders, DiscordChannels.PLAYER_JOIN, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerJoin(Data.serverName, playerName, playerUUID.toString(), coordinates, ip, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerJoin(Data.serverName, playerName, playerUUID.toString(), coordinates, ip, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }

    public static Map<String, ItemStack> getHeadCache() { return headCache; }
}
