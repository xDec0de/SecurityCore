package es.xdec0de.securitycore.utils.files;

import java.util.List;

import org.bukkit.command.CommandSender;

import es.xdec0de.securitycore.SecurityCore;

public class Config extends SCFile {

	public Config(SecurityCore plugin) {
		super(plugin, "config.yml");
	}

	public String getString(String path) {
		return cfg.getString(path);
	}

	public List<String> getList(String path) {
		return cfg.getStringList(path);
	}

	public boolean getBoolean(String path) {
		return cfg.getBoolean(path);
	}

	public int getInt(String path) {
		return cfg.getInt(path);
	}

	/**
	 * Checks if <b>sender</b> has permission to the setting.
	 * 
	 * @param sender The sender to check.
	 * @param message Whether to send the NoPerm message to the sender if it doesn't have permission or not.
	 * 
	 * @return true if the sender has permission, false otherwise.
	 */
	public boolean hasPermission(String path, CommandSender sender, boolean message) {
		if (sender.hasPermission(cfg.getString(path)))
			return true;
		if (message)
			plugin.getMessages().send("SecurityCore.NoPerm", sender);
		return false;
	}
}
