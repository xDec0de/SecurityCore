package es.xdec0de.securitycore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.securitycore.utils.files.Messages;
import es.xdec0de.securitycore.utils.files.SCConfig;
import es.xdec0de.securitycore.utils.files.SCSetting;

public class SCCommand implements TabExecutor {

	private final SecurityCore plugin;

	SCCommand(SecurityCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		Messages msg = plugin.getMessages();
		if(SCSetting.RELOAD_PERMISSION.asPermission(sender, true)) {
			if(args.length == 1) {
				switch(args[0].toLowerCase()) {
				case "reload": case "rl":
					SCConfig.setup(true);
					msg.reload(true);
					msg.send("Reload.Success", sender);
					break;
				default:
					msg.send("Reload.Usage", sender);
					break;
				}
			} else
				msg.send("Reload.Usage", sender);
		} else
			msg.send("SecurityCore.NoPerm", sender);
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
