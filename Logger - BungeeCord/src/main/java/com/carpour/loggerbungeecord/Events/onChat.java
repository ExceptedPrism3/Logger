package com.carpour.loggerbungeecord.Events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onChat implements Listener {


    @EventHandler
    public void onPlayerChat(ChatEvent event){

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String message = event.getMessage();
        ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();

        System.out.println("Player " + player + " Message " + message + " receiver " + receiver);


    }
}
