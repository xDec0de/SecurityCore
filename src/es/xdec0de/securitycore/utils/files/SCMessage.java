package es.xdec0de.securitycore.utils.files;

import org.bukkit.command.CommandSender;

import es.xdec0de.securitycore.utils.Replacer;

public enum SCMessage {

	PREFIX("SecurityCore.Prefix"),
	ERROR("SecurityCore.Error"),
	NO_PERM("SecurityCore.NoPerm"),

	AHS_NOT_ALLOWED("AHS.NotAllowed"),
	AHS_NOTIFY("AHS.Notify"),

	WARNCMDS_NOTIFY("WarnCMDs.Notify"),

	BLOCKEDCMDS_NOTIFY("BlockedCMDs.Notify"),
	BLOCKEDCMDS_BLOCKED("BlockedCMDs.Blocked"),

	RELOAD_SUCCESS("Reload.Success"),
	RELOAD_USAGE("Reload.Usage");

	private String path;

	SCMessage(String string) {
		this.path = string;
	}

	/**
	 * Gets the path on messages.yml to the message.
	 * 
	 * @return The path of the message.
	 */
	public String getPath() {
		return path;
	}

	// Getters //

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}.
	 * 
	 * @return The message as a string, with colors and the default replacer applied to it.
	 */
	public String asString() {
		return MessageUtils.applyColor(new Replacer("%prefix%", MessageUtils.prefix, "%error%", MessageUtils.errorPrefix).replaceAt(SCMessages.cfg.getString(path)));
	}

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with the 
	 * specified strings is added to the default replacer.
	 * 
	 * @param replacements The replacements to apply.
	 * 
	 * @return The message as a string, with colors and replacer applied to it.
	 */
	public String asString(String... replacements) {
		return MessageUtils.applyColor(new Replacer("%prefix%", MessageUtils.prefix, "%error%", MessageUtils.errorPrefix).add(replacements).replaceAt(SCMessages.cfg.getString(path)));
	}

	/**
	 * Gets a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with the 
	 * specified strings is added to the default replacer.
	 * 
	 * @param replacer The replacer to apply.
	 * 
	 * @return The message as a string, with colors and replacer applied to it.
	 */
	public String asString(Replacer replacer) {
		return MessageUtils.applyColor(new Replacer("%prefix%", MessageUtils.prefix, "%error%", MessageUtils.errorPrefix).add(replacer).replaceAt(SCMessages.cfg.getString(path)));
	}

	// Senders //

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * 
	 * @see #getMessage(SCMessage)
	 */
	public void send(CommandSender sender) {
		String send = asString();
		if(send != null && !send.isEmpty())
			sender.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * @param replacements The replacements to apply.
	 * 
	 * @see #getMessage(SCMessage, String...)
	 */
	public void send(CommandSender sender, String... replacements) {
		String send = asString(replacements);
		if(send != null && !send.isEmpty())
			sender.sendMessage(send);
	}

	/**
	 * Sends a message with colors {@link #applyColor(String)} and the default {@link Replacer}, also, a new replacer made with 
	 * the specified strings is added to the default replacer, empty messages will be ignored and the message wont be sent.
	 * 
	 * @param sender The sender that will receive the message.
	 * @param replacer The replacer to apply.
	 * 
	 * @see #getMessage(SCMessage, String...)
	 */
	public void send(CommandSender sender, Replacer replacer) {
		String send = asString(replacer);
		if(send != null && !send.isEmpty())
			sender.sendMessage(send);
	}
}
