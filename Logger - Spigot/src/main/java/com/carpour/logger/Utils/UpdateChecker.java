package com.carpour.logger.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {
    private final JavaPlugin javaPlugin;
    private final String localPluginVersion;
    private String spigotPluginVersion;
    private static final int ID = 94236;

    public UpdateChecker(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.localPluginVersion = javaPlugin.getDescription().getVersion();
    }

    public void checkForUpdate() {

        Bukkit.getScheduler().runTaskAsynchronously(this.javaPlugin, () -> Bukkit.getScheduler().runTask(this.javaPlugin, () -> {

            try {

                HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ID)).openConnection();
                connection.setRequestMethod("GET");
                this.spigotPluginVersion = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();

            } catch (IOException var2) {

                var2.printStackTrace();
                return;

            }

            if (!this.localPluginVersion.equals(this.spigotPluginVersion)) {

                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[Logger] |&e A New Version &a" + this.spigotPluginVersion + " &eis Available at: &ahttps://www.spigotmc.org/resources/" + ID + "/updates"));
                Bukkit.getScheduler().runTask(this.javaPlugin, () -> Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler(
                            priority = EventPriority.MONITOR
                    )
                    public void onPlayerJoin(PlayerJoinEvent event) {

                        Player player = event.getPlayer();

                        if (player.isOp() || player.hasPermission("logger.update")) {

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bLogger&7] &f|&e A New Version &a" + UpdateChecker.this.spigotPluginVersion + " &eis Available at: &ahttps://www.spigotmc.org/resources/" + ID + "/updates"));

                        }
                    }
                }, this.javaPlugin));

            }
        }));
    }
}
