package es.xdec0de.securitycore.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import es.xdec0de.securitycore.utils.Replacer;
import es.xdec0de.securitycore.utils.files.SCMessage;
import es.xdec0de.securitycore.utils.files.SCSetting;

public class CommandHandler implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String cmd = e.getMessage().split(" ")[0].toLowerCase();
		e.setCancelled(handleAntiHiddenSyntax(p, cmd));
		e.setCancelled(handleBlockedCMD(p, cmd));
		handleWarnCMD(p, cmd);
	}

	private boolean handleAntiHiddenSyntax(Player p, String cmd) {
		if(SCSetting.AHS_ENABLED.asBoolean() && !SCSetting.AHS_BYPASS_PERMISSION.asPermission(p, false)) {
			if(cmd.contains(":")) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				SCMessage.AHS_NOT_ALLOWED.send(p, rep);
				String notify = SCMessage.AHS_NOTIFY.asString(rep);
				if(SCSetting.AHS_NOTIFY_CONSOLE.asBoolean())
					Bukkit.getConsoleSender().sendMessage(notify);
				if(SCSetting.AHS_NOTIFY_PLAYERS.asBoolean())
					Bukkit.getOnlinePlayers().stream().filter(on -> SCSetting.AHS_NOTIFY_PERMISSION.asPermission(on, false)).forEach(on -> on.sendMessage(notify));
				return true;
			}
		}
		return false;
	}

	private boolean handleBlockedCMD(Player p, String cmd) {
		boolean console = SCSetting.BLOCKEDCMDS_NOTIFY_CONSOLE.asBoolean();
		boolean players = SCSetting.BLOCKEDCMDS_NOTIFY_PLAYERS.asBoolean();
		if((console || players) && !SCSetting.BLOCKEDCMDS_BYPASS_PERMISSION.asPermission(p, false)) {
			if(SCSetting.BLOCKEDCMDS_LIST.asList().contains(cmd)) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				String notify = SCMessage.BLOCKEDCMDS_NOTIFY.asString(rep);
				SCMessage.BLOCKEDCMDS_BLOCKED.send(p);
				if(console)
					Bukkit.getConsoleSender().sendMessage(notify);
				if(players)
					Bukkit.getOnlinePlayers().stream().filter(on -> SCSetting.BLOCKEDCMDS_NOTIFY_PERMISSION.asPermission(on, false)).forEach(on -> on.sendMessage(notify));
				return true;
			}
		}
		return false;
	}

	private void handleWarnCMD(Player p, String cmd) {
		boolean console = SCSetting.WARNCMDS_NOTIFY_CONSOLE.asBoolean();
		boolean players = SCSetting.WARNCMDS_NOTIFY_PLAYERS.asBoolean();
		if((console || players) && !SCSetting.WARNCMDS_BYPASS_PERMISSION.asPermission(p, false)) {
			if(SCSetting.WARNCMDS_LIST.asList().contains(cmd)) {
				Replacer rep = new Replacer("%command%", cmd, "%player%", p.getName());
				String notify = SCMessage.WARNCMDS_NOTIFY.asString(rep);
				if(console)
					Bukkit.getConsoleSender().sendMessage(notify);
				if(players)
					Bukkit.getOnlinePlayers().stream().filter(on -> SCSetting.WARNCMDS_NOTIFY_PERMISSION.asPermission(on, false)).forEach(on -> on.sendMessage(notify));
			}
		}
	}
}
