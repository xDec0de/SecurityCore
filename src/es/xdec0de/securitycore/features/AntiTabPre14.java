package es.xdec0de.securitycore.features;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import es.xdec0de.securitycore.utils.files.MessageUtils;
import es.xdec0de.securitycore.utils.files.SCSetting;

@SuppressWarnings("deprecation")
public class AntiTabPre14 implements Listener {

	@EventHandler
	public void onTabComplete(PlayerChatTabCompleteEvent e) {
		MessageUtils.logCol("&7Fired pre 1.14 event.");
		if(SCSetting.ANTITAB_ENABLED.asBoolean() && !SCSetting.ANTITAB_BYPASS_PERMISSION.asPermission(e.getPlayer(), false)) {
			List<String> checkContains = SCSetting.ANTITAB_HIDE_LIST_CONTAINS.asList();
			List<String> checkExact = SCSetting.ANTITAB_HIDE_LIST_EXACT.asList();
			List<String> commands = new LinkedList<String>();
			if(!SCSetting.ANTITAB_HIDE_ALL.asBoolean()) {
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
