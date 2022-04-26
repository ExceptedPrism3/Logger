package com.carpour.loggerbungeecord;

import com.carpour.loggerbungeecord.Commands.Reload;
import com.earth2me.essentials.Essentials;
import org.carour.loggercore.database.mysql.MySQL;
import org.carour.loggercore.database.mysql.MySQLData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Discord.DiscordFile;
import com.carpour.loggerbungeecord.Events.*;
import com.carpour.loggerbungeecord.ServerSide.Start;
import com.carpour.loggerbungeecord.ServerSide.Stop;
import com.carpour.loggerbungeecord.Utils.*;
import net.md_5.bungee.api.plugin.Plugin;
import org.carour.loggercore.util.SqlConfiguration;

public final class Main extends Plugin {

    private static Main instance;

    private static ConfigManager cm;

    public MySQL mySQL;
    public MySQLData mySQLData;
    public Discord discord;

    @Override
    public void onEnable() {


        instance = this;

        cm = new ConfigManager();
        cm.init();
        
        new Messages().init();
        
        new DiscordFile().init();
        
        discord = new Discord();
        discord.run();
        
        FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        getProxy().getPluginManager().registerListener(this, new OnChat());
        getProxy().getPluginManager().registerListener(this, new OnLogin());
        getProxy().getPluginManager().registerListener(this, new OnLeave());
        getProxy().getPluginManager().registerListener(this, new OnReload());

        getProxy().getPluginManager().registerCommand(this, new Reload());

        if (getConfig().getBoolean("MySQL.Enable")) {


            SqlConfiguration sqlConfiguration = new SqlConfiguration(
                    getConfig().getString("MySQL.Host"),
                    Integer.parseInt(getConfig().getString("MySQL.Port")),
                    getConfig().getString("MySQL.Database"),
                    getConfig().getString("MySQL.Username"),
                    getConfig().getString("MySQL.Password")
            );

            mySQL = new MySQL(sqlConfiguration, getLogger());
            mySQL.connect();
            mySQLData = new MySQLData<>(this, cm.getConfig());
            if (mySQL.isConnected()) mySQLData.createTable();
            mySQLData.emptyTable();

        }

        new ASCIIArt().Art();

        //bstats

        new Metrics(this, 12036);

        //Update Checker
        new UpdateChecker().checkUpdates();

        getLogger().info("has been Enabled!");

        new Start().run();
    }

    public static Main getInstance() {
        return instance;
    }

    public static ConfigManager getConfig() { return cm; }

    @Override
    public void onDisable() {

        new Stop().run();

        if (getConfig().getBoolean("MySQL.Enable") && mySQL.isConnected()) mySQL.disconnect();

        discord.disconnect();

        getLogger().info("has been Disabled!");
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
    public Essentials getAPI() {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return instance;
    }
}
