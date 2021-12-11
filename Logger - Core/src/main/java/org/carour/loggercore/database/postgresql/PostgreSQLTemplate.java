package org.carour.loggercore.database.postgresql;

import com.earth2me.essentials.Essentials;
import org.carour.loggercore.database.DatabaseTemplate;

public interface PostgreSQLTemplate<T> extends DatabaseTemplate<T> {
    public void setPostgreSQL(PostgreSQL postgreSQL);

    public PostgreSQL getPostgreSQL();

    public Essentials getAPI();

    public T getPlugin();

}
