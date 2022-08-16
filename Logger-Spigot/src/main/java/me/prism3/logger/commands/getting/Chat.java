package me.prism3.logger.commands.getting;

import me.prism3.logger.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Chat implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, String[] args) {
    try {
      if (args.length == 2) {
        int currentPage = Integer.parseInt(args[1]) - 1;
        if (currentPage < 0) {
          sender.sendMessage(ChatColor.RED + "Allowed page values are 1 - *!");
          return true;
        }
        Pager pager = new Pager(Main.getInstance().getDatabase().getPlayerChatCount(args[0]), 10, 5,
                currentPage);
        PlayerMessageSearch playerMessageSearch =
                new PlayerMessageSearch(args[0], sender, pager, label);
        playerMessageSearch.fetchAndSendResults();
      } else if (args.length == 1) {
        int currentPage = 0;
        Pager pager = new Pager(Main.getInstance().getDatabase().getPlayerChatCount(args[0]), 10, 5,
                currentPage);

        PlayerMessageSearch playerMessageSearch =
            new PlayerMessageSearch(args[0], sender, pager, label);
        playerMessageSearch.fetchAndSendResults();
      } else
        sender.sendMessage(ChatColor.RED + command.getUsage());

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
