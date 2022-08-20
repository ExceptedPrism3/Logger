package me.prism3.logger.events.misc;

import com.carpour.loggercore.database.data.Coordinates;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerExempt;
import static me.prism3.logger.utils.Data.loggerStaffLog;

public class ArmorStandPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandPlace(CreatureSpawnEvent event) {

        if (event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {

//            event.get

            System.out.println(event.getSpawnReason());

            System.out.println(event.getLocation().getBlockX() + " " + event.getLocation().getBlockY() + " " + event.getLocation().getBlockZ());
        }
    }
}
