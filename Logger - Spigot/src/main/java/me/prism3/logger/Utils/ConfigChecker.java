package me.prism3.logger.Utils;

import me.prism3.logger.Main;

import java.util.Arrays;

public class ConfigChecker {

    private boolean isGood = true;
    private boolean isUpdated = true;

    public ConfigChecker() {

        final String[] confVer = Data.configVersion.split("\\.");

        if (confVer.length != 3) {

            this.isGood = false;
            Main.getInstance().getLogger().severe("Invalid Config Version " + Arrays.toString(confVer) + ". Disabling...");
            return;
        }

        final String[] pluginVer = Data.pluginVersion.split("\\.");

        final int confMajor = Integer.parseInt(confVer[0]);
        final int confMinor = Integer.parseInt(confVer[1]);
        final int confPatch = Integer.parseInt(confVer[2]);

        final int pluginMajor = Integer.parseInt(pluginVer[0]);
        final int pluginMinor = Integer.parseInt(pluginVer[1]);
        final int pluginPatch = Integer.parseInt(pluginVer[2]);

        if (confMajor != pluginMajor || confMinor != pluginMinor || confPatch != pluginPatch) {

            final Main main = Main.getInstance();
            main.getLogger().severe("Plugin Configs are not up to date!");
            main.getLogger().severe("You must update them to avoid any complications.");
            this.isUpdated = false;

        }
    }

    public boolean getIsGood() { return this.isGood; }

    public boolean getIsUpdated() { return this.isUpdated; }
}
