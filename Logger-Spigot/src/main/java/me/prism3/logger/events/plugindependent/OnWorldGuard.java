package me.prism3.logger.events.plugindependent;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.commands.CommandUtils;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.EntryFlag;
import com.sk89q.worldguard.session.handler.Handler;

import java.util.Set;

public class OnWorldGuard extends Handler {

    public static final Factory FACTORY = new Factory();

    public static class Factory extends Handler.Factory<EntryFlag> {
        @Override
        public EntryFlag create(Session session) {
            return new EntryFlag(session);
        }
    }

    private static final long MESSAGE_THRESHOLD = 1000 * 2;
    private long lastMessage;

    public OnWorldGuard(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {

        boolean allowed = toSet.testState(player, Flags.ENTRY);

        System.out.println("1");

        if (!getSession().getManager().hasBypass(player, (World) to.getExtent()) && !allowed && moveType.isCancellable()) {

            System.out.println("2");

            String message = toSet.queryValue(player, Flags.ENTRY_DENY_MESSAGE);
            long now = System.currentTimeMillis();

            if ((now - lastMessage) > MESSAGE_THRESHOLD && message != null && !message.isEmpty()) {
                player.printRaw(CommandUtils.replaceColorMacros(message));

                System.out.println("3");

                lastMessage = now;
            }

            return false;
        } else {
            System.out.println("true");
            return true;
        }
    }
}