package org.carour.loggercore.database.sqlite;

import com.earth2me.essentials.Essentials;
import org.carour.loggercore.database.DatabaseTemplate;

public interface SQLiteTemplate<T> extends DatabaseTemplate<T> {
    public void setSQLite(SQLite sqLite);

    public SQLite getSQLite();

    public Essentials getAPI();

    public T getPlugin();

}
