package es.xdec0de.securitycore;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolManager;

import es.xdec0de.securitycore.features.AntiTab;
import es.xdec0de.securitycore.utils.files.MessageUtils;
import es.xdec0de.securitycore.utils.files.SCConfig;
import es.xdec0de.securitycore.utils.files.SCMessages;
import es.xdec0de.securitycore.utils.files.SCSetting;

public class SecurityCore extends JavaPlugin implements Listener {

	public static SecurityCore plugin;
	public static FileConfiguration config;
	File cfile;
	ProtocolManager protocolManager;

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
		if(SCSetting.ANTITAB_ENABLED.asBoolean())
			AntiTab.setup();
		getCommand("securitycore").setExecutor(new SCCommand());
	}

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String notify;
        String notify2;
        if (config.getList("ignore-blocked-cmds").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!config.getList("ignore-blocked-cmds").contains(e.getPlayer().getName()) && config.getList("blocked-cmds").contains(e.getMessage())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("blocked-cmd"))));
            if (config.getBoolean("notify-console")) {
                notify2 = config.getString("notify");
                notify2 = notify2.replaceAll("%command%", e.getMessage());
                notify2 = notify2.replaceAll("%player%", e.getPlayer().getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + notify2)));
            }
            if (config.getBoolean("notify-enabled")) {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayers.hasPermission(config.getString("notify-block-permission"))) continue;
                    notify = config.getString("notify");
                    notify = notify.replaceAll("%command%", e.getMessage());
                    notify = notify.replaceAll("%player%", e.getPlayer().getName());
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + notify)));
                }
            }
        }
        if (config.getList("ignore-warn-cmds").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!config.getList("ignore-warn-cmds").contains(e.getPlayer().getName())) {
            if (config.getList("warn-cmds").contains(e.getMessage())) {
                if (config.getBoolean("warn-console")) {
                    String warn = config.getString("warn-message");
                    warn = warn.replaceAll("%command%", e.getMessage());
                    warn = warn.replaceAll("%player%", e.getPlayer().getName());
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + warn)));
                }
                if (config.getBoolean("warn-enabled")) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        if (!onlinePlayers.hasPermission(config.getString("notify-warn-permission"))) continue;
                        String warn = config.getString("warn-message");
                        warn = warn.replaceAll("%command%", e.getMessage());
                        warn = warn.replaceAll("%player%", e.getPlayer().getName());
                        onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + warn)));
                    }
                }
            }
            if (!config.getBoolean("warn-enabled")) {
                e.setCancelled(false);
            }
        }
        if (config.getList("ignore-hidden-syntax").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!config.getList("ignore-hidden-syntax").contains(e.getPlayer().getName()) && e.getMessage().split(" ")[0].contains(":")) {
            if (config.getBoolean("notify-console")) {
                notify2 = config.getString("notify");
                notify2 = notify2.replaceAll("%command%", e.getMessage());
                notify2 = notify2.replaceAll("%player%", e.getPlayer().getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + notify2)));
            }
            if (config.getBoolean("notify-enabled")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("syntaxnotallowed"))));
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayers.hasPermission(config.getString("notify-syntax-permission"))) continue;
                    notify = config.getString("notify");
                    notify = notify.replaceAll("%command%", e.getMessage());
                    notify = notify.replaceAll("%player%", e.getPlayer().getName());
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + notify)));
                }
            }
            if (!config.getBoolean("notify-enabled")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("syntaxnotallowed"))));
            }
        }
    }
}

