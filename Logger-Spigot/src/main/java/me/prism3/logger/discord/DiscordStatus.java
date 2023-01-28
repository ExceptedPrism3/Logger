package me.prism3.logger.discord;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordStatus {

    private final JDA jda;
    private final List<List<String>> activities;
    private final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    private int currentIndex = 0;

    public DiscordStatus(JDA jda) {

        final Main main = Main.getInstance();
        final FileConfiguration discordFile = main.getDiscordFile().get();
        this.jda = jda;

        String discordStatus = discordFile.getString("ActivityCycling.Status");

        if (discordStatus != null && !discordStatus.isEmpty()
                && Arrays.asList("online", "idle", "dnd", "busy", "invisible").contains(discordStatus)) {

            if (discordStatus.equalsIgnoreCase("dnd") || discordStatus.equalsIgnoreCase("busy"))
                discordStatus = "DO_NOT_DISTURB";

            jda.getPresence().setStatus(OnlineStatus.valueOf(discordStatus.toUpperCase()));
        } else {
            Log.severe("Invalid status: " + discordStatus + ". Valid options are online, idle, dnd, and invisible.");
        }

        this.activities = (List<List<String>>) discordFile.get("ActivityCycling.Activities");

        if (activities == null || this.activities.isEmpty() || activities.get(0) == null) {
            Log.warning("No discord activities provided, disabling...");
            return;
        }

        for (List<String> activity : activities) {
            try {
                Activity.ActivityType.valueOf(activity.get(0).replace("playing", "streaming").toUpperCase());
            } catch (final IllegalArgumentException e) {
                Log.severe("Discord activity is invalid, disabling...");
                return;
            }
        }

        if (discordFile.getBoolean("ActivityCycling.Random"))
            Collections.shuffle(this.activities);

        final int delay = discordFile.getInt("ActivityCycling.Time");

        threadPool.scheduleAtFixedRate(() -> {
            this.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(
                    this.activities.get(this.currentIndex).get(0).replace("playing", "streaming")
                            .toUpperCase()), this.activities.get(this.currentIndex).get(1)));
            this.currentIndex = (this.currentIndex + 1) % this.activities.size();
        }, 0, delay, TimeUnit.SECONDS);
    }

    public void shutdownThreadPool() { this.threadPool.shutdownNow(); }
}
