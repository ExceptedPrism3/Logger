package me.prism3.logger.events.plugindependent;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
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
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnViaVer {

    private final Main main = Main.getInstance();

    public OnViaVer(Player player) { this.onConnect(player); }

    private void onConnect(Player player) {

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final ViaAPI via = Via.getAPI();
        final int versionID = via.getPlayerVersion(player.getUniqueId());
        final String versionName = ProtocolVersion.getProtocol(versionID).getName();

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Extras.ViaVersion-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%version%", versionName) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getViaVersionFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Extras.ViaVersion").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%version%", versionName) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Extras.ViaVersion-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.ViaVersion-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%version%", versionName), false);
                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Extras.ViaVersion").isEmpty()) {

                    this.main.getDiscord().viaVersion(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.ViaVersion").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%version%", versionName), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

//                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

//                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));
//TODO DB
            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
