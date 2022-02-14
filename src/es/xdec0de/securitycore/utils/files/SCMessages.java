package es.xdec0de.securitycore.utils.files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import es.xdec0de.securitycore.SecurityCore;

public class SCMessages {

	static FileConfiguration cfg;
	private static File file;

	public static void setup(boolean isByReload) {
		SecurityCore sc = SecurityCore.getPlugin(SecurityCore.class);
		if (!sc.getDataFolder().exists())
			sc.getDataFolder().mkdir(); 
		if (!(file = new File(sc.getDataFolder(), "messages.yml")).exists())
			sc.saveResource("messages.yml", false); 
		reload(true, isByReload);
	}

	private static void reload(boolean update, boolean isByReload) {
		cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(update && FileUtils.updateFile(file, "messages.yml", isByReload))
			reload(false, isByReload);
		MessageUtils.prefix = cfg.getString(SCMessage.PREFIX.getPath());
		MessageUtils.errorPrefix = cfg.getString(SCMessage.ERROR.getPath());
	}
}
