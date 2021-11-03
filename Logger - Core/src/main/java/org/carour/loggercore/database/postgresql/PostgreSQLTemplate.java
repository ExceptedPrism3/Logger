package org.carour.loggercore.database.postgresql;

import com.earth2me.essentials.Essentials;

public interface PostgreSQLTemplate<T> {
    public void setPostgreSQL(PostgreSQL postgreSQL);

    public PostgreSQL getPostgreSQL();

    public Essentials getAPI();

    public T getPlugin();

}
