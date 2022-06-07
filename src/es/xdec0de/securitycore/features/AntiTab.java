package es.xdec0de.securitycore.features;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import es.xdec0de.securitycore.SecurityCore;
import es.xdec0de.securitycore.api.MCVersion;
import es.xdec0de.securitycore.utils.files.Config;

@SuppressWarnings("deprecation")
public class AntiTab {

	private final SecurityCore plugin;

	public AntiTab(SecurityCore plugin, MCVersion ver) {
		this.plugin = plugin;
		Listener listener = ver.supports(MCVersion.V1_14) ? new Current() : new Old();
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	class Current implements Listener {
		@EventHandler
		public void onCommandSend(PlayerCommandSendEvent e) {
			Config cfg = plugin.getCfg();
			if(cfg.getBoolean("AntiTab.Enabled") && !cfg.hasPermission("AntiTab.BypassPerm", e.getPlayer(), false)) {
				List<String> checkContains = cfg.getList("AntiTab.Hide.ListContains");
				List<String> checkExact = cfg.getList("AntiTab.Hide.ListExact");
				List<String> commands = new LinkedList<String>();
				if(!cfg.getBoolean("AntiTab.Hide.All")) {
					commands.addAll(e.getCommands());
					for(String cmd : e.getCommands()) {
						for(String check : checkExact)
							if(cmd.equals(check))
								commands.remove(cmd);
						for(String check : checkContains)
							if(cmd.contains(check))
								commands.remove(cmd);
					}
				}
				e.getCommands().clear();
				e.getCommands().addAll(commands);
			}
		}
	}

	class Old implements Listener {
		@EventHandler
		public void onTabComplete(PlayerChatTabCompleteEvent e) {
			Config cfg = plugin.getCfg();
			if(cfg.getBoolean("AntiTab.Enabled") && !cfg.hasPermission("AntiTab.BypassPerm", e.getPlayer(), false)) {
				List<String> checkContains = cfg.getList("AntiTab.Hide.ListContains");
				List<String> checkExact = cfg.getList("AntiTab.Hide.ListExact");
				List<String> commands = new LinkedList<String>();
				if(!cfg.getBoolean("AntiTab.Hide.All")) {
					commands.addAll(e.getTabCompletions());
					for(String cmd : e.getTabCompletions()) {
						for(String check : checkExact)
							if(cmd.equals(check))
								commands.remove(cmd);
						for(String check : checkContains)
							if(cmd.contains(check))
								commands.remove(cmd);
					}
				}
				e.getTabCompletions().clear();
				e.getTabCompletions().addAll(commands);
			}
		}
	}
}
