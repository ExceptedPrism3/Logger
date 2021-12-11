package org.carour.loggercore.discord;


import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public abstract class DiscordOptions {

    File file;

    private boolean staffEnabled;
    private boolean playerChatEnabled;
    private boolean playerCommandsEnabled;
    private boolean consoleCommandsEnabled;
    private boolean playerSignTextEnabled;
    private boolean playerJoinEnabled;
    private boolean playerLeaveEnabled;
    private boolean playerKickEnabled;
    private boolean playerDeathEnabled;
    private boolean playerTeleportEnabled;
    private boolean playerLevelEnabled;
    private boolean blockBreakEnabled;
    private boolean blockPlaceEnabled;
    private boolean portalCreateEnabled;
    private boolean bucketPlaceEnabled;
    private boolean anvilUseEnabled;
    private boolean tpsEnabled;
    private boolean ramEnabled;
    private boolean serverStartEnabled;
    private boolean serverStopEnabled;
    private boolean itemDropEnabled;
    private boolean itemPickupEnabled;
    private boolean enchantEnabled;
    private boolean bookEditEnabled;
    private boolean furnaceEnabled;
    private boolean afkEnabled;

    public DiscordOptions() {
    }


    protected abstract DiscordOptions construct();

}
