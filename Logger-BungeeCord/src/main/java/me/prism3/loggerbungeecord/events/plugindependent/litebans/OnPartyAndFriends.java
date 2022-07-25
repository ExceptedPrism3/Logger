package me.prism3.loggerbungeecord.events.plugindependent.litebans;

import de.simonsator.partyandfriends.api.events.message.FriendMessageEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnPartyAndFriends implements Listener {

    @EventHandler
    public void onMessage(final FriendMessageEvent event) {

        final ProxiedPlayer sender = event.getSender().getPlayer();
        final ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
        final String message = event.getMessage();

        System.out.println(sender + " " + receiver + " " + message);


    }

}
