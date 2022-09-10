package me.prism3.loggerbungeecord.events.plugindependent.paf;

import de.simonsator.partyandfriends.api.events.message.FriendOnlineMessageEvent;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OnFriendMessage implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onMessage(final FriendOnlineMessageEvent event) {

        if (!event.isCancelled()) {

            final ProxiedPlayer sender = (ProxiedPlayer) event.getSender();

            if (sender.hasPermission(Data.loggerExempt)) return;

            final UUID senderUUID = sender.getUniqueId();
            final String senderName = sender.getName();
            final String receiverName = event.getReceiver().getName();
            final String serverName = sender.getServer().getInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && sender.hasPermission(Data.loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Extras.PAF-Friend-Message-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%receiver%", receiverName).replace("%uuid%", senderUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPafFriendMessageLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Extras.PAF-Friend-Message").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%receiver%", receiverName).replace("%uuid%", senderUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord Integration
            if (!sender.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && sender.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Extras.PAF-Friend-Message-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(senderName, senderUUID, this.main.getMessages().getString("Discord.Extras.PAF-Friend-Message-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%receiver%", receiverName).replace("%uuid%", senderUUID.toString()), false);

                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Extras.PAF-Friend-Message").isEmpty()) {

                        this.main.getDiscord().pafFriendMessage(senderName, senderUUID, this.main.getMessages().getString("Discord.Extras.PAF-Friend-Message").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%receiver%", receiverName).replace("%uuid%", senderUUID.toString()), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPAFFriendMessage(Data.serverName, senderUUID.toString(), senderName, message, receiverName, sender.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPAFFriendMessage(Data.serverName, senderUUID.toString(), senderName, message, receiverName, sender.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
