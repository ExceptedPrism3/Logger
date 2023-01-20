package me.prism3.logger.discord;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordStatus {

    private final JDA jda;
    private final List<List<String>> activities;
    private static final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    private int currentIndex = 0;

    public DiscordStatus(JDA jda) {

        final Main main = Main.getInstance();

        this.jda = jda;

        this.activities = (List<List<String>>) main.getDiscordFile().get("ActivityCycling.Activities");

        if (this.activities != null && !this.activities.isEmpty()) {
            try {
                for (List<String> activity : activities)
                    Activity.ActivityType.valueOf(activity.get(0).replace("playing", "streaming").toUpperCase());

            } catch (final IllegalArgumentException e) {
                Log.severe("Discord Status Activity is invalid. It has been disabled.");
            }
        }

        assert this.activities != null;

        if (main.getDiscordFile().getBoolean("ActivityCycling.Random"))
            Collections.shuffle(this.activities);

        threadPool.scheduleAtFixedRate(() -> {


            this.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(
                    this.activities.get(this.currentIndex).get(0).replace("playing", "streaming")
                            .toUpperCase()), this.activities.get(this.currentIndex).get(1)));

            this.currentIndex = (this.currentIndex + 1) % this.activities.size();
        }, 0, main.getDiscordFile().getInt("ActivityCycling.Time"), TimeUnit.SECONDS);
    }

    public static ScheduledExecutorService getThreadPool() { return threadPool; }
}
