package me.prism3.logger.events.player;

import io.papermc.paper.event.player.PlayerTradeEvent;
import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Map;

/**
 * Listens for player villager trade events and logs them.
 */
public class VillagerTradeListener implements Listener {

    private final LoggerAPI plugin;

    public VillagerTradeListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player villager trade event.
     *
     * @param event The PlayerTradeEvent to handle.
     */
    @EventHandler
    public void onPlayerTrade(final PlayerTradeEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        final MerchantRecipe trade = event.getTrade();
        final AbstractVillager villager = (AbstractVillager) event.getVillager();

        placeholders.put("villager_profession",
                villager instanceof org.bukkit.entity.Villager
                        ? ((org.bukkit.entity.Villager) villager).getProfession().toString()
                        : "WANDERING_TRADER");
        placeholders.put("villager_level",
                villager instanceof org.bukkit.entity.Villager
                        ? String.valueOf(((org.bukkit.entity.Villager) villager).getVillagerLevel())
                        : "0");

        ItemStack cost1 = trade.getIngredients().size() > 0 ? trade.getIngredients().get(0) : null;
        ItemStack cost2 = trade.getIngredients().size() > 1 ? trade.getIngredients().get(1) : null;
        ItemStack result = trade.getResult();

        placeholders.put("cost_1", cost1 != null ? cost1.getType() + " x" + cost1.getAmount() : "none");
        placeholders.put("cost_2", cost2 != null ? cost2.getType() + " x" + cost2.getAmount() : "none");
        placeholders.put("result", result.getType() + " x" + result.getAmount());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_VILLAGER_TRADE, player, placeholders);
    }
}
