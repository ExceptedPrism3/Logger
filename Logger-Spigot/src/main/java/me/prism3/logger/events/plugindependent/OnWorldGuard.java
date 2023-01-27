/*
package me.prism3.logger.events.plugindependent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;
import static me.prism3.logger.utils.Data.worldGuardRegions;

public class OnWorldGuard extends Handler {

    private final Main main = Main.getInstance();

    public static final Factory FACTORY = new Factory();

    public static class Factory extends Handler.Factory<OnWorldGuard> {
        @Override
        public OnWorldGuard create(Session session) { return new OnWorldGuard(session); }
    }

    public OnWorldGuard(Session session) { super(session); }

    @Override
    public boolean onCrossBoundary(LocalPlayer localPlayer, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {

        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionManager regions = container.get(localPlayer.getWorld());

        if (regions == null) return false;

        final Player player = BukkitAdapter.adapt(localPlayer);

        for (String wgRegion : worldGuardRegions) {

            final ProtectedRegion region = regions.getRegion(wgRegion);

            if (!entered.isEmpty() && entered.contains(region)) { this.logWorldGuard(player, wgRegion); }
        }

        return true;
    }

    private void logWorldGuard(Player player, String regionName) {

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getPlayer().getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Extras.WorldGuard-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%region%", regionName).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getWorldGuardFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Extras.WorldGuard").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%region%", regionName).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // DiscordManager Integration
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("DiscordManager.Extras.WorldGuard-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("DiscordManager.Extras.WorldGuard-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%region%", regionName).replace("%uuid%", playerUUID.toString()), false);
                }
            } else {

                if (!this.main.getMessages().get().getString("DiscordManager.Extras.WorldGuard").isEmpty()) {

                    this.main.getDiscord().worldGuard(playerName, playerUUID, this.main.getMessages().get().getString("DiscordManager.Extras.WorldGuard").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%region%", regionName).replace("%uuid%", playerUUID.toString()), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueWorldGuard(Data.serverName, playerUUID.toString(), worldName, playerName, regionName, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueWorldGuard(Data.serverName, playerUUID.toString(), worldName, playerName, regionName, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
*/
