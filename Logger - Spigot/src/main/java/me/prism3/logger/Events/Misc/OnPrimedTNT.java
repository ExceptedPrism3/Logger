package me.prism3.logger.Events.Misc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class OnPrimedTNT implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTntPrime(final EntityExplodeEvent event){

        if (event.getEntity() instanceof TNTPrimed){

            Entity causer = ((TNTPrimed) event.getEntity()).getSource();

            if (causer == null) return;

            String causerName = causer.getName();
            int x = event.getLocation().getBlockX();
            int y = event.getLocation().getBlockY();
            int z = event.getLocation().getBlockZ();

            System.out.println(causerName + event.getLocation());



        }

    }

//    public PrimedTNT(){
//
//    }
//
//    private void run(Player player){
//
//        Entity entity = null;
//
//        if (entity instanceof TNTPrimed){
//
//            Entity causer =
//
//        }
//
//
//    }
}
