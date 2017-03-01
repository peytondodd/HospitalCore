package de.gabik21.hospitalcore.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.util.Hologram;

public class HologramCommand implements CommandExecutor {

    FileConfiguration cfg = HospitalCore.inst().getConfig();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (!p.hasPermission("Owner") && !p.hasPermission("Moderator"))
	    return true;

	if (args.length == 1 && args[0].equalsIgnoreCase("list")) {

	    ConfigurationSection section = cfg.getConfigurationSection("holograms");

	    if (section != null) {

		for (String s : section.getKeys(false))
		    p.sendMessage(s + ": \"" + cfg.getStringList("holograms." + s + ".message") + "\"");

	    } else
		p.sendMessage("§cNo holograms added yet.");

	    return true;
	}

	if (args.length == 1 && args[0].equalsIgnoreCase("refresh")) {

	    for (Hologram hologram : Hologram.getHolograms()) {
		hologram.reset();
	    }

	    return true;
	}

	if (args.length == 2) {

	    if (args[0].equalsIgnoreCase("remove")) {

		Hologram h = getHologram(args[1]);

		if (h != null) {
		    h.destroy();
		    cfg.set("holograms." + args[1], null);
		    p.sendMessage("§aremoved.");
		} else
		    p.sendMessage("§cThis hologram does not exist");

		return true;
	    }
	    if (args[0].equalsIgnoreCase("teleport")) {

		return true;
	    }

	}

	if (args.length > 2) {

	    if (args[0].equalsIgnoreCase("add")) {

		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < args.length; i++)
		    sb.append(args[i]).append(" ");

		String allArgs = sb.toString().trim().replaceAll("&", "§");

		Location eyeloc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY(),
			p.getEyeLocation().getZ());

		Hologram h = new Hologram(allArgs);
		h.show(eyeloc);
		Location loc = h.getLocation();

		cfg.set("holograms." + args[1] + ".message", Arrays.asList(allArgs));
		cfg.set("holograms." + args[1] + ".x", loc.getX());
		cfg.set("holograms." + args[1] + ".y", loc.getY());
		cfg.set("holograms." + args[1] + ".z", loc.getZ());

		return true;
	    }
	}

	if (args.length > 2) {

	    if (args[0].equalsIgnoreCase("addline")) {

		Hologram h = getHologram(args[1]);

		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < args.length; i++)
		    sb.append(args[i]).append(" ");

		String allArgs = sb.toString().trim().replaceAll("&", "§");

		if (h != null) {

		    h.addLine(allArgs);
		    p.sendMessage("§aAdded a line.");

		    cfg.set("holograms." + args[1] + ".message", h.getLines());

		} else
		    p.sendMessage("§cThis hologram does not exist");

		return true;
	    }
	}

	return false;
    }

    private Hologram getHologram(String name) {

	Location loc = new Location(Bukkit.getWorlds().get(0), cfg.getDouble("holograms." + name + ".x"),
		cfg.getDouble("holograms." + name + ".y"), cfg.getDouble("holograms." + name + ".z"));

	for (Hologram h : Hologram.getHolograms())
	    if (loc.equals(h.getLocation()))
		return h;

	return null;

    }

}
