package es.xdec0de.securitycore.features;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import es.xdec0de.securitycore.utils.files.SCSetting;

public class AntiTabLatest implements Listener {

	@EventHandler
	public void onCommandSend(PlayerCommandSendEvent e) {
		if(SCSetting.ANTITAB_ENABLED.asBoolean() && !SCSetting.ANTITAB_BYPASS_PERMISSION.asPermission(e.getPlayer(), false)) {
			List<String> checkContains = SCSetting.ANTITAB_HIDE_LIST_CONTAINS.asList();
			List<String> checkExact = SCSetting.ANTITAB_HIDE_LIST_EXACT.asList();
			List<String> commands = new LinkedList<String>();
			if(!SCSetting.ANTITAB_HIDE_ALL.asBoolean()) {
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
