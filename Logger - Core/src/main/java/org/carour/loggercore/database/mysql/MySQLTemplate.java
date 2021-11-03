package org.carour.loggercore.database.mysql;

import com.earth2me.essentials.Essentials;
import org.bukkit.plugin.java.JavaPlugin;

public interface MySQLTemplate<T> {


    public void setMySQL(MySQL mySQL);

    public MySQL getMySQL();

    public Essentials getAPI();

    public T getPlugin();


}
