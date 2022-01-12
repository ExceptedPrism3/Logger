package com.carpour.logger;

import com.carpour.logger.API.AuthMeUtil;
import com.carpour.logger.API.EssentialsUtil;
import com.carpour.logger.Commands.OnLogger;
import com.carpour.logger.Database.MySQL.MySQL;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Events.*;
import com.carpour.logger.Events.OnCommands.OnCommand;
import com.carpour.logger.Events.OnInventories.OnFurnace;
import com.carpour.logger.Events.PluginDependent.OnAFK;
import com.carpour.logger.Events.PluginDependent.OnAuthMePassword;
import com.carpour.logger.Utils.*;
import com.carpour.logger.Database.SQLite.SQLite;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.ServerSide.*;
import de.jeff_media.updatechecker.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    private static Main instance;

    public MySQL mySQL;

    private SQLite sqLite;

    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults();

        Messages.Setup();
        Messages.get().options().copyDefaults(true);

        DiscordFile.Setup();
        DiscordFile.get().options().copyDefaults(true);

        discord = new Discord();
        discord.run();

        if (getConfig().getBoolean("MySQL.Enable")) {

            mySQL = new MySQL();
            mySQL.connect();
            MySQLData mySQLData = new MySQLData(this);
            if (mySQL.isConnected()) {
                mySQLData.createTable();
                mySQLData.emptyTable();
            }

        }

        if (getConfig().getBoolean("SQLite.Enable")) {

            sqLite = new SQLite();
            sqLite.connect();
            SQLiteData sqLiteData = new SQLiteData(this);
            if (sqLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }

        }

        if (getConfig().getBoolean("Log-to-Files") && getConfig().getBoolean("SQLite.Enable")){

            getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        getServer().getPluginManager().registerEvents(new OnChat(), this);
        getServer().getPluginManager().registerEvents(new OnCommand(), this);
        getServer().getPluginManager().registerEvents(new Console(), this);
        getServer().getPluginManager().registerEvents(new OnSign(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerKick(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLevel(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PortalCreation(), this);
        getServer().getPluginManager().registerEvents(new OnBucketFill(), this);
        getServer().getPluginManager().registerEvents(new OnBucketEmpty(), this);
        getServer().getPluginManager().registerEvents(new OnAnvil(), this);
        getServer().getPluginManager().registerEvents(new OnItemPickup(), this);
        getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
        getServer().getPluginManager().registerEvents(new OnEnchant(), this);
        getServer().getPluginManager().registerEvents(new OnBook(), this);
        getServer().getPluginManager().registerEvents(new RCON(), this);
        getServer().getPluginManager().registerEvents(new OnGameMode(), this);

        getServer().getPluginManager().registerEvents(new OnFurnace(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 300L, getConfig().getInt("RAM-TPS-Checker"));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 300L, getConfig().getInt("RAM-TPS-Checker"));

        Objects.requireNonNull(getCommand("logger")).setExecutor(new OnLogger());

        if (EssentialsUtil.getEssentialsAPI() != null){

            getServer().getPluginManager().registerEvents(new OnAFK(), this);

            getServer().getLogger().info("[Logger] Essentials Plugin Detected!");

        }

        if (AuthMeUtil.getAuthMeAPI() != null){

            getServer().getPluginManager().registerEvents(new OnAuthMePassword(), this);

            getServer().getLogger().info("[Logger] AuthMe Plugin Detected!");

        }

        new ASCIIArt().Art();

        //bstats

        new Metrics(this, 12036);

        //Update Checker
        int resource_ID = 94236;
        UpdateChecker.init(this, resource_ID)
                .checkEveryXHours(6)
                .setChangelogLink(resource_ID)
                .setNotifyOpsOnJoin(true)
                .setNotifyByPermissionOnJoin("logger.update")
                .checkNow();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GOLD + "Thank you " + ChatColor.GREEN + ChatColor.BOLD + "thelooter" + ChatColor.GOLD + " for the Contribution!");

        getServer().getLogger().info("[Logger] Plugin Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        new Stop().run();

        if (getConfig().getBoolean("MySQL.Enable") && mySQL.isConnected()) mySQL.disconnect();

        if (getConfig().getBoolean("SQLite.Enable") && sqLite.isConnected()) sqLite.disconnect();

        discord.disconnect();

        getServer().getLogger().info("[Logger] Plugin Disabled!");

    }

    public static Main getInstance() { return instance; }

    public SQLite getSqLite() { return sqLite; }
}