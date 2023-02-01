package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import me.prism3.logger.utils.playerdeathutils.PlayerFolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import java.io.File;
import java.util.*;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class PlayerInventory implements Listener, SubCommand {

    private Player selectedPlayer;
    private Player selectedBy;
    private File backupFile;

    @Override
    public String getName() { return "playerinventory"; }

    @Override
    public String getDescription() { return "Opens a menu with all online players and their available inventory backups."; }

    @Override
    public String getSyntax() { return "/logger playerinventory"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) { this.stepOne((Player) commandSender); }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    // Opening the first GUI with all online players and their available backups
    private void stepOne(final Player player) {
        int page = 0;
        final int itemsPerPage = 27;
        final int playerCount = Bukkit.getOnlinePlayers().size();
        final Inventory[] inventories = getInventories(playerCount, itemsPerPage);
        Main.getInstance().getExecutor().submit(() -> {
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            int startIndex = page * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, onlinePlayers.size());
            List<Player> playersToDisplay = onlinePlayers.subList(startIndex, endIndex);
            fillInventory(inventories[page], playersToDisplay);
            player.openInventory(inventories[page]);
        });
    }

    private Inventory[] getInventories(int playerCount, int itemsPerPage) {
        int pageCount = (int) Math.ceil((double) playerCount / itemsPerPage);
        Inventory[] inventories = new Inventory[pageCount];
        for (int i = 0; i < pageCount; i++) {
            int inventorySize = (int) Math.ceil((double) Math.min(itemsPerPage, playerCount - i * itemsPerPage) / 9) * 9;
            inventories[i] = Bukkit.createInventory(null, inventorySize, "Player Inventory Checker (Page " + (i + 1) + ")");
        }
        return inventories;
    }


    private void fillInventory(Inventory inventory, List<Player> players) {
        final boolean isNewVersion = Arrays.stream(Material.values()).anyMatch(material -> material.name().equals("PLAYER_HEAD"));
        final Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        for (Player onlinePlayer : players) {
            final ItemStack skull = new ItemStack(type, 1);
            if (!isNewVersion) {
                skull.setDurability((short) 3);
            }
            final SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(onlinePlayer.getName());
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName());
            List<String> lore = new ArrayList<>(Collections.singletonList(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(onlinePlayer)));
            meta.setLore(lore);
            skull.setItemMeta(meta);
            inventory.addItem(skull);
        }
    }

    @EventHandler
    private void onClick(final InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;

        final String title = event.getView().getTitle();
        if (!title.equals("Player Inventory Checker") && !title.startsWith("Inventory") && !title.startsWith("Backup(")) return;

        event.setCancelled(true);

        this.selectedBy = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        final String clickedItem = event.getCurrentItem().getItemMeta().getDisplayName();
        final ItemStack clickedStack = event.getCurrentItem();

        switch (clickedStack.getType()) {
            case CHEST:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    for (String list : PlayerFolder.fileNames(onlinePlayer)) {
                        if (list.equalsIgnoreCase(clickedItem)) {
                            this.selectedPlayer = onlinePlayer;
                            this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), onlinePlayer.getName() + File.separator + list);
                            this.stepThree();
                            return;
                        }
                    }
                }
                break;
            case ENDER_CHEST:
                this.stepTwo();
                return;
            case EMERALD_BLOCK:
                this.addItem();
                return;
            default:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (clickedItem.equals(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName())) {
                        this.selectedPlayer = onlinePlayer;
                        this.stepTwo();
                        return;
                    }
                }
        }
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void stepTwo() {

        final int backupCount = PlayerFolder.backupCount(this.selectedPlayer);

        if (backupCount == 0) return;

        this.selectedBy.closeInventory();

        int invSize = 9 * (int) Math.ceil(backupCount / 9.0);
        invSize = Math.min(invSize, 54);
        final Inventory secondInv = Bukkit.createInventory(this.selectedBy, invSize, "Backup(s) of " + this.selectedPlayer.getName());
        final String[] files = PlayerFolder.fileNames(this.selectedPlayer);

        for (int i = 0; i < backupCount; i++) {

            final ItemStack item = new ItemStack(Material.CHEST, 1);
            final ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(files[i]);
            item.setItemMeta(meta);

            secondInv.addItem(item);
        }

        this.selectedBy.openInventory(secondInv);
    }


    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    private void stepThree() {

        this.selectedBy.closeInventory();

        final Inventory lastInv = Bukkit.createInventory(this.selectedBy, 54, "Inventory of " + this.selectedPlayer.getName());

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final ItemStack[] invContent = InventoryToBase64.fromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));

        for (ItemStack item : invContent)
            lastInv.addItem(item);

        for (ItemStack item : armorContent)
            lastInv.addItem(item);

        lastInv.setItem(45, createItemStack(Material.ENDER_CHEST, ChatColor.RED + "Back", null));
        lastInv.setItem(49, createItemStack(Material.EMERALD_BLOCK, ChatColor.AQUA + "Backup",
                Collections.singletonList(ChatColor.RED + "Clears player's current Inventory!")));

        this.selectedBy.openInventory(lastInv);
    }

    private ItemStack createItemStack(final Material material, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        if (lore != null)
            meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private void addItem() {

        final FileConfiguration f = YamlConfiguration.loadConfiguration(this.backupFile);

        final ItemStack[] invContent = InventoryToBase64.fromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));

        this.selectedPlayer.getInventory().setContents(invContent);

        this.selectedPlayer.getInventory().setArmorContents(armorContent);

        this.selectedBy.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "Inventory Restored."));
        this.selectedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&8&l[ &6&lYour inventory has been restored! &8&l]"));
    }
}
