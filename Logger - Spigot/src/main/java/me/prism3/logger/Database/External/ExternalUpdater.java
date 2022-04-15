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
            List<String> currentTables = new ArrayList<>(40);
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
            final List<String> tableNames = ExternalData.getTableNames();




            // Primary Key removal and adding method
            try {
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            final ResultSet tables = databaseMetaData.getTables(Data.dbName, null, null, null);

                Statement stsm = connection.createStatement();
                int j = 0;
                for (j = 0; j< tableNames.size() ;j++) {
                    final ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(Data.dbName, null, tableNames.get(j));
                    final ResultSet columns = databaseMetaData.getColumns(Data.dbName, null, tableNames.get(j), null);
                    while (primaryKeys.next()) {
                        final String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (pkName.contains("Date")) {
                            stsm.executeUpdate("ALTER TABLE " + tableNames.get(j) + " DROP PRIMARY KEY");
                            keys = true;
                        }
                        if(!pkName.contains("id"))
                        {
                            stsm.executeUpdate("ALTER TABLE " + tableNames.get(j) + " ADD id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT FIRST");

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
    
  
                    


                  
                
               
                }

List <String> a = ExternalUpdater.getLoggerTables(tables);
for(int o = 0; o<a.size();o++)
{
    String y = a.get(o);
    String x = a.get(o).toLowerCase();
    System.out.println("x = " + x + " y = " + y);
    System.out.println("status " + (x.equals(y) == false));

    if(x.equals(y) == false)
    {
        stsm.executeUpdate("RENAME TABLE "+ a.get(o)+" TO " +a.get(o).toLowerCase());

    }

}

               

               
                
               
            
                stsm.close();
    
            } catch (Exception e) {

                main.getLogger().severe("Unable to update the tables. If the issue persists contact the Author!");
                e.printStackTrace();

            }
       


        if (keys && playerName) main.getLogger().info("All Tables have been Updated!");
        }
}
}
