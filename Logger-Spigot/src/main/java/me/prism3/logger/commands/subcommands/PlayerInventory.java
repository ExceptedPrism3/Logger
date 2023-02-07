package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.events.OnPlayerJoin;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class PlayerInventory implements Listener, SubCommand {

    private static final int ITEMS_PER_PAGE = 45;
    private static final String NEXT_PAGE = ChatColor.GREEN + "Next Page";
    private static final String PREVIOUS_PAGE = ChatColor.RED + "Previous Page";
    private Player selectedBy;
    private Player selectedPlayer;
    private File backupFile;
    private Inventory[] inventories;
    private List<Player> onlinePlayers;
    private int pageCount;

    @Override
    public String getName() { return "playerinventory"; }

    @Override
    public String getDescription() { return "Opens a menu with all online players and their available inventory backups."; }

    @Override
    public String getSyntax() { return "/logger playerinventory"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) { this.stepOne(); }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }

    // Opening the first GUI with all online players and their available backups
    private void stepOne() {

        final int itemsPerRow = 9;
        final int playerCount = Bukkit.getOnlinePlayers().size();
        this.pageCount = (int) Math.ceil((double) playerCount / (itemsPerRow * 6));

        CompletableFuture.runAsync(() -> {
            if (this.inventories == null || this.onlinePlayers == null || this.onlinePlayers.size() != Bukkit.getOnlinePlayers().size()) {
                this.inventories = IntStream.range(0, this.pageCount)
                        .mapToObj(i -> Bukkit.createInventory(this.selectedBy,
                                (int) Math.ceil((double) Math.min(playerCount - i * itemsPerRow * 6, itemsPerRow * 6) / itemsPerRow) * 9,
                                "Player Inventory Checker (Page " + (i + 1) + ")"))
                        .toArray(Inventory[]::new);

                this.onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

                IntStream.range(0, this.pageCount)
                        .forEach(i -> fillInventory(this.inventories[i], this.onlinePlayers.subList(i * itemsPerRow * 6, Math.min(this.onlinePlayers.size(), (i + 1) * itemsPerRow * 6)), itemsPerRow, i, this.pageCount));
            }

            this.selectedBy.openInventory(this.inventories[0]);
        });
    }

    private void fillInventory(Inventory inventory, List<Player> onlinePlayers, int itemsPerRow, int currentPage, int totalPages) {

        final AtomicInteger count = new AtomicInteger();

        onlinePlayers.stream()
                .map(player -> OnPlayerJoin.getHeadCache().get(player))
                .forEach(skull -> {
                    inventory.setItem(count.getAndIncrement(), skull);
                    if (count.get() % itemsPerRow == 0) {
                        count.addAndGet(itemsPerRow - 9);
                    }
                });
        if (onlinePlayers.size() == itemsPerRow * 6) {
            // Add Next Page Button
            if (currentPage < totalPages - 1) {
                final ItemStack next = this.createItemStack(Material.ARROW, NEXT_PAGE, null);
                inventory.setItem(inventory.getSize() - 5, next);
            }

            // Add Previous Page Button
            if (currentPage > 0) {
                final ItemStack previous = this.createItemStack(Material.ARROW, PREVIOUS_PAGE, null);
                inventory.setItem(inventory.getSize() - 9, previous);
            }
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {

        if (event.getClickedInventory() == null || !event.getView().getTitle().matches("(Player Inventory Checker \\(Page \\d+\\))|(Backup\\(s\\) of .*)|(Inventory of .*)"))
            return;

        event.setCancelled(true);

        final ItemStack clickedStack = event.getCurrentItem();
        if (clickedStack == null || !clickedStack.hasItemMeta())
            return;

        final String clickedItem = ChatColor.stripColor(clickedStack.getItemMeta().getDisplayName());
        this.selectedBy = (Player) event.getWhoClicked();
        int currentPage = this.getCurrentPage(event.getView().getTitle());

        switch (clickedItem) {
            case "Next Page":
                if (currentPage + 1 > this.getTotalPages()) {
                    this.selectedBy.sendMessage("You have reached the last page.");
                    return;
                }
                this.openPage(currentPage + 1);
                break;
            case "Previous Page":
                final int previousPage = currentPage - 1;
                if (previousPage < 1) {
                    this.selectedBy.sendMessage("You are already on the first page.");
                    return;
                }
                this.openPage(previousPage);
                break;
            default:
                this.handleClick(clickedItem, clickedStack);
                break;
        }
    }

    private void handleClick(String clickedItem, ItemStack clickedStack) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (clickedItem.equals(onlinePlayer.getName())) {
                this.selectedPlayer = onlinePlayer;
                this.stepTwo();
                return;
            }
            if (clickedStack.getType() == Material.CHEST) {
                for (String list : PlayerFolder.fileNames(onlinePlayer)) {
                    if (list.equalsIgnoreCase(clickedItem)) {
                        this.selectedPlayer = onlinePlayer;
                        this.backupFile = new File(FileHandler.getPlayerDeathBackupLogFolder(), onlinePlayer.getName() + File.separator + list);
                        this.stepThree();
                        return;
                    }
                }
            }
            if (clickedStack.getType() == Material.ENDER_CHEST) {
                this.stepTwo();
                return;
            }
            if (clickedStack.getType() == Material.EMERALD_BLOCK) {
                this.addItem();
                return;
            }
        }
    }

    private int getCurrentPage(final String title) {
        final Pattern pattern = Pattern.compile("(\\d+)");
        final Matcher matcher = pattern.matcher(title);
        matcher.find();
        return Integer.parseInt(matcher.group());
    }

    private int getTotalPages() {
        // calculate and return the total number of pages needed to display all players
        int totalPlayers = Bukkit.getOnlinePlayers().size();
        return (int) Math.ceil(totalPlayers / (double) ITEMS_PER_PAGE);
    }

    private void openPage(final int pageNumber) {

        final Inventory inventory = Bukkit.createInventory(this.selectedBy, 54, "Player Inventory Checker (Page " + (pageNumber) + ")");

        int startIndex = (pageNumber - 1) * ITEMS_PER_PAGE;
        int endIndex = startIndex + ITEMS_PER_PAGE;
        final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        int totalPlayers = players.size();

        for (int i = startIndex; i < endIndex && i < totalPlayers; i++)
            inventory.addItem(OnPlayerJoin.getHeadCache().get(players.get(i)));

        final ItemStack next = this.createItemStack(Material.ARROW, NEXT_PAGE, null);
        inventory.setItem(inventory.getSize() - 5, next);

        final ItemStack back = this.createItemStack(Material.ARROW, PREVIOUS_PAGE, null);
        inventory.setItem(inventory.getSize() - 9, back);

        this.selectedBy.openInventory(inventory);
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

        final ItemStack item = new ItemStack(Material.CHEST, 1);
        final ItemMeta meta = item.getItemMeta();

        for (String file : files) {

            meta.setDisplayName(file);
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

        for (ItemStack item : invContent)
            lastInv.addItem(item);

        final ItemStack[] armorContent = InventoryToBase64.fromBase64(f.getString("armor"));

        for (ItemStack item : armorContent)
            lastInv.addItem(item);

        lastInv.setItem(45, createItemStack(Material.ENDER_CHEST, ChatColor.RED + "Back", null));
        lastInv.setItem(49, createItemStack(Material.EMERALD_BLOCK, ChatColor.AQUA + "Backup",
                Collections.singletonList(ChatColor.RED + "Clears player's current Inventory!")));

        this.selectedBy.openInventory(lastInv);
    }

    public ItemStack createSkull(Player onlinePlayer) {

        final boolean isNewVersion = Arrays.stream(Material.values()).anyMatch(material -> material.name().equals("PLAYER_HEAD"));
        final Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        final ItemStack skull = new ItemStack(type, 1);

        if (!isNewVersion)
            skull.setDurability((short) 3);

        final SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(onlinePlayer.getName());
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName());
        final List<String> lore = new ArrayList<>(Collections.singletonList(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(onlinePlayer)));
        meta.setLore(lore);
        skull.setItemMeta(meta);

        return skull;
    }

    private ItemStack createItemStack(final Material material, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material, 1);
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
