package me.prism3.logger.utils.updater;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;

public class RunUpdater {

    public void updateChecker() {

        Log.info("Checking for updates...");

        Updater updater = new BukkitUpdater(Main.getInstance(), 669177, Updater.UpdateType.VERSION_CHECK).check();

        if (updater.getResult() != Updater.UpdateResult.UPDATE_AVAILABLE) {
            updater = new SpigotUpdater(Main.getInstance(), 94236, Updater.UpdateType.VERSION_CHECK).check();
        }

        if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {

            if (Main.getInstance().getConfig().getBoolean("Updater.Checker")) {

                if (updater.downloadFile()) {

                    Log.info("§aUpdate downloaded, it will be applied automatically on the next server restart");
                    Log.info("§aUpdate downloaded to your update folder, it will be applied automatically on the next server restart");
                    return;
                }
                Log.info("Could not download latest update. Please update manually from one of the links below.");
            }

            Log.info("§aFollow the link to download: §8" + updater.getUpdateLink());

        } else { Log.info("No update found"); }
    }
}
