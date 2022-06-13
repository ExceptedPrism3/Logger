package me.prism3.logger.commands.getting;

import me.prism3.logger.database.external.ExternalData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

public class Chat implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        boolean containsResult;

        //Unfinished
        if (args.length == 2) {

            if (!args[1].matches("^[0-9]+$")) {
                sender.sendMessage(ChatColor.RED + "Use a numeric value for page number");
                return true;
            }

            try {
                int page = Integer.parseInt(args[1]);
                containsResult = Chat.sendResults(ExternalData.getMessages(args[0], page), sender, args[1]);

                if (!containsResult) return true;

                String nextPage = String.valueOf(page + 10);
                String prevPage = String.valueOf(page - 10);
                TextComponent paging = Chat.getTextComponent("next", "/loggerget " + args[0] + " " + nextPage, "Next Page");
                TextComponent back = Chat.getTextComponent("back", "/loggerget " + args[0] + " " + prevPage, "Previous Page");
                paging.addExtra("     -    ");
                paging.addExtra(back);
                sender.spigot().sendMessage(paging);

                return true;

            } catch (Exception e) { e.printStackTrace(); }

        } else if (args.length == 1) {


            containsResult = Chat.sendResults(ExternalData.getMessages(args[0], 0), sender, args[0]);
            if (!containsResult) return true;
            sender.spigot().sendMessage(Chat.getTextComponent("next", "/loggerget " + args[0] + " 10", "Next Page"));


        } else sender.sendMessage(ChatColor.RED + command.getUsage());

        return true;
    }

    public static TextComponent getTextComponent(String text, String commandString, String hoverString) {

        TextComponent tc = new TextComponent(text);
        tc.setColor(ChatColor.GRAY);
        tc.setBold(true);
        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverString).color(ChatColor.GRAY).italic(true).create()));
        return tc;
    }

    public static boolean sendResults(ResultSet rs, CommandSender sender, String searchedPlayer) {

        try {

            if (!rs.isBeforeFirst()) {
                sender.sendMessage("Empty results");
                return false;
            }

            while (rs.next()) {
                sender.sendMessage(ChatColor.RED + rs.getTimestamp("Date").toString() + " " + searchedPlayer + ": " + ChatColor.GREEN + rs.getString("Message"));
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
