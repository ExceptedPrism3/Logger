package me.prism3.loggerbungeecord.api;

import me.prism3.loggerbungeecord.Main;
import net.md_5.bungee.api.plugin.Plugin;

public class PartyAndFriendsUtil {

    private PartyAndFriendsUtil() {}

    public static Plugin getPartyAndFriendsAPI() {

        return Main.getInstance().getProxy().getPluginManager().getPlugin("PartyAndFriends");
    }
}
