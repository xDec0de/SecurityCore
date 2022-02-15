package es.xdec0de.securitycore.utils.files;

import java.util.List;

import org.bukkit.command.CommandSender;

public enum SCSetting {

	AHS_ENABLED("AHS.Enabled"),
	AHS_BYPASS_PERMISSION("AHS.BypassPerm"),
	AHS_NOTIFY_PERMISSION("AHS.Notify.Permission"),
	AHS_NOTIFY_CONSOLE("AHS.Notify.Console"),
	AHS_NOTIFY_PLAYERS("AHS.Notify.Players"),

	ANTITAB_ENABLED("AntiTab.Enabled"),
	ANTITAB_BYPASS_PERMISSION("AntiTab.BypassPerm"),

	WARNCMDS_BYPASS_PERMISSION("WarnCMDs.BypassPerm"),
	WARNCMDS_NOTIFY_PERMISSION("WarnCMDs.Notify.Permission"),
	WARNCMDS_NOTIFY_CONSOLE("WarnCMDs.Notify.Console"),
	WARNCMDS_NOTIFY_PLAYERS("WarnCMDs.Notify.Players"),
	WARNCMDS_LIST("WarnCMDs.List"),

	BLOCKEDCMDS_ENABLED("BlockedCMDs.Enabled"),
	BLOCKEDCMDS_BYPASS_PERMISSION("BlockedCMDs.BypassPerm"),
	BLOCKEDCMDS_NOTIFY_PERMISSION("BlockedCMDs.Notify.Permission"),
	BLOCKEDCMDS_NOTIFY_CONSOLE("BlockedCMDs.Notify.Console"),
	BLOCKEDCMDS_NOTIFY_PLAYERS("BlockedCMDs.Notify.Players"),
	BLOCKEDCMDS_LIST("BlockedCMDs.List"),

	RELOAD_PERMISSION("ReloadPerm");

	private String path;

	SCSetting(String string) {
		this.path = string;
	}

	/**
	 * Gets the path on config.yml to the setting.
	 * 
	 * @return The path of the setting.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the setting as a string.
	 * 
	 * @return The setting as a string.
	 */
	public String asString() {
		return SCConfig.cfg.getString(path);
	}

	/**
	 * Gets the setting as a boolean.
	 * 
	 * @return The setting as a boolean.
	 */
	public boolean asBoolean() {
		return SCConfig.cfg.getBoolean(path);
	}

	public List<String> asList() {
		return SCConfig.cfg.getStringList(path);
	}

	/**
	 * Checks if <b>sender</b> has permission to the setting.
	 * 
	 * @param sender The sender to check.
	 * @param message Whether to send {@link SCMessage#NO_PERM} to the sender if it doesn't have permission or not.
	 * 
	 * @return true if the sender has permission, false otherwise.
	 */
	public boolean asPermission(CommandSender sender, boolean message) {
		if(sender.hasPermission(SCConfig.cfg.getString(path)))
			return true;
		//if(message)
			//SCMessage.NO_PERM.send(sender);
		return false;
	}
}
