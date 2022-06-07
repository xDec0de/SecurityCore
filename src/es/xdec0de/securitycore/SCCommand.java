package es.xdec0de.securitycore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.securitycore.utils.files.Config;
import es.xdec0de.securitycore.utils.files.Messages;

public class SCCommand implements TabExecutor {

	private final SecurityCore plugin;

	SCCommand(SecurityCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		Messages msg = plugin.getMessages();
		Config cfg = plugin.getCfg();
		if (!cfg.hasPermission("ReloadPerm", sender, true))
			return true;
		if(args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "reload": case "rl":
				cfg.reload(true);
				msg.reload(true);
				msg.send("Reload.Success", sender);
				break;
			default:
				msg.send("Reload.Usage", sender);
				break;
			}
		} else
			msg.send("Reload.Usage", sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			if(plugin.getCfg().hasPermission("ReloadPerm", sender, false))
				tab.add("reload");
		}
		return tab;
	}
}
