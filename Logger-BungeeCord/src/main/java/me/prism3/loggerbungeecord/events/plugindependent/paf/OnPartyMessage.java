package me.prism3.loggerbungeecord.events.plugindependent.paf;

import de.simonsator.partyandfriends.api.events.message.PartyMessageEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static me.prism3.loggerbungeecord.utils.Data.loggerExempt;

public class OnPartyMessage implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessage(final PartyMessageEvent event) {

        if (!event.isCancelled()) {

            final ProxiedPlayer sender = event.getSender().getPlayer();

            if (sender.hasPermission(loggerExempt)) return;

            final UUID senderUUID = sender.getUniqueId();
            final String senderName = sender.getName();
            final String serverName = sender.getServer().getInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");
            final String partyLeader = event.getParty().getLeader().getName();
            final List<OnlinePAFPlayer> partyMembers = event.getParty().getAllPlayers();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && sender.hasPermission(Data.loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Extras.PAF-Party-Message-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%leader%", partyLeader).replace("%uuid%", senderUUID.toString()).replaceAll("%members%", String.valueOf(partyMembers)) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPafPartyMessageLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Extras.PAF-Party-Message").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%leader%", partyLeader).replace("%uuid%", senderUUID.toString()).replaceAll("%members%", String.valueOf(partyMembers)) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!sender.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && sender.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Extras.PAF-Party-Message-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(senderName, senderUUID, this.main.getMessages().getString("Discord.Extras.PAF-Party-Message-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%leader%", partyLeader).replace("%uuid%", senderUUID.toString()).replaceAll("%members%", String.valueOf(partyMembers)), false);
                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Extras.PAF-Party-Message").isEmpty()) {

                        this.main.getDiscord().pafPartyMessage(senderName, senderUUID, this.main.getMessages().getString("Discord.Extras.PAF-Party-Message").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", serverName).replace("%player%", sender.getName()).replace("%msg%", message).replace("%leader%", partyLeader).replace("%uuid%", senderUUID.toString()).replaceAll("%members%", String.valueOf(partyMembers)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

//                    Main.getInstance().getDatabase().insertPAFPartyMessage(Data.serverName, senderUUID.toString(), senderName, message, partyLeader, Collections.singletonList(partyMembers.toString()), sender.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

//                    Main.getInstance().getSqLite().insertPAFPartyMessage(Data.serverName, senderUUID.toString(), senderName, message, partyLeader, Collections.singletonList(partyMembers.toString()), sender.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
