package es.xdec0de.securitycore.features;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.FieldAccessException;

import es.xdec0de.securitycore.SecurityCore;

public class AntiTab {

	private static boolean setup;

	public static boolean setup() {
		if(!setup) {
			ProtocolManager manager = ProtocolLibrary.getProtocolManager();
			manager.addPacketListener((PacketListener)new PacketAdapter(SecurityCore.getPlugin(SecurityCore.class), ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.TAB_COMPLETE}) {

				public void onPacketReceiving(PacketEvent event) {
					if(event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
						try {
							if(event.getPlayer().hasPermission(SecurityCore.config.getString("AntiTab-bypass-permission")))
								return;
							PacketContainer packet = event.getPacket();
							String message = ((String)packet.getSpecificModifier(String.class).read(0)).toLowerCase();
							if (message.startsWith("/") && !message.contains(" ") || message.startsWith("/ver") && !message.contains("  ") || message.startsWith("/version") && !message.contains("  ") || message.startsWith("/?") && !message.contains("  ") || message.startsWith("/about") && !message.contains("  ") || message.startsWith("/help") && !message.contains("  ")) {
								event.setCancelled(true);
							}
						} catch (FieldAccessException e) {
							Bukkit.getConsoleSender().sendMessage("Temporary AntiTab error message.");
						}
					}
				}
			});
			setup = true;
		}
		return setup;
	}
}
