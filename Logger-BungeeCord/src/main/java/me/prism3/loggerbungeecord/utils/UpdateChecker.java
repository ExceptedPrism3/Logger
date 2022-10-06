package me.prism3.loggerbungeecord.utils;

import me.prism3.loggerbungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    private static final String VERSION = Data.pluginVersion;
    private static final String DOWNLOAD_URL = "https://www.spigotmc.org/resources/logger.94236/updates/";

    public void checkUpdates() {

        ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {

            final String httpsUrl = "https://api.spigotmc.org/legacy/update.php?resource=94236/";

            try {

                final URL url = new URL(httpsUrl);
                final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                if (con != null) {

                    try {

                        final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String input;

                        while ((input = br.readLine()) != null) {

                            if (!input.trim().equalsIgnoreCase(VERSION.trim())) {

                                Log.info(ChatColor.GOLD + "A new version is available for download " + ChatColor.GREEN + VERSION);
                                Log.info(ChatColor.GOLD + "Download Link: " + ChatColor.GREEN + DOWNLOAD_URL);
                            }
                        }

                        br.close();

                    } catch (final IOException e) {

                        Log.warning("Could not check for Updates, if the issue persists contact the Authors!");
                        e.printStackTrace();
                    }
                }

            } catch (final IOException e) {

                Log.warning("Could not check for Updates, if the issue persists contact the Authors!");
                e.printStackTrace();
            }
        });
    }
}