package org.carour.loggercore.database;

import com.earth2me.essentials.Essentials;

public interface DatabaseTemplate<T> {
    public Essentials getAPI();

    public T getPlugin();
}
