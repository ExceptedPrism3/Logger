package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(final AsyncChatEvent event) {

            final Player player = event.getPlayer();
            World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            String msg = PlainTextComponentSerializer.plainText().serialize(event.message());
            String serverName = main.getConfig().getString("Server-Name");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Chat"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                Discord.staffChat(player, "\uD83D\uDCAC **|** \uD83D\uDC6E\u200D♂️ " + msg, false, Color.GREEN);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has said => " + msg +"\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Chat")) && (main.sql.isConnected())) {


                        MySQLData.playerChat(serverName, worldName, playerName, msg, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerChat(player, "\uD83D\uDCAC " + msg, false, Color.GREEN);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has said => " + msg +"\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Chat")) &&(main.sql.isConnected())){

            try {

                MySQLData.playerChat(serverName, worldName, playerName, msg, false);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
