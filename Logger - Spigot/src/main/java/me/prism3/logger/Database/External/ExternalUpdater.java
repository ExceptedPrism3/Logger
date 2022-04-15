package me.prism3.logger.Database.External;

import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;

import java.sql.*;
import java.util.List;


public class ExternalUpdater {

    public ExternalUpdater() {

        final Main main = Main.getInstance();

        int i = 0;
        boolean keys = false;
        boolean playerName = false;

        if (Data.isExternal && main.getExternal().isConnected()) {
            final Connection connection = main.getExternal().getConnection();
            final List<String> tableNames = ExternalData.getTableNames();
            

            // Primary Key removal and adding method
            try {
                Statement stsm = connection.createStatement();
                int j = 0;
                for (j = 0; j< tableNames.size() ;j++) {
                    final DatabaseMetaData databaseMetaData = connection.getMetaData();
                    final ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(Data.dbName, null, tableNames.get(j));
                    final ResultSet columns = databaseMetaData.getColumns(Data.dbName, null, tableNames.get(j), null);
                    while (primaryKeys.next()) {
                        final String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (pkName.contains("Date")) {
                            stsm.executeUpdate("ALTER TABLE " + tableNames.get(i) + " DROP PRIMARY KEY");
                            keys = true;
                        }
                        if(!pkName.contains("id"))
                        {
                            stsm.executeUpdate("ALTER TABLE " + tableNames.get(i) + " ADD id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT FIRST");

                        }
                    }
                
                    while(columns.next())
                    {
                        
                        if(columns.getString("COLUMN_NAME").contains("Playername"))
                        {
                            stsm.executeUpdate("ALTER TABLE " + tableNames.get(j) + " RENAME COLUMN Playername to Player_Name");
                            playerName = true;
                        }
                    }


                  
                
                stsm.close();
                }
            } catch (Exception e) {

                main.getLogger().severe("Unable to update the tables. If the issue persists contact the Author!");
                e.printStackTrace();

            }
        }
        if (keys && playerName) main.getLogger().info("All Tables have been Updated!");
        }
}
