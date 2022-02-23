package com.carpour.logger.Events.Spy;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnCommandSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmdSpy(final PlayerCommandPreprocessEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Commands")
                && this.main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt) || player.hasPermission(loggerSpyBypass)) return;

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission(loggerSpy)) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(this.main.getConfig().getString("Spy-Features.Commands-Spy.Message")).
                                    replace("%player%", player.getName()).
                                    replace("%cmd%", event.getMessage().replace("\\", "\\\\"))));

                }
            }
        }
    }
}
