package me.prism3.logger.utils.updater;

import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class SpigotUpdater extends Updater {

	public SpigotUpdater(final Plugin plugin, final int id, final UpdateType type) {
		super(plugin, id, type);
	}

	@Override
	protected boolean read() {
		try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.getId()).openStream();

			 final Scanner scanner = new Scanner(inputStream)) {

			if (scanner.hasNext()) { this.versionName = scanner.next(); }
		} catch (final IOException e) {
			Log.warning("Spigot might be down or have it's protection up! This error can be safely ignored");
			this.setResult(UpdateResult.FAIL_DBO);
			return false;
		}
		return true;
	}

	@Override
	public final boolean downloadFile() { return false; }

	@Override
	public String getUpdateLink() { return "https://www.spigotmc.org/resources/logger-1-7-1-19.94236/"; }
}