package me.prism3.logger.commands.getting;

import me.prism3.logger.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Chat implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        try {

            if (args.length == 1) {

                int currentPage = 0;
                final Pager pager = new Pager(Main.getInstance().getDatabase().getPlayerChatCount(args[0]), 10, 5, currentPage);

                final PlayerMessageSearch playerMessageSearch = new PlayerMessageSearch(args[0], sender, pager, label);
                playerMessageSearch.fetchAndSendResults();

            } else if (args.length == 2) {

                int currentPage = Integer.parseInt(args[1]) - 1;

                if (currentPage < 0) {
                    sender.sendMessage(ChatColor.RED + "Allowed page values are 1 - *!");
                    return true;
                }

                final Pager pager = new Pager(Main.getInstance().getDatabase().getPlayerChatCount(args[0]), 10, 5, currentPage);
                final PlayerMessageSearch playerMessageSearch = new PlayerMessageSearch(args[0], sender, pager, label);
                playerMessageSearch.fetchAndSendResults();
            }

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length >= 3)
            return new ArrayList<>(GetCommandBuilder.commandNames);

        //TODO All subcommands are shown in the 3rd param of the player, you can change that from the if checker

        return null;
    }
}
