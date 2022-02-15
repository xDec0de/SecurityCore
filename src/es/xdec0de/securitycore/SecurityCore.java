package es.xdec0de.securitycore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.securitycore.features.AntiTab;
import es.xdec0de.securitycore.features.CommandHandler;
import es.xdec0de.securitycore.utils.files.MessageUtils;
import es.xdec0de.securitycore.utils.files.SCConfig;
import es.xdec0de.securitycore.utils.files.SCMessages;

public class SecurityCore extends JavaPlugin implements Listener {

	public static FileConfiguration config;

	public void onEnable() {
		executeEnable();
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
		MessageUtils.logCol("       &e&lSecurityCore &8- &aEnabled");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Author&8: &bxDec0de_");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Version: &b"+getDescription().getVersion());
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
	}

	public void onDisable() {
		MessageUtils.log(" ");
		MessageUtils.logCol("&8|------------------------------------------>");
		MessageUtils.log(" ");
		MessageUtils.logCol("       &e&lSecurityCore &8- &cDisabled");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Author&8: &bxDec0de_");
		MessageUtils.log(" ");
		MessageUtils.logCol("  &b- &7Version: &b"+getDescription().getVersion());
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
