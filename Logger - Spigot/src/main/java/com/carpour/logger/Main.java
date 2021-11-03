package com.carpour.logger;

import org.carour.loggercore.database.mysql.MySQLTemplate;
import org.carour.loggercore.database.postgresql.PostgreSQL;
import org.carour.loggercore.database.postgresql.PostgreSQLData;
import com.carpour.logger.Commands.OnLogger;
import org.carour.loggercore.database.mysql.MySQL;
import org.carour.loggercore.database.mysql.MySQLData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Events.*;
import com.carpour.logger.Events.onCommands.OnCommand;
import com.carpour.logger.Events.onInventories.OnFurnace;
import com.carpour.logger.Utils.*;
import org.carour.loggercore.database.postgresql.PostgreSQLTemplate;
import org.carour.loggercore.database.sqlite.SQLite;
import org.carour.loggercore.database.sqlite.SQLiteData;
import com.carpour.logger.ServerSide.*;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.carour.loggercore.database.sqlite.SQLiteTemplate;

import java.util.Objects;

public class Main extends JavaPlugin implements MySQLTemplate<JavaPlugin>, SQLiteTemplate<JavaPlugin>, PostgreSQLTemplate<JavaPlugin> {

    private static Main instance;

    public MySQL mySQL;
    public MySQLData<Main> mySQLData;

    private SQLite sqLite;
    private SQLiteData<?> sqLiteData;

    private PostgreSQL postgreSQL;
    private PostgreSQLData<?> postgreSQLData;

    public Start start;
    public Stop stop;

    public Discord discord;

    public SQLite getSqLite() {
        return sqLite;
    }

    public SQLiteData<?> getSqLiteData() {
        return sqLiteData;
    }


    @Override
    public PostgreSQL getPostgreSQL() {
        return postgreSQL;
    }

    public PostgreSQLData<?> getPostgreSQLData() {
        return postgreSQLData;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults();

        Messages.Setup();
        Messages.get().options().copyDefaults(true);
        Messages.save();

        DiscordFile.Setup();
        DiscordFile.values();
        DiscordFile.get().options().copyDefaults(true);
        DiscordFile.save();

        discord = new Discord();
        discord.run();

        if (getConfig().getBoolean("MySQL.Enable")) {

            mySQL = new MySQL(this);
            mySQL.connect();
            mySQLData = new MySQLData<>(this);
            if (mySQL.isConnected()) mySQLData.createTable();
            mySQLData.emptyTable();

        }

        if (getConfig().getBoolean("SQLite.Enable")) {

            sqLite = new SQLite(this);
            sqLite.connect();
            sqLiteData = new SQLiteData<>(this);
            if (sqLite.isConnected()) {
                sqLiteData.createTable();
            }
            sqLiteData.emptyTable();

        }

        if (getConfig().getBoolean("PostgreSQL.Enable")) {
            postgreSQL = new PostgreSQL(this);
            postgreSQL.connect();

            postgreSQLData = new PostgreSQLData<>(this);

            if (postgreSQL.isConnected()) {
                postgreSQLData.createTable();
            }
            postgreSQLData.emptyTable();

        }

        if (getConfig().getBoolean("Log-to-Files") && getConfig().getBoolean("SQLite.Enable")) {

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
        getServer().getPluginManager().registerEvents(new OnBucketPlace(), this);
        getServer().getPluginManager().registerEvents(new OnAnvil(), this);
        getServer().getPluginManager().registerEvents(new OnItemPickup(), this);
        getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
        getServer().getPluginManager().registerEvents(new OnEnchant(), this);
        getServer().getPluginManager().registerEvents(new OnBook(), this);

        getServer().getPluginManager().registerEvents(new OnFurnace(), this);
//        getServer().getPluginManager().registerEvents(new onShulker(), this);
//        getServer().getPluginManager().registerEvents(new OnChest(), this);


        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 200L, getConfig().getInt("RAM-TPS-Checker"));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 200L, getConfig().getInt("RAM-TPS-Checker"));

        Objects.requireNonNull(getCommand("logger")).setExecutor(new OnLogger());

        if (getAPI() != null) {

            getServer().getPluginManager().registerEvents(new OnAFK(), this);

            getServer().getLogger().info("[Logger] Essentials Plugin was Found!");

        }

        new ASCIIArt().art();

        //bstats

        new Metrics(this, 12036);

        //Update Checker
        new UpdateChecker(this).checkForUpdate();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GOLD + "Thank you " + ChatColor.GREEN + ChatColor.BOLD + "thelooter" + ChatColor.GOLD + " for the Contribution!");

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "Plugin Enabled!");

        start = new Start();
        start.run();
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void setMySQL(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    @Override
    public MySQL getMySQL() {
        return this.mySQL;
    }

    @Override
    public void setSQLite(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    @Override
    public SQLite getSQLite() {
        return this.sqLite;
    }

    @Override
    public void setPostgreSQL(PostgreSQL postgreSQL) {
        this.postgreSQL = postgreSQL;
    }


    @Override
    public Essentials getAPI() {

        IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials instanceof Essentials) {

            return (Essentials) essentials;

        } else return null;
    }


    @Override
    public JavaPlugin getPlugin() {
        return instance;
    }

    @Override
    public void onDisable() {

        stop = new Stop();
        stop.run();

        if (getConfig().getBoolean("MySQL.Enable") && mySQL.isConnected()) mySQL.disconnect();

        if (getConfig().getBoolean("SQLite.Enable") && sqLite.isConnected()) sqLite.disconnect();

        discord.disconnect();

        getServer().getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "Plugin Disabled!");

    }
}