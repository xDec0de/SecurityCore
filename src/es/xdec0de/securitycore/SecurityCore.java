package es.xdec0de.securitycore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.securitycore.api.MCVersion;
import es.xdec0de.securitycore.api.SecurityCoreAPI;
import es.xdec0de.securitycore.features.AntiTab;
import es.xdec0de.securitycore.features.CommandHandler;
import es.xdec0de.securitycore.utils.files.MessageUtils;
import es.xdec0de.securitycore.utils.files.SCConfig;
import es.xdec0de.securitycore.utils.files.SCMessages;

public class SecurityCore extends JavaPlugin implements Listener {

	public static FileConfiguration config;

	public void onEnable() {
		executeEnable();
		MCVersion ver = SecurityCoreAPI.getInstance().getServerVersion();
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
		MessageUtils.logCol("       &e&lSecurityCore &8- &aEnabled");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Author&8: &bxDec0de_");
		MessageUtils.log(" ");
		if(ver.isSupported())
			MessageUtils.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &b"+ver.getName());
		else
			MessageUtils.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &4"+ver.getName()+" &8- &cUnsupported version&8.");
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
	}

	public void onDisable() {
		MCVersion ver = SecurityCoreAPI.getInstance().getServerVersion();
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
		MessageUtils.logCol("       &e&lSecurityCore &8- &cDisabled");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Author&8: &bxDec0de_");
		MessageUtils.log(" ");
		if(ver.isSupported())
			MessageUtils.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &b"+ver.getName());
		else
			MessageUtils.logCol("  &b- &7Version&8: &b"+getDescription().getVersion()+" &8| &7MC &4"+ver.getName()+" &8- &cUnsupported version&8.");
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
	}

	private void executeEnable() {
		SCConfig.setup(false);
		SCMessages.setup(false);
		Bukkit.getPluginManager().registerEvents(new CommandHandler(), this);
		Bukkit.getPluginManager().registerEvents(new AntiTab(), this);
		getCommand("securitycore").setExecutor(new SCCommand());
	}
}
