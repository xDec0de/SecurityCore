package es.xdec0de.securitycore.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.securitycore.SecurityCore;

public class SettingChanger implements TabExecutor {

	private final SecurityCore plugin;
	private List<String> ints = new ArrayList<>();

	// /scsetting setint <path> <value>

	public SettingChanger(SecurityCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		initialize();
		if (!plugin.getCfg().hasPermission("SCSetting.Permission", sender, true))
			return true;
		if (args.length == 3) {
			switch (args[0].toLowerCase()) {
			case "setint": handleInt(sender, args); break;
			default: plugin.getMessages().send("SCSetting.Usage", sender); break;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		initialize();
		if (!plugin.getCfg().hasPermission("SCSetting.Permission", sender, false))
			return new ArrayList<>();
		if (args.length == 1)
			return Arrays.asList("setint");
		if (args.length == 2) {
			String mode = args[0].toLowerCase();
			if (mode.equals("setint"))
				return ints;
			return new ArrayList<>();
		}
		if (args.length == 3)
			return Arrays.asList("value");
		return null;
	}

	private void handleInt(CommandSender sender, String[] args) {
		String path = args[1];
		int value = StringUtils.isNumeric(args[2]) ? Integer.valueOf(args[2]) : 0;
		if (!ints.contains(path))
			plugin.getMessages().send("SCSetting.InvalidPath", sender);
		else if (value <= 0)
			plugin.getMessages().send("SCSetting.IntInvalid", sender);
		else {
			plugin.getCfg().getFile().set(path, value);
			plugin.getCfg().save();
			plugin.getCfg().reload(false);
			plugin.getMessages().send("SCSetting.Changed", sender, "%path%", path, "%value%", args[2]);
		}
	}

	private void initialize() {
		if (ints.isEmpty())
			ints = Arrays.asList("RedstoneLimiter.MaxPulsesPerSecond");
	}
}
