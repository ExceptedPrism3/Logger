package com.carpour.loggerbungeecord.Utils;

import com.carpour.loggerbungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static com.carpour.loggerbungeecord.Utils.Data.pluginVersion;

public class UpdateChecker {

    private final String version = pluginVersion;
    private final String downloadURL = "https://www.spigotmc.org/resources/logger.94236/updates/";

    public void checkUpdates() {

        ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {

            final String https_url = "https://api.spigotmc.org/legacy/update.php?resource=94236/";

            try {

                final URL url = new URL(https_url);
                final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                if (con != null) {

                    try {

                        final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String input;

                        while ((input = br.readLine()) != null) {

                            if (!input.trim().equalsIgnoreCase(version.trim())) {

                                Main.getInstance().getLogger().info(ChatColor.GOLD + "A new version is available for download " + ChatColor.GREEN + version);
                                Main.getInstance().getLogger().info(ChatColor.GOLD + "Download Link: " + ChatColor.GREEN + downloadURL);

                            }
                        }

                        br.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("Could not check for Updates, if the issue persists contact the Authors!");

                        e.printStackTrace();

                    }
                }

            } catch (IOException e) {

                Main.getInstance().getLogger().warning("Could not check for Updates, if the issue persists contact the Authors!");

                e.printStackTrace();

            }
        });
    }
}