package org.carour.loggercore.database.mysql;

import com.earth2me.essentials.Essentials;
import org.bukkit.plugin.java.JavaPlugin;
import org.carour.loggercore.database.DatabaseTemplate;

public interface MySQLTemplate<T> extends DatabaseTemplate<T> {


    public void setMySQL(MySQL mySQL);

    public MySQL getMySQL();

    public Essentials getAPI();

    public T getPlugin();


}
