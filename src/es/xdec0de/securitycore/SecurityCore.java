package es.xdec0de.securitycore;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolManager;

import es.xdec0de.securitycore.features.AntiTab;

public class SecurityCore extends JavaPlugin implements Listener, TabExecutor {

	public static SecurityCore plugin;
	public static FileConfiguration config;
	File cfile;
	ProtocolManager protocolManager;

	public void onEnable() {
		plugin = this;
		config = this.getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		this.cfile = new File(this.getDataFolder(), "config.yml");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("enable"))));
		this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
		this.saveDefaultConfig();
		if (config.getBoolean("AntiTab-enabled"))
			AntiTab.setup();
		else
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + "&eWARNING: &4AntiTab &cis disabled, your plugins, spigot version etc... are visible")));
	}

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("disable"))));
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

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ahs")) {
            if (sender.hasPermission(config.getString("ahs-command-permission"))) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("NotEnoughArgs"))));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(config.getString("ahs-reload-permission"))) {
                        config = YamlConfiguration.loadConfiguration((File)this.cfile);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("reload-msg"))));
                    }
                    if (!sender.hasPermission(config.getString("ahs-reload-permission"))) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("NoPerm"))));
                    }
                } else if (args[0].equalsIgnoreCase("rl")) {
                    if (sender.hasPermission(config.getString("ahs-reload-permission"))) {
                        config = YamlConfiguration.loadConfiguration((File)this.cfile);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("reload-msg"))));
                    }
                    if (!sender.hasPermission(config.getString("ahs-reload-permission"))) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("NoPerm"))));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("usage"))));
                }
            }
            if (!sender.hasPermission(config.getString("ahs-command-permission"))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(config.getString("prefix")) + " " + config.getString("NoPerm"))));
            }
        }
        return true;
    }
}

