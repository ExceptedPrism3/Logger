package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.InventoryToBase64;
import me.prism3.logger.utils.PlayerFolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerInventoryCommand implements Listener, SubCommand {

    @Override
    public String getName() { return "playerinventory"; }

    @Override
    public String getDescription() { return "Opens a menu with all online players and their available backups."; }

    @Override
    public String getSyntax() { return "/logger playerinventory"; }

    @Override
    public void perform(Player player, String[] args) {

        try {
            this.stepOne(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getSubCommandsArgs(Player player, String[] args) { return Collections.emptyList(); }

    // Opening the first GUI with all online players and their available backups
    private void stepOne(Player player) throws IOException {

        final Inventory firstInv = Bukkit.createInventory(null, 27, "Player Inventory Checker");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            final boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");

            Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

            final ItemStack skull = new ItemStack(type, 1);

            if (!isNewVersion) skull.setDurability((short) 3);

            final SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner(onlinePlayer.getName());
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName());
            final ArrayList<String> skullMeta = new ArrayList<>();
            skullMeta.add(ChatColor.WHITE + "Backups Available: " + ChatColor.AQUA + PlayerFolder.backupCount(onlinePlayer));
            meta.setLore(skullMeta);
            skull.setItemMeta(meta);

            firstInv.addItem(skull);

        }

        player.openInventory(firstInv);

    }

    File file;
    Player playerNameInv;
    Player playerNameBack;

    @EventHandler
    public void onClick(final InventoryClickEvent e) {

        if (e.getClickedInventory() == null) return;

        final String title = e.getView().getTitle();

        if (title.equals("Player Inventory Checker") || title.endsWith("'s Inventory") || title.endsWith("'s Backup(s)")) {

            e.setCancelled(true);

            final Player player = (Player) e.getWhoClicked();

            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) return;

            final String clickedItem = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();

            boolean isPresent = false;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                if (clickedItem.equals(ChatColor.GOLD + "" + ChatColor.BOLD + onlinePlayer.getName())) {

                    this.stepTwo(player, onlinePlayer);
                    break;

                }

                if (e.getCurrentItem().getType() == Material.CHEST) {

                    for (String list : PlayerFolder.fileNames(onlinePlayer)) {

                        if (list.equalsIgnoreCase(clickedItem)) {

                            this.file = new File(list);
                            isPresent = true;
                            break;

                        }
                    }

                    this.stepThree(player, onlinePlayer);

                    if (isPresent) break;

                }

                if (e.getCurrentItem().getType() == Material.ENDER_CHEST) {

                    this.stepTwo(player, this.playerNameInv);

                }

                if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {

                    try {

                        this.addItem(player, this.playerNameInv);

                    } catch (Exception except) {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lLogger &8&l| &rAn error has occurred whilst restoring " + playerNameInv + "'s Inventory"));
                        except.printStackTrace();

                    }
                    break;
                }
            }
        }
    }

    // Opening the second GUI after selecting the player and displaying all available backups
    private void stepTwo(Player staff, Player player) {

        if (PlayerFolder.fileNames(player) == null || PlayerFolder.backupCount(player) == 0) return;

        staff.closeInventory();

        int i = PlayerFolder.backupCount(player);

        final Inventory secondInv = Bukkit.createInventory(staff, 27,  player.getName() + "'s Backup(s)");

        this.playerNameBack = player;

        final String[] files = PlayerFolder.fileNames(player);

        for (int e = 0; e < i; e++) {

            final ItemStack chest = new ItemStack(Material.CHEST);

            final ItemMeta chestMeta = chest.getItemMeta();

            assert chestMeta != null;
            assert files != null;
            chestMeta.setDisplayName(files[e]);

            chest.setItemMeta(chestMeta);

            secondInv.setItem(e, chest);

        }

        staff.openInventory(secondInv);
    }

    // Opening the last GUI based on the selected backup from the previous step, and displaying all items
    public void stepThree(Player staff, Player player) {

        staff.closeInventory();

        final Inventory lastInv = Bukkit.createInventory(staff, 54, player.getName() + "'s Inventory");

        this.playerNameInv = player;
        String fullPath = FileHandler.getPlayerDeathBackupLogFolder().getPath() + File.separator + player.getName() + File.separator + this.file.getName();
        File testFile = new File(fullPath);

        final FileConfiguration f = YamlConfiguration.loadConfiguration(testFile);

        final ItemStack[] invContent = InventoryToBase64.stacksFromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.stacksFromBase64(f.getString("armor"));
        final ItemStack backButton = new ItemStack(Material.ENDER_CHEST);

        final ItemMeta backButtonMeta = backButton.getItemMeta();

        assert backButtonMeta != null;
        backButtonMeta.setDisplayName(ChatColor.RED + "Back");

        backButton.setItemMeta(backButtonMeta);

        final ItemStack backupButton = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta backupButtonMeta = backupButton.getItemMeta();
        assert backupButtonMeta != null;
        backupButtonMeta.setDisplayName(ChatColor.AQUA + "Backup");
        List<String> backupButtonLore = new ArrayList<>();
        backupButtonLore.add(ChatColor.RED + "Clears Player's current Inventory!");
        backupButtonMeta.setLore(backupButtonLore);

        backupButton.setItemMeta(backupButtonMeta);

        for (int i = 0; i < Objects.requireNonNull(invContent).length; i++) {

            lastInv.setItem(i, invContent[i]);

        }

        if (armorContent.length != 0) {

            lastInv.setItem(36, armorContent[0]);
            lastInv.setItem(37, armorContent[1]);
            lastInv.setItem(38, armorContent[2]);
            lastInv.setItem(39, armorContent[3]);
        }

        lastInv.setItem(45, backButton);
        lastInv.setItem(49, backupButton);

        staff.openInventory(lastInv);
    }

    private void addItem(Player staff, Player player) {

        final FileConfiguration f = YamlConfiguration.loadConfiguration(new File(FileHandler.getPlayerDeathBackupLogFolder() + File.separator + player.getName() + File.separator + this.file));

        player.getInventory().clear();

        final ItemStack[] invContent = InventoryToBase64.stacksFromBase64(f.getString("inventory"));
        final ItemStack[] armorContent = InventoryToBase64.stacksFromBase64(f.getString("armor"));

        for (int i = 0; i < Objects.requireNonNull(invContent).length; i++) {

            player.getInventory().setItem(i, invContent[i]);

        }

        player.getInventory().setArmorContents(armorContent);

        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lLogger &8&l| &rInventory Restored."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l[ &6&lYour inventory has been restored! &8&l]"));
    }
}
