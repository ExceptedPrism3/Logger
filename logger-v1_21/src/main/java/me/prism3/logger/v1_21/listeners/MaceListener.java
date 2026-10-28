package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Locale;
import java.util.Map;

public class MaceListener implements Listener {

    private final LoggerAPI plugin;

    public MaceListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMaceAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (PermissionManager.isExempt(player)) return;

            if (player.getInventory().getItemInMainHand().getType() == Material.MACE) {
                if (player.getFallDistance() > 1.5) {
                    double damage = event.getFinalDamage();
                    String victim = event.getEntity().getName();

                    Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
                    placeholders.put("victim", victim);
                    placeholders.put("fall_distance", String.format(Locale.US, "%.2f", player.getFallDistance()));
                    placeholders.put("damage", String.format(Locale.US, "%.2f", damage));

                    plugin.getLoggerManager().logEvent(LogType.PLAYER_MACE_SMASH, player, placeholders);
                }
            }
        }
    }
}
