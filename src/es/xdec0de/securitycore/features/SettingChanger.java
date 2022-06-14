package es.xdec0de.securitycore.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import es.xdec0de.securitycore.SecurityCore;
import es.xdec0de.securitycore.api.SCSetting;
import es.xdec0de.securitycore.api.SCSetting.SettingType;

public class SettingChanger implements TabExecutor {

	private final SecurityCore plugin;

	public SettingChanger(SecurityCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.getCfg().hasPermission("SCSetting.Permission", sender, true))
			return true;
		if (args.length == 3) {
			String mode = args[0].toLowerCase();
			SettingType type = getTypeFromCmd(mode);
			if (type != null && mode.contains("set"))
				return handleSet(sender, args, type); // Always returns true.
		}
		plugin.getMessages().send("SCSetting.Usage", sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.getCfg().hasPermission("SCSetting.Permission", sender, false))
			return new ArrayList<>();
		if (args.length == 1)
			return Arrays.asList("setint", "setbool", "setstr");
		if (args.length == 2) {
			String mode = args[0].toLowerCase();
			if (mode.equals("setint"))
				return SCSetting.namesOfType(SettingType.INT);
			else if (mode.equals("setbool") || mode.equals("setboolean"))
				return SCSetting.namesOfType(SettingType.BOOLEAN);
			else if (mode.equals("setstr") || mode.equals("setstring"))
				return SCSetting.namesOfType(SettingType.STRING);
			return new ArrayList<>();
		}
		if (args.length == 3) {
			String mode = args[0].toLowerCase();
			boolean isBool = mode.equals("setbool") || mode.equals("setboolean");
			return isBool ? Arrays.asList("true", "false") : Arrays.asList("value");
		}
		return null;
	}

	private boolean handleSet(CommandSender sender, String[] args, SettingType type) {
		SCSetting setting = SCSetting.of(args[1]);
		String value = args[2];
		if (setting == null)
			plugin.getMessages().send("SCSetting.InvalidSetting", sender);
		else if (!setting.allows(value))
			plugin.getMessages().send("SCSetting.InvalidValue", sender);
		else {
			if (type.equals(SettingType.INT))
				plugin.getCfg().getFile().set(setting.getPath(), Integer.valueOf(value));
			else if (type.equals(SettingType.BOOLEAN))
				plugin.getCfg().getFile().set(setting.getPath(), Boolean.valueOf(value));
			else
				plugin.getCfg().getFile().set(setting.getPath(), value);
			plugin.getCfg().save();
			plugin.getCfg().reload(false);
			plugin.getMessages().send("SCSetting.Changed", sender, "%setting%", setting.name(), "%value%", value);
		}
		return true;
	}

	private SettingType getTypeFromCmd(String mode) { // mode is already lower case
		switch (mode) {
		case "setint": return SettingType.INT;
		case "setboolean": case "setbool": return SettingType.BOOLEAN;
		case "setstr": case "setstring": return SettingType.STRING;
		default: return null;
		}
	}
}
