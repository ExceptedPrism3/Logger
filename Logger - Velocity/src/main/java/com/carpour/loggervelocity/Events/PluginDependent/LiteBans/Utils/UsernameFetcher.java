package com.carpour.loggervelocity.Events.PluginDependent.LiteBans.Utils;

import litebans.api.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsernameFetcher {

    public static String playerNameFetcher(String uuid){

        String name = "";
        String query = "SELECT name from {history} WHERE uuid=? ORDER BY date DESC LIMIT 1";

        try (PreparedStatement st = Database.get().prepareStatement(query)) {

            st.setString(1, uuid);

            try (ResultSet rs = st.executeQuery()) {

                if (rs.next()) {

                    name = rs.getString("name");

                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
        return name;
    }
}
