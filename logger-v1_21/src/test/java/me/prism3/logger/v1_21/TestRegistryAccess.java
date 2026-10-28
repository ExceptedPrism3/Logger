package me.prism3.logger.v1_21;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.Registry;

import static org.mockito.Mockito.mock;

public class TestRegistryAccess implements RegistryAccess {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> Registry<T> getRegistry(Class<T> type) {
        return mock(Registry.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> Registry<T> getRegistry(RegistryKey<T> registryKey) {
        return mock(Registry.class);
    }
}
