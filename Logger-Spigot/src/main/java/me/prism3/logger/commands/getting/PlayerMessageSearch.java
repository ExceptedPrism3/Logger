package me.prism3.logger.commands.getting;

import com.carpour.loggercore.database.entity.PlayerChat;
import me.prism3.logger.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PlayerMessageSearch {
  //FIXME
  private final CommandSender sender;
  private final String searchedPlayer;
  private final Pager pager;
  private final String commandLabel;

  /**
   * @param searchedPlayer player name to search
   * @param sender         Entity that initiated the search
   * @param pager          Pager
   * @param commandLabel   command label
   */
  public PlayerMessageSearch(String searchedPlayer, CommandSender sender, Pager pager,
                             String commandLabel) {
    this.searchedPlayer = searchedPlayer;
    this.sender = sender;
    this.pager = pager;
    this.commandLabel = commandLabel;
  }

  /**
   * Fetches results from database then sends results to the sender
   */
  public void fetchAndSendResults() {

    this.sendResults(Main.getInstance().getDatabase()
            .getPlayerChatByPlayerName(searchedPlayer, this.pager.getCurrentOffset(), 10));

    if (pager.getCurrentOffset() > pager.getCount()) {
      this.sender.sendMessage("No results found for that page!");
      return;
    }
    this.sender.spigot()
            .sendMessage(this.pager.preparePaging(this.commandLabel, this.searchedPlayer));

  }

  /**
   * Send results to sender
   */
  private void sendResults(List<PlayerChat> rs) {
    // TODO format datetime 
    try {
      for (PlayerChat a : rs) {
        this.sender.sendMessage(ChatColor.RED + a.getDate().toString() + " "
                + searchedPlayer + ": " + ChatColor.GREEN + a.getMessage());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
