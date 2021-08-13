package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class onCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)

    public void onPlayerCmd(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        String msg = event.getMessage();
        String[] msg2 = event.getMessage().split(" ");
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Commands"))){

            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")){

                for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    if (msg2[0].equalsIgnoreCase(m)) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed => " + msg + "\n");
                                out.close();

                                if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.sql.isConnected())) {

                                    MySQLData.playerCommands(serverName, worldName, playerName, msg, true);

                                }

                            } catch (IOException e) {

                                System.out.println("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                        }

                        Discord.playerCommand(player, "\uD83D\uDC7E " +   msg, false, Color.red);

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed => " + msg + "\n");
                            out.close();

                        } catch (IOException e) {

                            System.out.println("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        return;
                    }
                }

                return;

            }


            if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Block")) {

                    if (msg2[0].equalsIgnoreCase(m)) {

                        return;
                    }
                }
            }

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " +  msg, false, Color.red);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed => " + msg + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.sql.isConnected())) {


                        MySQLData.playerCommands(serverName, worldName, playerName, msg, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerCommand(player, "\uD83D\uDC7E " +  msg, false, Color.red);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed => " + msg + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if ((main.getConfig().getBoolean("MySQL.Enable")) && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.sql.isConnected())){

            try {

                MySQLData.playerCommands(serverName, worldName, playerName, msg, false);

            }catch (Exception e){

                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("Log.Player-Commands") && main.getConfig().getBoolean("Player-Commands.Commands-Spy.Enable")) {

            List<String> hcmds = main.getConfig().getStringList("Player-Commands.Blacklist-Commands");
            if (!(event.getPlayer().hasPermission("logger.exempt") || player.isOp())) {

                for (String hcmd : hcmds) {

                    if (event.getMessage().split(" ")[0].replaceFirst("/", "").equalsIgnoreCase(hcmd)) return;

                }

                for (Player players : Bukkit.getOnlinePlayers()) {

                    if (players.hasPermission("logger.cmdspy")) {

                        players.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(main.getConfig().getString("Player-Commands.Commands-Spy.Message")).replace("%player%", event.getPlayer().getName()).replace("%cmd%", event.getMessage())));

                    }
                }
            }
        }
    }
}
