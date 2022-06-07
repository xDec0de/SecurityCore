package es.xdec0de.securitycore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.securitycore.api.MCVersion;
import es.xdec0de.securitycore.api.SecurityCoreAPI;
import es.xdec0de.securitycore.features.AntiTabLatest;
import es.xdec0de.securitycore.features.AntiTabPre14;
import es.xdec0de.securitycore.features.CommandHandler;
import es.xdec0de.securitycore.features.RedstoneLimiter;
import es.xdec0de.securitycore.utils.files.Config;
import es.xdec0de.securitycore.utils.files.Messages;

public class SecurityCore extends JavaPlugin implements Listener {

	public static FileConfiguration config;

	private final Config cfg = new Config(this);
	private final Messages msg = new Messages(this);

	public void onEnable() {
		executeEnable();
		MCVersion ver = SecurityCoreAPI.getInstance().getServerVersion();
		msg.log(" ");
		msg.logCol("&8|------------------------------------------>");
		msg.log(" ");
		msg.logCol("       &e&lSecurityCore &8- &aEnabled");
		msg.log(" ");
		msg.logCol("  &b- &7Author&8: &bxDec0de_");
		msg.log(" ");
		if(ver.isSupported())
			msg.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &b"+ver.getName());
		else
			msg.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &4"+ver.getName()+" &8- &cUnsupported version&8.");
		msg.log(" ");
		msg.logCol("&8|------------------------------------------>");
		msg.log(" ");
	}

	public void onDisable() {
		MCVersion ver = SecurityCoreAPI.getInstance().getServerVersion();
		msg.log(" ");
		msg.logCol("&8|------------------------------------------>");
		msg.log(" ");
		msg.logCol("       &e&lSecurityCore &8- &cDisabled");
		msg.log(" ");
		msg.logCol("  &b- &7Author&8: &bxDec0de_");
		msg.log(" ");
		if(ver.isSupported())
			msg.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &b"+ver.getName());
		else
			msg.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &4"+ver.getName()+" &8- &cUnsupported version&8.");
		msg.log(" ");
		msg.logCol("&8|------------------------------------------>");
		msg.log(" ");
	}

	private void executeEnable() {
		Bukkit.getPluginManager().registerEvents(new CommandHandler(this), this);
		if(SecurityCoreAPI.getInstance().getServerVersion().supports(MCVersion.V1_14))
			Bukkit.getPluginManager().registerEvents(new AntiTabLatest(this), this);
		else
			Bukkit.getPluginManager().registerEvents(new AntiTabPre14(this), this);
		getCommand("securitycore").setExecutor(new SCCommand(this));
		getServer().getPluginManager().registerEvents(new RedstoneLimiter(this), this);
	}

	public Config getCfg() {
		return cfg;
	}

	@Override
	public FileConfiguration getConfig() {
		return cfg.getFile();
	}

	public Messages getMessages() {
		return msg;
	}
}
