package com.carpour.loggervelocity.Discord;

import com.carpour.loggervelocity.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordStatus {

    private final Discord discord = new Discord();
    private final JDA jda = discord.getJda();

    int currentIndex = 0;
    List<List<String>> activities = (List<List<String>>) DiscordFile.get().get("ActivityCycling.Activities");

    private static final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    public DiscordStatus() {

        try {

            assert activities != null;
            activities.forEach((list -> Activity.ActivityType.valueOf(list.
                    get(0).replace("playing", "streaming").toUpperCase())));

        }catch (Exception exception){

            Main.getInstance().getLogger().error("Discord Status Activity is invalid. It has been disabled.");
            return;

        }

        if (DiscordFile.get().getBoolean("ActivityCycling.Random")){

            Collections.shuffle(activities);

        }

        threadPool.scheduleWithFixedDelay(() -> {

            jda.getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(
                    activities.get(currentIndex).get(0).replaceAll("playing", "streaming")
                            .toUpperCase()), activities.get(currentIndex).get(1)));

            currentIndex = (currentIndex + 1) % activities.size();

        }, 0, DiscordFile.get().getInt("ActivityCycling.Time"), TimeUnit.SECONDS);
    }
    public static ScheduledExecutorService getThreadPool(){ return threadPool; }
}
