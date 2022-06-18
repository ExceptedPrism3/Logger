package me.prism3.logger.commands.getting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.prism3.logger.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class PlayerMessageSearch {

  public static Connection getNewConnection() throws SQLException {
    return Main.getInstance().getExternal().getHikari().getConnection();
  }

  private CommandSender sender;
  private String searchedPlayer;
  private Pager pager;
  private String commandLabel;

  /**
   *
   * @param searchedPlayer player name to search
   * @param sender Entity that initiated the search
   * @param pager Pager
   * @param commandLabel command label
   *
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
    String sql =
        "SELECT message, id, date FROM player_chat WHERE player_name=? ORDER BY  DATE ASC, id DESC LIMIT 10 OFFSET ?";
    try (Connection connection = PlayerMessageSearch.getNewConnection();
        final PreparedStatement getStatement = connection.prepareStatement(sql)) {
      getStatement.setString(1, this.searchedPlayer);
      getStatement.setInt(2, this.pager.getCurrentOffset());
      ResultSet rs = getStatement.executeQuery();
      this.sendResults(rs);

      if (pager.getCurrentOffset() > pager.getCount()) {
        this.sender.sendMessage("No results found for that page!");
        return;
      }
      this.sender.spigot()
          .sendMessage(this.pager.preparePaging(this.commandLabel, this.searchedPlayer));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param playerName searched player name
   * @return int
   */
  public static int getMessagesCount(String playerName) {
    try (Connection connection = PlayerMessageSearch.getNewConnection();
        final PreparedStatement getStatement = connection
            .prepareStatement("SELECT COUNT(*) as total FROM player_chat WHERE player_name = ?")) {
      getStatement.setString(1, playerName);
      ResultSet rs = getStatement.executeQuery();

      rs.next();

      return rs.getInt("total");
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * Send results to sender
   * 
   * @param ResultSet rs
   *
   */
  private void sendResults(ResultSet rs) {
    // TODO format datetime 
    try {
      while (rs.next()) {
        this.sender.sendMessage(ChatColor.RED + rs.getTimestamp("date").toString() + " "
            + searchedPlayer + ": " + ChatColor.GREEN + rs.getString("Message"));
      }
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
