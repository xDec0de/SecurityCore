package es.xdec0de.securitycore.features;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import es.xdec0de.securitycore.SecurityCore;
import es.xdec0de.securitycore.utils.files.Config;

@SuppressWarnings("deprecation")
public class AntiTabPre14 implements Listener {

	private final SecurityCore plugin;

	public AntiTabPre14(SecurityCore plugin) {
		this.plugin = plugin;
	}

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
