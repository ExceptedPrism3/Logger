package me.prism3.loggerbungeecord.events.plugindependent.paf;

import de.simonsator.partyandfriends.api.events.message.PartyMessageEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class OnPartyMessage implements Listener {

    @EventHandler
    public void onMessage(final PartyMessageEvent event) {

        final ProxiedPlayer sender = event.getSender().getPlayer();
        final List<OnlinePAFPlayer> partyMembers = event.getParty().getAllPlayers();
        final String partyLeader = event.getParty().getLeader().getName();
        final String message = event.getMessage();

        System.out.println(sender + " " + partyMembers + " " + message + " " + partyLeader);


    }
}
