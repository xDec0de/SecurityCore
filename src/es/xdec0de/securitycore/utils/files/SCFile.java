package es.xdec0de.securitycore.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import es.xdec0de.securitycore.SecurityCore;
import net.md_5.bungee.api.ChatColor;

public class SCFile {

	final SecurityCore plugin;
	final String path;

	final File file;
	FileConfiguration cfg;

	SCFile(SecurityCore plugin, String path) {
		this.plugin = plugin;
		this.path = path;
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if(!(file = new File(plugin.getDataFolder(), path)).exists())
			plugin.saveResource(path, false);
		reload(false);
		update(false);
	}

	public void reload(boolean update) {
		if (update)
			update(true);
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	private File copyInputStreamToFile(String path, InputStream inputStream) {
		File file = new File(path);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1)
				outputStream.write(bytes, 0, read);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean update(boolean reload) {
		final String prefix = "&7[&b&lSecurity&9&lCore&7]";
		try {
			int changes = 0;
			Utf8YamlConfiguration updated = new Utf8YamlConfiguration();
			if (plugin.getResource(path) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+path, plugin.getResource(path)));
			else {
				log(prefix+" Could not update &6"+path);
				return false;
			}
			Set<String> oldKeys = cfg.getKeys(true);
			Set<String> updKeys = updated.getKeys(true);
			for (String str : oldKeys)
				if(!updKeys.contains(str)) {
					cfg.set(str, null);
					changes++;
				}
			for (String str : updKeys)
				if (!oldKeys.contains(str)) {
					cfg.set(str, updated.get(str));
					changes++;
				}
			cfg.save(plugin.getDataFolder() + "/"+path);
			if (changes != 0) {
				log(" ");
				if (reload)
					log(prefix+" &6"+path+" &7has been reloaded with &b"+changes+" &7changes.");
				else
					log(prefix+" &6"+path+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.");
				return true;
			}
			return false;
		} catch(InvalidConfigurationException | IOException ex) {
			log(prefix+" Could not update &6"+path);
			return false;
		}
	}

	// Used to not call Messages just in case it breaks, reporting errors of messages.yml using it's class is pretty dumb :)
	private void log(String str) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
	}

	public FileConfiguration getFile() {
		return cfg;
	}

	class Utf8YamlConfiguration extends YamlConfiguration {

		@Override
		public void save(File file) throws IOException {
			Validate.notNull(file, "File cannot be null");
			Files.createParentDirs(file);
			String data = this.saveToString();
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
			try {
				writer.write(data);
			} finally {
				writer.close();
			}
		}

		@Override
		public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
			Validate.notNull(file, "File cannot be null");
			this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
		}
	}
}