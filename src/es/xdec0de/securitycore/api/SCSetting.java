package es.xdec0de.securitycore.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public enum SCSetting {

	AHS_ENABLED("AHS.Enabled", SettingType.BOOLEAN),
	AHS_BYPASS_PERM("AHS.BypassPerm", SettingType.STRING),
	AHS_NOTIFY_PERM("AHS.Notify.Permission", SettingType.STRING),
	AHS_NOTIFY_CONSOLE("AHS.Notify.Console", SettingType.BOOLEAN),
	AHS_NOTIFY_PLAYERS("AHS.Notify.Players", SettingType.BOOLEAN),

	ANTITAB_ENABLED("AntiTab.Enabled", SettingType.BOOLEAN),
	ANTITAB_BYPASS_PERM("AntiTab.BypassPerm", SettingType.STRING),
	ANTITAB_HIDE_ALL("AntiTab.Hide.All", SettingType.BOOLEAN),
	ANTITAB_HIDE_CONTAINS("AntiTab.Hide.ListContains", SettingType.STRING_LIST),
	ANTITAB_HIDE_EXACT("AntiTab.Hide.ListExact", SettingType.STRING_LIST),

	WARNCMDS_ENABLED("WarnCMDs.Enabled", SettingType.BOOLEAN),
	WARNCMDS_BYPASS_PERM("WarnCMDs.BypassPerm", SettingType.STRING),
	WARNCMDS_NOTIFY_PERM("WarnCMDs.Notify.Permission", SettingType.STRING),
	WARNCMDS_NOTIFY_CONSOLE("WarnCMDs.Notify.Console", SettingType.BOOLEAN),
	WARNCMDS_NOTIFY_PLAYERS("WarnCMDs.Notify.Players", SettingType.BOOLEAN),
	WARNCMDS_LIST("WarnCMDs.List", SettingType.STRING_LIST),

	BLOCKEDCMDS_ENABLED("BlockedCMDs.Enabled", SettingType.BOOLEAN),
	BLOCKEDCMDS_BYPASS_PERM("BlockedCMDs.BypassPerm", SettingType.STRING),
	BLOCKEDCMDS_NOTIFY_PERM("BlockedCMDs.Notify.Permission", SettingType.STRING),
	BLOCKEDCMDS_NOTIFY_CONSOLE("BlockedCMDs.Notify.Console", SettingType.BOOLEAN),
	BLOCKEDCMDS_NOTIFY_PLAYERS("BlockedCMDs.Notify.Players", SettingType.BOOLEAN),
	BLOCKEDCMDS_LIST("BlockedCMDs.List", SettingType.STRING_LIST),

	RELOAD_PERM("ReloadPerm", SettingType.STRING),

	REDSTONELIMITER_ENABLED("RedstoneLimiter.Enabled", SettingType.BOOLEAN),
	REDSTONELIMITER_MAX_PPS("RedstoneLimiter.MaxPulsesPerSecond", SettingType.INT) {
		@Override
		public boolean allows(String value) {
			return StringUtils.isNumeric(value) ? Integer.valueOf(value) > 0 : false;
		}
	},

	SCSETTING_PERM("SCSetting.Permission", SettingType.STRING);

	private final String path;
	private final SettingType type;

	private SCSetting(String path, SettingType type) {
		this.path = path;
		this.type = type;
	}

	public static SCSetting of(String name) {
		if (name == null || name.isEmpty())
			return null;
		try {
			return valueOf(name);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public static List<SCSetting> ofType(SettingType type) {
		List<SCSetting> res = new ArrayList<>();
		for (SCSetting setting : SCSetting.values())
			if (setting.getType().equals(type))
				res.add(setting);
		return res;
	}

	public static List<String> namesOfType(SettingType type) {
		List<String> res = new ArrayList<>();
		for (SCSetting setting : SCSetting.values())
			if (setting.getType().equals(type))
				res.add(setting.name());
		return res;
	}

	public String getPath() {
		return path;
	}

	public SettingType getType() {
		return type;
	}

	public enum SettingType {
		/** Represents a boolean value, true or false.*/
		BOOLEAN,
		/** Represents a positive integer value with a predefined range.*/
		INT,
		/** Represents a String.*/
		STRING,
		/** Represents a String list.*/
		STRING_LIST;

	}

	public boolean allows(String value) {
		if (getType().equals(SettingType.STRING) || getType().equals(SettingType.STRING_LIST))
			return value != null && !value.isEmpty();
		String val = value.toLowerCase();
		if (getType().equals(SettingType.BOOLEAN))
			return val.equals("true") || val.equals("false");
		else
			return StringUtils.isNumeric(val);
	}
}
