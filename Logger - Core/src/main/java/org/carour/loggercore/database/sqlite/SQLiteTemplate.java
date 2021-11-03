package org.carour.loggercore.database.sqlite;

import com.earth2me.essentials.Essentials;

public interface SQLiteTemplate<T> {
    public void setSQLite(SQLite sqLite);

    public SQLite getSQLite();

    public Essentials getAPI();

    public T getPlugin();

}
