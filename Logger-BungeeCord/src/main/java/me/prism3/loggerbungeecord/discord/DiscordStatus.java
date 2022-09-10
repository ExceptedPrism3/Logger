package me.prism3.loggerbungeecord.discord;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordStatus {

    private final Main main = Main.getInstance();
    private final JDA jda;

    private int currentIndex = 0;
    private final List<List<String>> activities = (List<List<String>>) this.main.getDiscordFile().get().get("ActivityCycling.Activities");

    private static final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    public DiscordStatus(JDA jda) {

        this.jda = jda;

        try {

            assert this.activities != null;
            this.activities.forEach((list -> Activity.ActivityType.valueOf(list.
                    get(0).replace("playing", "streaming").toUpperCase())));

        } catch (final Exception exception) {

            Log.severe("Discord Status Activity is invalid. It has been disabled.");
            return;

        }

        if (this.main.getDiscordFile().get().getBoolean("ActivityCycling.Random"))
            Collections.shuffle(this.activities);

        threadPool.scheduleWithFixedDelay(() -> {

            this.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(
                    this.activities.get(this.currentIndex).get(0).replace("playing", "streaming")
                            .toUpperCase()), this.activities.get(this.currentIndex).get(1)));

            this.currentIndex = (this.currentIndex + 1) % this.activities.size();

        }, 0, this.main.getDiscordFile().get().getInt("ActivityCycling.Time"), TimeUnit.SECONDS);
    }

    public static ScheduledExecutorService getThreadPool() { return threadPool; }

}
