package me.prism3.loggerbungeecord.hooks;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.events.plugindependent.paf.OnFriendMessage;
import me.prism3.loggerbungeecord.events.plugindependent.paf.OnPartyMessage;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.plugin.Plugin;

public class PartyAndFriendsUtil {

    private PartyAndFriendsUtil() {}

    public static boolean isAllowed = false;

    public static void getPartyAndFriendsHook() {

        if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.PAF")) {

            if (Main.getInstance().getConfig().getBoolean("PAF.Friend-Message"))
                Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), new OnFriendMessage());

            if (Main.getInstance().getConfig().getBoolean("PAF.Party-Message"))
                Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), new OnPartyMessage());

            Log.info("PartyAndFriends Plugin Detected!");

            isAllowed = true;
        }
    }

    private static Plugin getPartyAndFriendsAPI() { return Main.getInstance().getProxy().getPluginManager().getPlugin("PartyAndFriends"); }
}
