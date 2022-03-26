package me.prism3.loggerbungeecord.Events;

import me.prism3.loggerbungeecord.Database.External.ExternalData;
import me.prism3.loggerbungeecord.Database.SQLite.SQLiteData;
import me.prism3.loggerbungeecord.Discord.Discord;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.Utils.FileHandler;
import me.prism3.loggerbungeecord.Utils.Messages;
import me.prism3.loggerbungeecord.Utils.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnLogin implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onLogging(final PostLoginEvent event){

        if (this.main.getConfig().getBoolean("Log-Player.Login")) {

            final ProxiedPlayer player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            InetSocketAddress playerIP = (InetSocketAddress) event.getPlayer().getSocketAddress();

            if (!Data.isPlayerIP) playerIP = null;

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Login-Staff").replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerLogin(Data.serverName, playerName, playerIP, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLogin(Data.serverName, playerName, playerIP, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                    out.write(Messages.getString("Files.Player-Login").replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }
                } else {

                    if (!Messages.getString("Discord.Player-Login").isEmpty()) {

                        Discord.playerLogin(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerLogin(Data.serverName, playerName, playerIP, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLogin(Data.serverName, playerName, playerIP, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        if (!main.cF.getIsUpdated()){

            if (event.getPlayer().hasPermission(Data.loggerStaff)){

                event.getPlayer().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&8&l[&b&lLogger&8&l] " +
                        "&c&lPlugin Configs are not updated!")));

                event.getPlayer().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&8&l[&b&lLogger&8&l] " +
                        "&c&lUpdate them to avoid any complications.")));

            }
        }
    }
}
