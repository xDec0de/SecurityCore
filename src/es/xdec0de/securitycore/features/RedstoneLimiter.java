package es.xdec0de.securitycore.features;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import es.xdec0de.securitycore.SecurityCore;

public class RedstoneLimiter implements Listener {

	private final SecurityCore plugin;
	HashMap<Location, Integer> pulses = new HashMap<>();

	public RedstoneLimiter(SecurityCore plugin) {
		this.plugin = plugin;
		Bukkit.getScheduler().runTaskTimer(plugin, () -> checkPulses(), 20, 20);
	}

	private void checkPulses() {
		pulses.forEach((loc, exec) -> {
			if (exec > plugin.getCfg().getInt("RedstoneLimiter.MaxPulsesPerSecond"))
				loc.getWorld().getBlockAt(loc).breakNaturally();
		});
		pulses.clear();
	}

	@EventHandler
	public void onRedstone(BlockRedstoneEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	@EventHandler
	public void onPistonRetract(BlockPistonExtendEvent e) {
		handleRedstonePulse(e.getBlock().getLocation());
	}

	private void handleRedstonePulse(Location loc) {
		Integer exec = pulses.get(loc);
		if (exec == null)
			pulses.put(loc, 1);
		else
			pulses.put(loc, exec + 1);
	}
}
