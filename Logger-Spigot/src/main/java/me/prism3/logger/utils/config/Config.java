package me.prism3.logger.utils.config;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Config extends YamlConfiguration {

	private final Main main;
	private final File file;
	private final FileConfiguration configFile;

	@SuppressWarnings("resource")
	public Config(final Main plugin, final String name) throws FileNotFoundException {
		this.main = plugin;
		this.file = new File(plugin.getDataFolder(), name);
		if (!this.file.exists()) {
			this.prepareFile(name);
			Log.info("New " + name + " file has been created successfully!");
		}
		this.configFile = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8));
	}

	@Override
	public final void set(final @NotNull String path, final Object value) {
		this.configFile.set(path, value);
	}

	@Override
	public final String getString(final @NotNull String path) {
		return this.configFile.getString(path);
	}

	@Override
	public final String getString(final @NotNull String path, final String def) {
		return this.configFile.getString(path, def);
	}

	@Override
	public final int getInt(final @NotNull String path) {
		return this.configFile.getInt(path);
	}

	@Override
	public final int getInt(final @NotNull String path, final int def) {
		return this.configFile.getInt(path, def);
	}

	@Override
	public final boolean getBoolean(final @NotNull String path) {
		return this.configFile.getBoolean(path);
	}

	@Override
	public final boolean getBoolean(final @NotNull String path, final boolean def) {
		return this.configFile.getBoolean(path, def);
	}

	@Override
	public final ConfigurationSection getConfigurationSection(final @NotNull String path) {
		if (!this.configFile.isConfigurationSection(path))
			return this.configFile.createSection(path);
		return this.configFile.getConfigurationSection(path);
	}

	@Override
	public final double getDouble(final @NotNull String path) {
		return this.configFile.getDouble(path);
	}

	@Override
	public final double getDouble(final @NotNull String path, final double def) {
		return this.configFile.getDouble(path, def);
	}

	@Override
	public final List<?> getList(final @NotNull String path) {
		return this.configFile.getList(path);
	}

	@Override
	public final List<?> getList(final @NotNull String path, final List<?> def) {
		return this.configFile.getList(path, def);
	}

	@SuppressWarnings("resource")
	private void prepareFile(final String resource) {
		try {
			this.file.getParentFile().mkdirs();
			if (this.file.createNewFile() && resource != null && !resource.isEmpty()) {
				this.copyResource(this.main.getResource(resource), this.file);
			}
		} catch (final IOException e) { e.printStackTrace(); }
	}

	private void copyResource(final InputStream resource, final File file1) {
		try (final OutputStream out = new FileOutputStream(file1)) {
			int lenght;
			final byte[] buf = new byte[1024];

			while ((lenght = resource.read(buf)) > 0) { out.write(buf, 0, lenght); }
			resource.close();
		} catch (final Exception e) { e.printStackTrace(); }
	}
}
