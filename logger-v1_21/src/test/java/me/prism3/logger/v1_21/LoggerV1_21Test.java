package me.prism3.logger.v1_21;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.LoggerManager;
import me.prism3.logger.utils.enums.LogType;
import me.prism3.logger.v1_21.listeners.CrafterListener;
import me.prism3.logger.v1_21.listeners.MaceListener;
import me.prism3.logger.v1_21.listeners.SculkShriekerListener;
import me.prism3.logger.v1_21.listeners.VaultListener;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.SculkShrieker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerV1_21Test {

    @Test
    @SuppressWarnings("unchecked")
    public void testAllFour1_21ListenersEndToEnd() {
        LoggerAPI plugin = mock(LoggerAPI.class);
        LoggerManager loggerManager = mock(LoggerManager.class);
        when(plugin.getLoggerManager()).thenReturn(loggerManager);

        Player player = mock(Player.class);
        World world = mock(World.class);
        when(world.getName()).thenReturn("trial_chambers");
        Location loc = new Location(world, 250, -20, 300);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getName()).thenReturn("MacePlayer");
        when(player.getWorld()).thenReturn(world);
        when(player.getLocation()).thenReturn(loc);
        when(player.hasPermission(anyString())).thenReturn(false);

        ArgumentCaptor<Map<String, String>> mapCaptor = ArgumentCaptor.forClass(Map.class);

        // 1. MaceListener (Fall > 1.5 with Material.MACE)
        PlayerInventory inv = mock(PlayerInventory.class);
        ItemStack maceItem = mock(ItemStack.class);
        when(maceItem.getType()).thenReturn(Material.MACE);
        when(inv.getItemInMainHand()).thenReturn(maceItem);
        when(player.getInventory()).thenReturn(inv);
        when(player.getFallDistance()).thenReturn(8.5f);

        Entity victim = mock(Entity.class);
        when(victim.getName()).thenReturn("Breeze");
        EntityDamageByEntityEvent maceEvent = mock(EntityDamageByEntityEvent.class);
        when(maceEvent.getDamager()).thenReturn(player);
        when(maceEvent.getEntity()).thenReturn(victim);
        when(maceEvent.getFinalDamage()).thenReturn(38.25);

        MaceListener maceListener = new MaceListener(plugin);
        maceListener.onMaceAttack(maceEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_MACE_SMASH), eq(player), mapCaptor.capture());
        Map<String, String> macePh = mapCaptor.getValue();
        assertEquals("Breeze", macePh.get("victim"));
        assertEquals("8.50", macePh.get("fall_distance"));
        assertEquals("38.25", macePh.get("damage"));

        // 2. VaultListener (BlockDispenseLootEvent on Material.VAULT)
        Block vaultBlock = mock(Block.class);
        when(vaultBlock.getType()).thenReturn(Material.VAULT);
        when(vaultBlock.getX()).thenReturn(255);
        when(vaultBlock.getY()).thenReturn(-19);
        when(vaultBlock.getZ()).thenReturn(305);

        ItemStack loot = mock(ItemStack.class);
        when(loot.getType()).thenReturn(Material.HEAVY_CORE);
        when(loot.getAmount()).thenReturn(1);

        BlockDispenseLootEvent vaultEvent = mock(BlockDispenseLootEvent.class);
        when(vaultEvent.getBlock()).thenReturn(vaultBlock);
        when(vaultEvent.getPlayer()).thenReturn(player);
        when(vaultEvent.getDispensedLoot()).thenReturn(Collections.singletonList(loot));

        VaultListener vaultListener = new VaultListener(plugin);
        vaultListener.onVaultDispense(vaultEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_VAULT_UNLOCK), eq(player), mapCaptor.capture());
        Map<String, String> vaultPh = mapCaptor.getValue();
        assertEquals("255", vaultPh.get("vault_x"));
        assertEquals("-19", vaultPh.get("vault_y"));
        assertEquals("305", vaultPh.get("vault_z"));
        assertEquals("HEAVY_CORE x1", vaultPh.get("items"));

        // 3. CrafterListener (CrafterCraftEvent)
        Block crafterBlock = mock(Block.class);
        when(crafterBlock.getX()).thenReturn(260);
        when(crafterBlock.getY()).thenReturn(64);
        when(crafterBlock.getZ()).thenReturn(310);
        when(crafterBlock.getWorld()).thenReturn(world);

        ItemStack craftedItem = mock(ItemStack.class);
        when(craftedItem.getType()).thenReturn(Material.IRON_BLOCK);
        when(craftedItem.getAmount()).thenReturn(1);

        CrafterCraftEvent crafterEvent = mock(CrafterCraftEvent.class);
        when(crafterEvent.getBlock()).thenReturn(crafterBlock);
        when(crafterEvent.getResult()).thenReturn(craftedItem);

        CrafterListener crafterListener = new CrafterListener(plugin);
        crafterListener.onCraft(crafterEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_CRAFTER_CRAFT), isNull(), mapCaptor.capture());
        Map<String, String> crafterPh = mapCaptor.getValue();
        assertEquals("IRON_BLOCK x1", crafterPh.get("item"));
        assertEquals("trial_chambers", crafterPh.get("world"));

        // 4. SculkShriekerListener (BlockReceiveGameEvent with GameEvent.SHRIEK)
        Block shriekerBlock = mock(Block.class);
        SculkShrieker shriekerState = mock(SculkShrieker.class);
        when(shriekerState.getWarningLevel()).thenReturn(3);
        when(shriekerBlock.getState()).thenReturn(shriekerState);
        when(shriekerBlock.getX()).thenReturn(270);
        when(shriekerBlock.getY()).thenReturn(-40);
        when(shriekerBlock.getZ()).thenReturn(320);

        BlockReceiveGameEvent shriekEvent = mock(BlockReceiveGameEvent.class);
        GameEvent mockShriekGameEvent = mock(GameEvent.class);
        when(mockShriekGameEvent.getKey()).thenReturn(org.bukkit.NamespacedKey.minecraft("shriek"));
        when(shriekEvent.getEvent()).thenReturn(mockShriekGameEvent);
        when(shriekEvent.getEntity()).thenReturn(player);
        when(shriekEvent.getBlock()).thenReturn(shriekerBlock);

        SculkShriekerListener shriekerListener = new SculkShriekerListener(plugin);
        shriekerListener.onShriek(shriekEvent);

        verify(loggerManager).logEvent(eq(LogType.PLAYER_SCULK_SHRIEKER), eq(player), mapCaptor.capture());
        Map<String, String> shriekPh = mapCaptor.getValue();
        assertEquals("3", shriekPh.get("level"));
    }
}
