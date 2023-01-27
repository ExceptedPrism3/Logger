package me.prism3.logger.utils.updater;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.json.JSONObject;

public class PluginUpdater {

    private final String resourceAPI = "https://api.github.com/repos/ExceptedPrism3/Logger/releases/latest";
    private final String currentVersion;
    private final File updateFolder = Bukkit.getUpdateFolderFile();
    private final long checkInterval = TimeUnit.HOURS.toMillis(12);
    private final ConcurrentHashMap<String, JSONObject> cache = new ConcurrentHashMap<>();
    private long lastCheck;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public PluginUpdater(String currentVersion) { this.currentVersion = currentVersion; }

    public void checkForUpdates() {

        if (System.currentTimeMillis() - this.lastCheck < this.checkInterval)
            return;

        this.threadPool.submit(() -> {
            final JSONObject json;

            if (this.cache.containsKey(this.resourceAPI)) {

                json = this.cache.get(this.resourceAPI);

            } else {

                try {

                    final URL url = new URL(this.resourceAPI);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Accept", "application/vnd.github+json");

                    if (conn.getResponseCode() != 200) {
                        Log.severe("Error checking for updates. Got response code " + conn.getResponseCode());
                        return;
                    }

                    try (final InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                         final BufferedReader buffer = new BufferedReader(reader)) {

                        final StringBuilder response = new StringBuilder();
                        buffer.lines().forEach(response::append);

                        json = new JSONObject(response.toString());
                        this.cache.put(this.resourceAPI, json);
                    }

                } catch (final IOException e) {
                    Log.severe("Error checking for updates: " + e.getMessage());
                    return;
                }
            }

            final String latestTag = json.getString("tag_name");

            if (this.compareVersions(this.currentVersion, latestTag.replace("v", "")) < 0) {

                Log.info("A new update is available: " + latestTag);
                Log.info("Downloading...");

                final String downloadUrl = json.getJSONArray("assets")
                        .getJSONObject(0)
                        .getString("browser_download_url");

                Bukkit.getUpdateFolderFile().mkdirs();

                this.downloadFile(downloadUrl, new File(this.updateFolder, "Logger.jar"));

                Log.info("The new version " + latestTag + " has been downloaded. It will be auto-installed on server restart.");

            } else { Log.info("You are using the latest version: " + this.currentVersion); }

            this.lastCheck = System.currentTimeMillis();
        });
    }

    private void downloadFile(String url, File file) {
        try {
            final URL downloadUrl = new URL(url);
            try (InputStream in = downloadUrl.openStream()) {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (final IOException e) { Log.severe("Error downloading file: " + e.getMessage()); }
    }

    private int compareVersions(String current, String latest) {

        final String[] currentParts = current.split("\\.");
        final String[] latestParts = latest.split("\\.");

        for (int i = 0; i < Math.max(currentParts.length, latestParts.length); i++) {

            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (currentPart != latestPart)
                return currentPart - latestPart;
        }
        return 0;
    }
}
