package me.prism3.logger.serverside;

import org.bukkit.Bukkit;

import static me.prism3.logger.utils.Data.playerCountNumber;

public class PlayerCount implements Runnable {

    public void run() {

        int players = Bukkit.getServer().getOnlinePlayers().size();

        if (players >= playerCountNumber) {

            System.out.println(":D");

        }
    }
}
