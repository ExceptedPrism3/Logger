package com.carpour.logger.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordStatus {

    Discord discord = new Discord();
    JDA jda = discord.getJda();

    int currentIndex = 0;
    List<List<String>> activities = (List<List<String>>) DiscordFile.get().get("ActivityCycling.Activities");

    private final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    public DiscordStatus() {

        threadPool.scheduleWithFixedDelay(() -> {

            jda.getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(
                    activities.get(currentIndex).get(0).replaceAll("playing", "streaming")
                            .toUpperCase()), activities.get(currentIndex).get(1)));

            currentIndex = (currentIndex + 1) % activities.size();

        }, 0, DiscordFile.get().getInt("ActivityCycling.Time"), TimeUnit.SECONDS);
    }

    public ScheduledExecutorService getThreadPool(){ return threadPool; }

}
