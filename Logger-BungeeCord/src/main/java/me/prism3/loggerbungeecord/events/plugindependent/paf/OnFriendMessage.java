package me.prism3.loggerbungeecord.events.plugindependent.paf;

import de.simonsator.partyandfriends.api.events.message.FriendOnlineMessageEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnFriendMessage implements Listener {

    @EventHandler
    public void onMessage(final FriendOnlineMessageEvent event) {

        final ProxiedPlayer sender = event.getSender().getPlayer();
        final String receiver = event.getReceiver().getName();
        final String message = event.getMessage();

        System.out.println(sender + " " + receiver + " " + message);


    }
}
