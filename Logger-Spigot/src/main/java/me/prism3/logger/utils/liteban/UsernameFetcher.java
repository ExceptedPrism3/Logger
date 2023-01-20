package me.prism3.logger.utils.liteban;

import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsernameFetcher {

    private UsernameFetcher() {}

    public static String playerNameFetcher(String uuid) {

        String name = "";
        final String query = "SELECT name FROM {history} WHERE uuid = ? ORDER BY date DESC LIMIT 1";

        try (final PreparedStatement st = Database.get().prepareStatement(query);
             final ResultSet rs = st.executeQuery()) {

            st.setString(1, uuid);

            if (rs.next())
                name = rs.getString("name");

        } catch (SQLException e) { e.printStackTrace(); }

        return name;
    }
}
