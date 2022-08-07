package me.prism3.logger.events.plugindependent;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.entity.Player;

public class OnViaVer {

    public void onConnect(Player player) {

        ViaAPI via = Via.getAPI();

//        System.out.println(via.getConnection(player.getUniqueId()).getProtocolInfo().getProtocolVersion());
//        System.out.println(via.getConnection(player.getUniqueId()).getProtocolInfo().getServerProtocolVersion());
//        System.out.println(via.getVersion());
//        System.out.println(via.getPlayerVersion(player.getUniqueId()));
//        System.out.println(via.getServerVersion());
//        System.out.println(via.getVersion());


//        if(via.getPlayerVersion(player) == ProtocolVersion.v1_8.getId()) {
//            player.sendMessage("You are on 1.8 !");
//
//        }

    }
}
