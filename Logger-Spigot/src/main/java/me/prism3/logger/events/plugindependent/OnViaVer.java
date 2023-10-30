package me.prism3.logger.events.plugindependent;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;


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

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%version%", versionName);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                this.main.getFileHandler().handleFileLog(LogCategory.STAFF, "Files.Extras.ViaVersion-Staff", placeholders);
            } else {
                this.main.getFileHandler().handleFileLog(LogCategory.VIAVERSION, "Files.Extras.ViaVersion", placeholders);
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Extras.ViaVersion-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Extras.ViaVersion", placeholders, DiscordChannels.VIA_VERSION, playerName, playerUUID);
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
