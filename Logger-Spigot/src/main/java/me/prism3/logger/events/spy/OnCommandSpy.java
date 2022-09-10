package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnCommandSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmdSpy(final PlayerCommandPreprocessEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Commands")
                && this.main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass)) return;

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission(Data.loggerSpy)) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.main.getConfig().getString("Spy-Features.Commands-Spy.Message")).
                                    replace("%player%", player.getName()).
                                        replace("%cmd%", event.getMessage().replace("\\", "\\\\")));

                }
            }
        }
    }
}
