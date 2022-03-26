package me.prism3.loggervelocity.Database.External;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.Data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


public class ExternalUpdater {

    public ExternalUpdater() {
        final Main main = Main.getInstance();

        int i = 0;
        boolean keys = false;
        boolean playerName = false;

        if (Data.isExternal && External.isConnected()) {

            final List<String> index = ExternalData.getTablesNames();

            // Primary Key removal Method
            try {

                for (String ignored : index) {

                    final Connection connection = main.getExternal().getConnection();
                    final DatabaseMetaData databaseMetaData = connection.getMetaData();

                    final ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, index.get(i));

                    while (resultSet.next()) {

                        final String primaryKeys = resultSet.getString(4);

                        if (primaryKeys.contains("Date")) {

                            final PreparedStatement ps1 = connection.prepareStatement("ALTER TABLE " + index.get(i) + " DROP PRIMARY KEY");
                            ps1.executeUpdate();
                            ps1.close();

                            keys = true;
                        }
                    }

                    // Playername Renaming method

                    final PreparedStatement desc = connection.prepareStatement("DESC " + index.get(i));
                    final ResultSet query = desc.executeQuery();

                    while (query.next()) {

                        final String field = query.getString("Field");

                        if (field.contains("Playername")) {

                            final PreparedStatement pS2 = connection.prepareStatement("ALTER TABLE " + index.get(i) + " CHANGE Playername Player_Name Varchar(30)");
                            pS2.executeUpdate();
                            pS2.close();

                            playerName = true;
                        }
                    }
                    i++;
                }
            } catch (Exception e) {

                main.getLogger().error("Unable to update the tables. If the issue persists contact the Author!");
                e.printStackTrace();

            }
        }
        if (keys && playerName) main.getLogger().info("All Tables have been Updated!");
    }
}
