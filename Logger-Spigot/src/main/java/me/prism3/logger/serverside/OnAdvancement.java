package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.logger.utils.Data.*;

public class OnAdvancement implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onAdvancementDone(final PlayerAdvancementDoneEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%uuid%", player.getUniqueId().toString());
        placeholders.put("%player%", player.getName());
        placeholders.put("%advancement%", event.getAdvancement().getKey().toString());


        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Server-Side.Advancement-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Server-Side.Advancement", placeholders, FileHandler.getAdvancementFile());
            }
        }

        // DiscordManager
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Server-Side.Advancement-Staff", placeholders, DiscordChannels.STAFF, player.getName(), player.getUniqueId());
            } else {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Server-Side.Advancement", placeholders, DiscordChannels.ADVANCEMENT, player.getName(), player.getUniqueId());
            }
        }
    }
}
