package me.prism3.logger.Database.External;

import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ExternalUpdater {
    public static List<String> getLoggerTables(ResultSet a )
    {
        try
        { 
            String name; 
            List<String> currentTables = new ArrayList<String>(40);
            while(a.next())
            {
               name = a.getString("TABLE_NAME");
                if(ExternalData.getTableNames().contains(name))
                {
                    currentTables.add(name);
                }
               

            }

            
            return currentTables;
        }
        catch(Exception e) {
e.printStackTrace();
return null;
        }
        

    }

    public ExternalUpdater() {

        final Main main = Main.getInstance();

        boolean keys = false;
        boolean playerName = false;

        if (Data.isExternal && main.getExternal().isConnected()) {
            final Connection connection = main.getExternal().getConnection();

            // Primary Key removal and adding method
            try {
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            final ResultSet tables = databaseMetaData.getTables(Data.dbName, null, null, null);
            List <String> currentTables = ExternalUpdater.getLoggerTables(tables);

                Statement stsm = connection.createStatement();
                int j = 0;
                for (j = 0; j< currentTables.size() ;j++) {
                    final ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(Data.dbName, null, currentTables.get(j));
                    final ResultSet columns = databaseMetaData.getColumns(Data.dbName, null, currentTables.get(j), null);
                    while (primaryKeys.next()) {
                        final String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (pkName.contains("Date")) {
                        stsm.addBatch("ALTER TABLE " + currentTables.get(j) + " DROP PRIMARY KEY");
                            keys = true;
                        }
                        if(!pkName.contains("id"))
                        {
                            stsm.addBatch("ALTER TABLE " + currentTables.get(j) + " ADD id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT FIRST");

                        }
                    }
                
                    while(columns.next())
                    {
                        
                        if(columns.getString("COLUMN_NAME").contains("Playername"))
                        {
                            stsm.addBatch("ALTER TABLE " + currentTables.get(j) + " RENAME COLUMN Playername to Player_Name");
                            playerName = true;
                        }
                    }
    
  
       
                        String oldName = currentTables.get(j);
                        String newName = currentTables.get(j).toLowerCase();
                    
                        if(!oldName.equals(newName))
                        {
                            stsm.addBatch("RENAME TABLE "+ oldName+" TO " +newName);
                    
                        }
                    
                    

                  
                
               
                }
                stsm.executeBatch();



               

               
                
               
            
                stsm.close();
    
            } catch (Exception e) {

                main.getLogger().severe("Unable to update the tables. If the issue persists contact the Author!");
                e.printStackTrace();

            }
       


        if (keys && playerName) main.getLogger().info("All Tables have been Updated!");
        }
}
}
