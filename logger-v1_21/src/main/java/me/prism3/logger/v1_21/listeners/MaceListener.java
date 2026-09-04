package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.Log;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class MaceListener implements Listener {

    private final LoggerAPI plugin;

    public MaceListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMaceAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            
            // Check if holding a Mace
            if (player.getInventory().getItemInMainHand().getType() == Material.MACE) {
                // Check for Smash Attack (falling significantly)
                if (player.getFallDistance() > 1.5) {
                    double damage = event.getFinalDamage();
                    String victim = event.getEntity().getName();
                    
                    Log.info(player.getName() + " performed a Mace Smash Attack on " + victim + 
                            " (Fall: " + String.format("%.2f", player.getFallDistance()) + " blocks, Damage: " + String.format("%.2f", damage) + ")");
                }
            }
        }
    }
}
