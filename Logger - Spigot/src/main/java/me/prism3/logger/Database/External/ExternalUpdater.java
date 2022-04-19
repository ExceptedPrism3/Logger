package me.prism3.logger.Database.External;

import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExternalUpdater {
    public static List<String> getLoggerTables(ResultSet dbTables) {
        try {
            String name;
            List<String> currentTables = new ArrayList<String>(40);
            List<String> oldTables = ExternalData.getTableNames();
            while (dbTables.next()) {
                name = dbTables.getString("TABLE_NAME");

                if (oldTables.stream().anyMatch(name::equalsIgnoreCase)) {
                    currentTables.add(name);
                }
            }

            return currentTables;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ExternalUpdater() {

        final Main main = Main.getInstance();

        if (Data.isExternal && main.getExternal().isConnected()) {
            final Connection connection = main.getExternal().getConnection();

            // Primary Key removal and adding method
            try {
                Long start = System.nanoTime();
                final DatabaseMetaData databaseMetaData = connection.getMetaData();
                final ResultSet tables = databaseMetaData.getTables(Data.dbName, null, null, null);
                List<String> currentTables = ExternalUpdater.getLoggerTables(tables);
                int j = 0;
                Statement stsm = connection.createStatement();

                for (j = 0; j < currentTables.size(); j++) {
                    ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(Data.dbName, null,
                            currentTables.get(j));
                    ResultSet columns = databaseMetaData.getColumns(Data.dbName, null, currentTables.get(j),
                            null);
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
                        int m = 1;

                        for (m = 1; m <= 24; m++) x.add(columns.getString(m));

                        String dataType = columns.getString(6);
                        String length = String.valueOf(columns.getInt(7));
                        String nullType = columns.getBoolean(18) ? "NULL" : "NOT NULL";
                        String defaultVal = columns.getString(13);
                        String attribute = "";
                        dataType = dataType.equalsIgnoreCase("DATETIME") ? "DATETIME(6)" : dataType + "(" + length + ") "; 

                        if(dataType.contains("UNSIGNED"))
                        {
                            String [] arr = dataType.split(" ");
                            dataType = arr[0];
                            attribute = arr[1].replaceAll("[0-9()]", "");
                            dataType = dataType + "(" + length + ") " + attribute;

                        }

                        lowerName = columnName.toLowerCase();
                        if (columnName.contains("Playername")) {
                            stsm.executeUpdate(
                                "ALTER TABLE " + currentTables.get(j) + " CHANGE COLUMN " + columnName + " "
                                + "player_name" + "  "+ dataType+ " " + nullType + " DEFAULT "
                                + defaultVal);
                        }


                        if (!columnName.equals(lowerName)) {
                            String sql = "ALTER TABLE " + currentTables.get(j) + " CHANGE COLUMN " + columnName + " "
                            + lowerName + "  "+ dataType+ " " + nullType + " DEFAULT "
                            + defaultVal;
                            stsm.executeUpdate(sql);
                        }

                    }

                    String oldName = currentTables.get(j);
                    String newName = currentTables.get(j).toLowerCase();

                    if (!oldName.equals(newName)) {
                        stsm.executeUpdate("RENAME TABLE " + oldName + " TO " + newName);

                    }

                }

                stsm.close();
                System.out.println(TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS));
            } catch (Exception e) {

                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                main.getLogger().severe("Unable to update the tables. If the issue persists contact the Author!");
                e.printStackTrace();

            }

        }
    }
}
