package es.xdec0de.securitycore.utils.files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import es.xdec0de.securitycore.SecurityCore;
import es.xdec0de.securitycore.utils.Replacer;

public class Messages extends SCFile {

	private final Pattern HEX_PATTERN;

	public Messages(SecurityCore plugin) {
		super(plugin, "messages.yml");
		HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");
	}

	public String applyColor(String string) {
		if(string == null)
			return "null";
		char COLOR_CHAR = '\u00A7';
		Matcher matcher = HEX_PATTERN.matcher(string);
		StringBuffer buffer = new StringBuffer(string.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, COLOR_CHAR + "x"
					+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
					+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
					+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
		}
		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}

	// Loggers //

	/**
	 * Sends the specified string to the console, if the string is null, "null" will be sent, if the string is empty, 
	 * nothing will be done.
	 * 
	 * @param str The string to send
	 */
	public void log(String str) {
		if(str == null)
			str = "null";
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Applies color ({@link #applyColor(String)}) to the specified string and then sends it to the console, if the string is null, "null" will be sent, 
	 * if the string is empty, nothing will be done.
	 * 
	 * @param str The string to send
	 */
	public void logCol(String str) {
		if(str == null)
			str = "null";
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(applyColor(str));
	}

	/**
	 * Applies color ({@link #applyColor(String)}) and replacements ({@link Replacer}) to the specified string and then sends it to the console, 
	 * if the string is null, "null" will be sent, if the string is empty, nothing will be done.
	 * 
	 * @param str The string to send ({@link #applyColor(String)})
	 * @replacements The replacements to apply to the string ({@link Replacer})
	 */
	public void logCol(String str, String... replacements) {
		if(str == null)
			str = "null";
		if(!str.isEmpty())
			Bukkit.getConsoleSender().sendMessage(applyColor(new Replacer(replacements).replaceAt(str)));
	}

	Replacer getDefaultReplacer() {
		return new Replacer("%prefix%", cfg.getString("PREFIX"), "%error%", cfg.getString("ERROR_PREFIX"));
	}

	public String get(String path) {
		return applyColor(getDefaultReplacer().replaceAt(cfg.getString(path)));
	}

	public String get(String path, Replacer replacer) {
		return applyColor(getDefaultReplacer().add(replacer).replaceAt(cfg.getString(path)));
	}

	public String get(String path, String... replacements) {
		return applyColor(getDefaultReplacer().add(replacements).replaceAt(cfg.getString(path)));
	}

	public void send(String path, CommandSender sender) {
		sender.sendMessage(get(path));
	}

	public void send(String path, CommandSender sender, Replacer replacer) {
		sender.sendMessage(get(path, replacer));
	}

	public void send(String path, CommandSender sender, String... replacements) {
		sender.sendMessage(get(path, replacements));
	}
}
