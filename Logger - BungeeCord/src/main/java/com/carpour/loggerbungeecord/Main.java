package com.carpour.loggerbungeecord;

import com.carpour.loggerbungeecord.Events.onChat;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.Metrics;
import com.carpour.loggerbungeecord.Utils.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;

    private static ConfigManager cm;

    @Override
    public void onEnable() {

        setInstance(this);
        cm = new ConfigManager();
        cm.init();

        getLogger().info(ChatColor.GREEN + "has been loaded!");

        getProxy().getPluginManager().registerListener(this, new onChat());

        //bstats

        new Metrics(this, 11779);

        //Update Checker

        new UpdateChecker(this, 93562).getLatestVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info(ChatColor.GREEN + "Plugin is up to date!");
            } else {
                getLogger().info(ChatColor.GOLD + "Plugin has a new Update! Current version " + ChatColor.RED + version + ChatColor.GOLD + " and the newest is " + ChatColor.GREEN + this.getDescription().getVersion());
            }
        });
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static ConfigManager getConfig() {
        return cm;
    }

    @Override
    public void onDisable() {

        getLogger().info(ChatColor.RED + "has been unloaded!");

    }
}
