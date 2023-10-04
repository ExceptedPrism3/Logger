package me.prism3.logger.database.external;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalUpdater {

  private ExternalUpdater() {}

  public static List<String> getLoggerTables(ResultSet dbTables) {
    try {
      String name;
      List<String> currentTables = new ArrayList<>(40);
      List<String> oldTables = ExternalData.getTableNames();

      while (dbTables.next()) {
        name = dbTables.getString("TABLE_NAME");

        if (oldTables.stream().anyMatch(name::equalsIgnoreCase))
          currentTables.add(name);
      }

      return currentTables;
    } catch (Exception e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  public static void updater() {
    final Main main = Main.getInstance();

    if (Data.isExternal && main.getExternal().isConnected()) {
      // Primary Key removal and adding method
      try (final Connection connection = main.getExternal().getHikari().getConnection();
           final Statement stsm = connection.createStatement()) {

        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        final ResultSet tables = databaseMetaData.getTables(Data.dbName, null, null, null);
        List<String> currentTables = ExternalUpdater.getLoggerTables(tables);

        int j;

        for (j = 0; j < currentTables.size(); j++) {
          stsm.execute("ALTER TABLE " + currentTables.get(j) + " CONVERT TO CHARACTER SET utf8mb4");

          ResultSet primaryKeys =
                  databaseMetaData.getPrimaryKeys(Data.dbName, null, currentTables.get(j));

          ResultSet columns =
                  databaseMetaData.getColumns(Data.dbName, null, currentTables.get(j), null);
          while (primaryKeys.next()) {
            final String pkName = primaryKeys.getString("COLUMN_NAME");

            if (pkName.contains("Date")) {
              stsm.executeUpdate("ALTER TABLE " + currentTables.get(j) + " DROP PRIMARY KEY");
            }

            if (!pkName.contains("id")) {
              stsm.executeUpdate("ALTER TABLE " + currentTables.get(j)
                      + " ADD id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT FIRST");
            }
          }

          String columnName;
          String lowerName;

          while (columns.next()) {
            columnName = columns.getString("COLUMN_NAME");
            List<String> x = new ArrayList<>();
            int m;

            for (m = 1; m <= 24; m++)
              x.add(columns.getString(m));

            String dataType = columns.getString(6);
            String length = String.valueOf(columns.getInt(7));
            String nullType = columns.getBoolean(18) ? "NULL" : "NOT NULL";
            String defaultVal = columns.getString(13);
            String attribute;
            dataType = dataType.equalsIgnoreCase("DATETIME") ? "DATETIME(6)"
                    : dataType + "(" + length + ") ";

            if (dataType.contains("UNSIGNED")) {
              String[] arr = dataType.split(" ");
              dataType = arr[0];
              attribute = arr[1].replaceAll("[0-9()]", "");
              dataType = dataType + "(" + length + ") " + attribute;
            }

            lowerName = columnName.toLowerCase();
            if (columnName.contains("Playername")) {
              stsm.executeUpdate("ALTER TABLE " + currentTables.get(j) + " CHANGE COLUMN "
                      + columnName + " " + "player_name" + "  " + dataType + " " + nullType
                      + " DEFAULT " + defaultVal);
            }

            if (!columnName.equals(lowerName)) {

              String sql = "ALTER TABLE " + currentTables.get(j) + " CHANGE COLUMN " + columnName
                      + " " + lowerName + "  " + dataType + " " + nullType + " DEFAULT " + defaultVal;
              stsm.executeUpdate(sql);

            }
          }

          String oldName = currentTables.get(j);
          String newName = currentTables.get(j).toLowerCase();

          if (!oldName.equals(newName)) {
            stsm.executeUpdate("RENAME TABLE " + oldName + " TO " + newName);
          }
        }

      } catch (Exception e) {
        Log.severe("Unable to update the tables. If the issue persists contact the Authors!");
        e.printStackTrace();
      }
    }
  }
}
