package es.xdec0de.securitycore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.securitycore.utils.files.SCConfig;
import es.xdec0de.securitycore.utils.files.SCMessage;
import es.xdec0de.securitycore.utils.files.SCMessages;
import es.xdec0de.securitycore.utils.files.SCSetting;

public class SCCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(SCSetting.RELOAD_PERMISSION.asPermission(sender, true)) {
			if(args.length == 1) {
				switch(args[0].toLowerCase()) {
				case "reload": case "rl":
					SCConfig.setup(true);
					SCMessages.setup(true);
					SCMessage.RELOAD_SUCCESS.send(sender);
					break;
				default:
					SCMessage.RELOAD_USAGE.send(sender);
					break;
				}
			} else
				SCMessage.RELOAD_USAGE.send(sender);
		} else
			SCMessage.NO_PERM.send(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			if(SCSetting.RELOAD_PERMISSION.asPermission(sender, false))
				tab.add("reload");
		}
		return tab;
	}
}
