package es.xdec0de.securitycore.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import es.xdec0de.securitycore.SecurityCore;
import es.xdec0de.securitycore.utils.Replacer;
import es.xdec0de.securitycore.utils.files.Config;

public class CommandHandler implements Listener {

	private final SecurityCore plugin;

	public CommandHandler(SecurityCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String cmd = e.getMessage().split(" ")[0].toLowerCase();
		if(handleAntiHiddenSyntax(p, cmd) || handleBlockedCMD(p, cmd))
			e.setCancelled(true);
		handleWarnCMD(p, cmd);
	}

	private boolean handleAntiHiddenSyntax(Player p, String cmd) {
		Config cfg = plugin.getCfg();
		if (cfg.getBoolean("AHS.Enabled") && !cfg.hasPermission("AHS.BypassPerm", p, false)) {
			if(cmd.contains(":")) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				plugin.getMessages().send("AHS.NotAllowed", p, rep);
				String notify = plugin.getMessages().get("AHS.Notify", rep);
				if (cfg.getBoolean("AHS.Notify.Console"))
					Bukkit.getConsoleSender().sendMessage(notify);
				if (cfg.getBoolean("AHS.Notify.Players"))
					Bukkit.getOnlinePlayers().stream().filter(on -> cfg.hasPermission("AHS.Notify.Permission", on, false)).forEach(on -> on.sendMessage(notify));
				return true;
			}
		}
		return false;
	}

	private boolean handleBlockedCMD(Player p, String cmd) {
		Config cfg = plugin.getCfg();
		if (cfg.getBoolean("BlockedCMDs.Enabled") && !cfg.hasPermission("BlockedCMDs.BypassPerm", p, false)) {
			if(cfg.getList("BlockedCMDs.List").contains(cmd)) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				plugin.getMessages().send("BlockedCMDs.Blocked", p, rep);
				String notify = plugin.getMessages().get("BlockedCMDs.Notify", rep);
				if(cfg.getBoolean("BlockedCMDs.Notify.Console"))
					Bukkit.getConsoleSender().sendMessage(notify);
				if(cfg.getBoolean("BlockedCMDs.Notify.Players"))
					Bukkit.getOnlinePlayers().stream().filter(on -> cfg.hasPermission("BlockedCMDs.Notify.Permission", p, false)).forEach(on -> on.sendMessage(notify));
				return true;
			}
		}
		return false;
	}

	private void handleWarnCMD(Player p, String cmd) {
		Config cfg = plugin.getCfg();
		boolean console = cfg.getBoolean("WarnCMDs.Notify.Console");
		boolean players = cfg.getBoolean("WarnCMDs.Notify.Players");
		if((console || players) && !cfg.hasPermission("WarnCMDs.BypassPerm", p, false)) {
			if(cfg.getList("WarnCMDs.List").contains(cmd)) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				String notify = plugin.getMessages().get("WarnCMDs.Notify", rep);
				if(console)
					Bukkit.getConsoleSender().sendMessage(notify);
				if(players)
					Bukkit.getOnlinePlayers().stream().filter(on -> cfg.hasPermission("WarnCMDs.Notify.Permission", p, false)).forEach(on -> on.sendMessage(notify));
			}
		}
	}
}
