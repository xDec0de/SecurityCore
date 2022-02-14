package es.xdec0de.securitycore.utils.files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.securitycore.SecurityCore;

public class SCConfig {

	static FileConfiguration cfg;
	private static File file;

	public static void setup(boolean isByReload) {
		SecurityCore sc = SecurityCore.getPlugin(SecurityCore.class);
		if (!sc.getDataFolder().exists())
			sc.getDataFolder().mkdir(); 
		if (!(file = new File(sc.getDataFolder(), "config.yml")).exists())
			sc.saveResource("config.yml", false); 
		reload(true, isByReload);
	}

	private static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "config.yml", isByReload))
			reload(false, isByReload);
	}
}
