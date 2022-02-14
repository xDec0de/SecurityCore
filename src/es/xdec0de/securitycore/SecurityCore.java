package es.xdec0de.securitycore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.FieldAccessException;
import java.io.File;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SecurityCore extends JavaPlugin implements Listener, TabExecutor {

    public static SecurityCore plugin;
    FileConfiguration config;
    File cfile;
    ProtocolManager protocolManager;

    public void onEnable() {
        plugin = this;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
        this.cfile = new File(this.getDataFolder(), "config.yml");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("enable"))));
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.saveDefaultConfig();
        if (this.config.getBoolean("AntiTab-enabled")) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
            this.protocolManager.addPacketListener((PacketListener)new PacketAdapter((Plugin)this, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.TAB_COMPLETE}){

                public void onPacketReceiving(PacketEvent event) {
                    if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                        try {
                            if (event.getPlayer().hasPermission(SecurityCore.this.config.getString("AntiTab-bypass-permission"))) {
                                return;
                            }
                            PacketContainer packet = event.getPacket();
                            String message = ((String)packet.getSpecificModifier(String.class).read(0)).toLowerCase();
                            if (message.startsWith("/") && !message.contains(" ") || message.startsWith("/ver") && !message.contains("  ") || message.startsWith("/version") && !message.contains("  ") || message.startsWith("/?") && !message.contains("  ") || message.startsWith("/about") && !message.contains("  ") || message.startsWith("/help") && !message.contains("  ")) {
                                event.setCancelled(true);
                            }
                        }
                        catch (FieldAccessException e) {
                            SecurityCore.this.getLogger().log(Level.SEVERE, "Couldn't access field.", e);
                        }
                    }
                }
            });
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + "&eWARNING: &4AntiTab &cis disabled, your plugins, spigot version etc... are visible")));
        }
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("disable"))));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String notify;
        String notify2;
        if (this.config.getList("ignore-blocked-cmds").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!this.config.getList("ignore-blocked-cmds").contains(e.getPlayer().getName()) && this.config.getList("blocked-cmds").contains(e.getMessage())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("blocked-cmd"))));
            if (this.config.getBoolean("notify-console")) {
                notify2 = this.config.getString("notify");
                notify2 = notify2.replaceAll("%command%", e.getMessage());
                notify2 = notify2.replaceAll("%player%", e.getPlayer().getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + notify2)));
            }
            if (this.config.getBoolean("notify-enabled")) {
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayers.hasPermission(this.config.getString("notify-block-permission"))) continue;
                    notify = this.config.getString("notify");
                    notify = notify.replaceAll("%command%", e.getMessage());
                    notify = notify.replaceAll("%player%", e.getPlayer().getName());
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + notify)));
                }
            }
        }
        if (this.config.getList("ignore-warn-cmds").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!this.config.getList("ignore-warn-cmds").contains(e.getPlayer().getName())) {
            if (this.config.getList("warn-cmds").contains(e.getMessage())) {
                if (this.config.getBoolean("warn-console")) {
                    String warn = this.config.getString("warn-message");
                    warn = warn.replaceAll("%command%", e.getMessage());
                    warn = warn.replaceAll("%player%", e.getPlayer().getName());
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + warn)));
                }
                if (this.config.getBoolean("warn-enabled")) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        if (!onlinePlayers.hasPermission(this.config.getString("notify-warn-permission"))) continue;
                        String warn = this.config.getString("warn-message");
                        warn = warn.replaceAll("%command%", e.getMessage());
                        warn = warn.replaceAll("%player%", e.getPlayer().getName());
                        onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + warn)));
                    }
                }
            }
            if (!this.config.getBoolean("warn-enabled")) {
                e.setCancelled(false);
            }
        }
        if (this.config.getList("ignore-hidden-syntax").contains(e.getPlayer().getName())) {
            e.setCancelled(false);
        }
        if (!this.config.getList("ignore-hidden-syntax").contains(e.getPlayer().getName()) && e.getMessage().split(" ")[0].contains(":")) {
            if (this.config.getBoolean("notify-console")) {
                notify2 = this.config.getString("notify");
                notify2 = notify2.replaceAll("%command%", e.getMessage());
                notify2 = notify2.replaceAll("%player%", e.getPlayer().getName());
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + notify2)));
            }
            if (this.config.getBoolean("notify-enabled")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("syntaxnotallowed"))));
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayers.hasPermission(this.config.getString("notify-syntax-permission"))) continue;
                    notify = this.config.getString("notify");
                    notify = notify.replaceAll("%command%", e.getMessage());
                    notify = notify.replaceAll("%player%", e.getPlayer().getName());
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + notify)));
                }
            }
            if (!this.config.getBoolean("notify-enabled")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("syntaxnotallowed"))));
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ahs")) {
            if (sender.hasPermission(this.config.getString("ahs-command-permission"))) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("NotEnoughArgs"))));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(this.config.getString("ahs-reload-permission"))) {
                        this.config = YamlConfiguration.loadConfiguration((File)this.cfile);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("reload-msg"))));
                    }
                    if (!sender.hasPermission(this.config.getString("ahs-reload-permission"))) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("NoPerm"))));
                    }
                } else if (args[0].equalsIgnoreCase("rl")) {
                    if (sender.hasPermission(this.config.getString("ahs-reload-permission"))) {
                        this.config = YamlConfiguration.loadConfiguration((File)this.cfile);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("reload-msg"))));
                    }
                    if (!sender.hasPermission(this.config.getString("ahs-reload-permission"))) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("NoPerm"))));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("usage"))));
                }
            }
            if (!sender.hasPermission(this.config.getString("ahs-command-permission"))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(this.config.getString("prefix")) + " " + this.config.getString("NoPerm"))));
            }
        }
        return true;
    }
}

