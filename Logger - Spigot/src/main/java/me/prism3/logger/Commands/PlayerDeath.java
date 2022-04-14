/*
package me.prism3.logger.Commands;

import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Utils.InventoryToBase64;
import me.prism3.logger.Utils.PlayerFolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerDeath implements CommandExecutor , Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        try {
            this.stepOne((Player) sender);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    // Opening the first GUI with all online players and their available backups
    private void stepOne(Player player) throws IOException {

        Inventory inv = Bukkit.createInventory(null, 27, "Player Inventory Checker");

        for (Player players : Bukkit.getOnlinePlayers()) {

            ItemStack skull = new ItemStack(Objects.requireNonNull(Material.getMaterial("SKULL_ITEM")), 1, (short) 3);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            assert meta != null;
            meta.setOwner(players.getName());
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + players.getName());
            final ArrayList<String> youtubeLore = new ArrayList<>();
            youtubeLore.add(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(players));
            meta.setLore(youtubeLore);
            skull.setItemMeta(meta);

            inv.addItem(skull);

        }

        player.openInventory(inv);

    }

    File file;

    @EventHandler
    public void onClick(final InventoryClickEvent e) throws IOException {

        if (e.getClickedInventory() == null) return;

        String title = e.getView().getTitle();

        if (e.getView().getTitle().equals("Player Inventory Checker") || title.endsWith("'s Inventory")) {

            e.setCancelled(true);

            final Player player = (Player) e.getWhoClicked();

            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) return;

            String clickedItem = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();

            boolean isPresent = false;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                if (clickedItem.equals(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName())) {

                    this.stepTwo(player, onlinePlayer);
                    break;

                }

                if (e.getCurrentItem().getType() == Material.CHEST) {

                    for (String list : PlayerFolder.fileNames(onlinePlayer)) {

                        if (list.equalsIgnoreCase(clickedItem)) {

                            file = new File(list + ".yml");
                            isPresent = true;
                            break;

                        }
                    }

                    this.stepThree(player, onlinePlayer);

                    if (isPresent) break;

                }

                if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {

                    this.addItem(player);

                }
            }*/
/*

            Bukkit.getOnlinePlayers(ProxiedPlayer -> {

                this.stepTwo(player, ProxiedPlayer);
            });*//*

        }
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void stepTwo(Player staff, Player player) throws IOException {

        staff.closeInventory();

        int i = PlayerFolder.backupCount(player);

        Inventory inv = Bukkit.createInventory(staff, 27, "Player Inventory Checker");

        String[] files = PlayerFolder.fileNames(player);

        for (int e = 0; e < i; e++) {

            ItemStack chest = new ItemStack(Material.CHEST);

            ItemMeta chestMeta = chest.getItemMeta();

            assert chestMeta != null;
            chestMeta.setDisplayName(files[e]);

            chest.setItemMeta(chestMeta);

            inv.setItem(e, chest);

        }

        staff.openInventory(inv);
    }

    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    public void stepThree(Player staff, Player player) {

        staff.closeInventory();

        Inventory inv = Bukkit.createInventory(staff, 54, player.getName() + "'s Inventory");
        
        FileConfiguration f = YamlConfiguration.loadConfiguration(new File(FileHandler.getPlayerDeathBackupLogFolder() + "/" + player.getName() + "/" + file + "/"));

        System.out.println(f);

        ItemStack[] items = this.method(f.getString("Inventory"));
        ItemStack backButton = new ItemStack(Material.ENDER_CHEST);

        ItemMeta backButtonMeta = backButton.getItemMeta();

        assert backButtonMeta != null;
        backButtonMeta.setDisplayName(ChatColor.RED + "Back");

        backButton.setItemMeta(backButtonMeta);

        ItemStack backupButton = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta backupButtonMeta = backupButton.getItemMeta();
        assert backupButtonMeta != null;
        backupButtonMeta.setDisplayName(ChatColor.AQUA + "Backup");
        List<String> backupButtonLore = new ArrayList<>();
        backupButtonLore.add(ChatColor.RED + "Clears Player's current Inventory!");
        backupButtonMeta.setLore(backupButtonLore);

        backupButton.setItemMeta(backupButtonMeta);

        for (int i = 0; i < items.length; i++) {

            inv.setItem(i, items[i]);

        }

        inv.setItem(45, backButton);
        inv.setItem(49, backupButton);

        staff.openInventory(inv);
    }

    private void addItem(Player player) {

        FileConfiguration f = YamlConfiguration.loadConfiguration(new File(FileHandler.getPlayerDeathBackupLogFolder() + "/" + player.getName() + "/" + file + "/"));

        player.getInventory().clear();

        ItemStack[] items = this.method(f.getString("Inventory"));

        for (int i = 0; i < items.length; i++) {

            player.getInventory().setItem(i, items[i]);

        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lLogger &8&l| &rInventory Restored."));
    }

    private ItemStack[] method(String base64) {

        ItemStack[] inv = null;

        try {
            inv = InventoryToBase64.stacksFromBase64(base64);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return inv;
    }
}
*/
