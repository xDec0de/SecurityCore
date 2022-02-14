package es.xdec0de.securitycore.utils.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.securitycore.SecurityCore;

class FileUtils {

	private static File copyInputStreamToFile(String path, InputStream inputStream) {
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

	static boolean updateFile(File file, String path, boolean isByReload) {
		try {
			int changes = 0;
			FileConfiguration old = Utf8YamlConfiguration.loadConfiguration(file);
			Utf8YamlConfiguration updated = new Utf8YamlConfiguration();
			JavaPlugin blp = SecurityCore.getPlugin(SecurityCore.class);
			if(blp.getResource(path) != null)
				updated.load(copyInputStreamToFile(blp.getDataFolder()+ "/"+path, blp.getResource(path)));
			else {
				//MessageUtils.logColRep("%prefix% Could not update &6"+path);
				return false;
			}
			Set<String> oldKeys = old.getKeys(true);
			Set<String> updKeys = updated.getKeys(true);
			for(String str : oldKeys)
				if(!updKeys.contains(str)) {
					old.set(str, null);
					changes++;
				}
			for(String str : updKeys)
				if(!oldKeys.contains(str)) {
					old.set(str, updated.get(str));
					changes++;
				}
			old.save(blp.getDataFolder() + "/"+path);
			if(changes != 0) {
				/*if(isByReload)
					MessageUtils.logColRep("%prefix% &6"+path+" &7has been reloaded with &b"+changes+" &7changes.");
				else
					MessageUtils.logColRep("%prefix% &6"+path+" &7has been updated to &ev"+blp.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.");*/
				return true;
			}
			return false;
		} catch(InvalidConfigurationException | IOException ex) {
			//MessageUtils.logColRep("%prefix% Could not update &6"+path);
			return false;
		}
	}
}
