package me.prism3.loggerbungeecord.utils;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggercore.database.utils.OsUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class PluginUpdater {

    public static boolean isUpdated = false;
    private String newTag = "";
    private static final File updateFolder = new File(Main.getInstance().getDataFolder().getParentFile(), "update");

    public void run() { this.checkForUpdate(); }

    /**
     * Check for a newer version of Logger every 12hrs
     * If present Download > Store > Auto Install on Server Restart
     */
    private void checkForUpdate() {

        try (final InputStream input = new URL(resourceAPIChecker).openStream();
             final BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {

            final StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);

            new JSONObject(stringBuilder);

            this.newTag = stringBuilder.toString();

            this.pluginVersionChecker(stringBuilder.toString());

        } catch (final IOException e) { Log.severe("Could not check for updates."); }
    }

    private void pluginVersionChecker(final String remote) {

        if (this.versionTagChecker(remote)) {

            if (!updateFolder.exists())
                updateFolder.mkdir();

            if (this.isThere()) {

                Log.info("An update of the plugin is ready to be installed. Restart your server to install.");
                isUpdated = true;
                return;
            }

            Log.info("Update Found! Attempt to download...");

            try {

                Files.copy(new URL("https://github.com/ExceptedPrism3/Logger/releases/download/v" + remote + "/Logger-Shaded-" + remote + ".jar").openStream(),
                        Paths.get(updateFolder + File.separator + "Logger.jar"));

                Log.info("The new version has been successfully downloaded and stored. Restart your server to auto-install." +
                        " Current Version: " + pluginVersion + ", New Version: " + this.newTag);

                isUpdated = true;

            } catch (final IOException e) {

                Log.severe("An error has occurred whist downloading the new plugin version." +
                        " You still can download it manually via the following link: " + resourceLink);
            }
        }
        else Log.info("You're using the latest plugin version");
    }

    private boolean versionTagChecker(final String remoteVersion) {

        if (remoteVersion.equalsIgnoreCase(pluginVersion))
            return false;

        final String[] remote = remoteVersion.split("\\.");
        final String[] local = pluginVersion.split("\\.");
        final int length = Math.max(local.length, remote.length);

        try {
            for (int i = 0; i < length; i++) {

                final int localNumber = i < local.length ? Integer.parseInt(local[i]) : 0;
                final int remoteNumber = i < remote.length ? Integer.parseInt(remote[i]) : 0;

                if (remoteNumber > localNumber)
                    return true;

                if (remoteNumber < localNumber)
                    return false;
            }
        } catch (final NumberFormatException ex) {

            Log.warning("An Error has occurred whilst reading the version tag. If this issue persists contact the Authors.");
        }

        return false;
    }

    private boolean isThere() {

        final File newVersion = new File(updateFolder, "Logger.jar");

        return newVersion.exists();
    }

    public static void movePlugin() {

        if (OsUtils.isWindows() || !OsUtils.isKnownOS()) {

            Log.severe("Auto-Plugin installing is not supported on this OS. You'll have to do it manually.");
            return;
        }

        final File newV = new File(updateFolder, "Logger.jar");

        try {

            Files.delete(Main.getInstance().getFile().toPath());

            Files.move(newV.toPath(), Main.getInstance().getDataFolder().getParentFile().toPath().resolve(newV.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) { e.printStackTrace(); }
    }
}

