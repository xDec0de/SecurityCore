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
			if (type != null)
				return handleChange(sender, args, type); // Always returns true.
		}
		plugin.getMessages().send("SCSetting.Usage", sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.getCfg().hasPermission("SCSetting.Permission", sender, false))
			return new ArrayList<>();
		if (args.length == 1)
			return Arrays.asList("setint", "setbool", "setstr", "listadd", "listrem");
		if (args.length == 2) {
			String mode = args[0].toLowerCase();
			SettingType type = getTypeFromCmd(mode);
			return type != null ? SCSetting.namesOfType(type) : new ArrayList<>();
		}
		if (args.length == 3) {
			String mode = args[0].toLowerCase();
			boolean isBool = mode.equals("setbool") || mode.equals("setboolean");
			return isBool ? Arrays.asList("true", "false") : Arrays.asList("value");
		}
		return null;
	}

	private boolean handleChange(CommandSender sender, String[] args, SettingType type) {
		SCSetting setting = SCSetting.of(args[1]);
		String value = args[2];
		if (setting == null)
			plugin.getMessages().send("SCSetting.InvalidSetting", sender);
		else if (!setting.allows(value))
			plugin.getMessages().send("SCSetting.InvalidValue", sender);
		else {
			if (type.equals(SettingType.STRING_LIST)) {
				String mode = args[0].toLowerCase();
				List<String> lst = plugin.getCfg().getList(setting.getPath());
				if (mode.contains("add")) {
					boolean contains = lst.contains(value);
					String path = !contains ? "SCSetting.Added" : "SCSetting.NotAdded";
					if (!contains)
						lst.add(value);
					plugin.getMessages().send(path, sender, "%setting%", setting.name(), "%value%", value);
				} else {
					String path = lst.remove(value) ? "SCSetting.Removed" : "SCSetting.NotRemoved";
					plugin.getMessages().send(path, sender, "%setting%", setting.name(), "%value%", value);
				}
				plugin.getCfg().getFile().set(setting.getPath(), lst);
			} else if (type.equals(SettingType.INT))
				plugin.getCfg().getFile().set(setting.getPath(), Integer.valueOf(value));
			else if (type.equals(SettingType.BOOLEAN))
				plugin.getCfg().getFile().set(setting.getPath(), Boolean.valueOf(value));
			else
				plugin.getCfg().getFile().set(setting.getPath(), value);
			plugin.getCfg().save();
			plugin.getCfg().reload(false);
			if (!type.equals(SettingType.STRING_LIST))
				plugin.getMessages().send("SCSetting.Changed", sender, "%setting%", setting.name(), "%value%", value);
		}
		return true;
	}

	private SettingType getTypeFromCmd(String mode) { // mode is already lower case
		switch (mode) {
		case "setint": return SettingType.INT;
		case "setboolean": case "setbool": return SettingType.BOOLEAN;
		case "setstr": case "setstring": return SettingType.STRING;
		case "listadd": case "listrem": case "listremove": return SettingType.STRING_LIST;
		default: return null;
		}
	}
}
