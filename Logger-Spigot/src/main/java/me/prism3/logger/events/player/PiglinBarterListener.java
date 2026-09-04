package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.stream.Collectors;

public class PiglinBarterListener implements Listener {

    private final LoggerAPI plugin;

    public PiglinBarterListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBarter(PiglinBarterEvent event) {
        // PiglinBarterEvent doesn't have a getPlayer() method directly, 
        // but the input usually comes from a player. 
        // However, the event is Entity-based (the Piglin).
        // We might not be able to reliably attribute this to a specific player 
        // unless we track who dropped the gold.
        // But for now, we'll log it as a general event or try to find nearby players?
        // Wait, the event documentation says "Stores details for players trading with piglins".
        // But looking at Spigot API, PiglinBarterEvent extends EntityEvent (the Piglin).
        // It does NOT have getPlayer().
        // So we can't easily attribute this to a player without tracking item drops.
        // However, the user asked for "Player Piglin Barter".
        // If we can't get the player, we can't use PlayerManager placeholders.
        
        // Let's check if there is a way. 
        // If not, we might have to skip player attribution or use a workaround.
        // Actually, usually bartering happens when a Piglin picks up an item.
        // The EntityPickupItemEvent has the entity (Piglin) and the Item.
        // The Item entity has a thrower UUID.
        
        // But PiglinBarterEvent happens when the trade completes.
        // Let's assume for now we just log the location and items, 
        // and if we can't find a player, we pass null.
        
        // Actually, let's try to find the nearest player? No, that's inaccurate.
        // Let's look at the input item.
        
        // NOTE: Since I cannot guarantee a player, I will log it with null player if needed,
        // but the LogType expects a player.
        // Maybe I should use a generic "Piglin" as the name?
        
        // Wait, if I can't get the player, I can't use PLAYER_PIGLIN_BARTER effectively if it relies on player context.
        // But I can pass null and handle it.
        
        // Let's try to get the input item.
        ItemStack input = event.getInput();
        String output = event.getOutcome().stream()
                .map(item -> item.getType().name() + " x" + item.getAmount())
                .collect(Collectors.joining(", "));

        Map<String, String> placeholders = new java.util.HashMap<>();
        placeholders.put("x", String.valueOf(event.getEntity().getLocation().getBlockX()));
        placeholders.put("y", String.valueOf(event.getEntity().getLocation().getBlockY()));
        placeholders.put("z", String.valueOf(event.getEntity().getLocation().getBlockZ()));
        placeholders.put("world", event.getEntity().getWorld().getName());
        placeholders.put("input", input.getType().name() + " x" + input.getAmount());
        placeholders.put("output", output);
        
        // We don't have a player, so we can't check permissions or get player placeholders.
        // This is a limitation of the API.
        // I will log it with null player.
        
        plugin.getLoggerManager().logEvent(LogType.PLAYER_PIGLIN_BARTER, null, placeholders);
    }
}
