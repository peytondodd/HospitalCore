package de.gabik21.hospitalcore.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;

public class SetSpawnCommand implements CommandExecutor {

    private HospitalCore main;

    public SetSpawnCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (label.equalsIgnoreCase("setspawn")) {

	    if (p.hasPermission("Owner")) {

		Location loc = p.getLocation();

		if (args.length == 0) {

		    main.getConfig().set("spawn.x", loc.getX());
		    main.getConfig().set("spawn.y", loc.getY());
		    main.getConfig().set("spawn.z", loc.getZ());
		    main.getConfig().set("spawn.pitch", loc.getPitch());
		    main.getConfig().set("spawn.yaw", loc.getYaw());

		    main.saveConfig();
		    p.sendMessage(main.getConfig().getString("prefix") + "§aSpawn sucessfully setted!");

		    return true;
		}

		if (args.length == 1) {

		    main.getConfig().set(args[0] + ".x", loc.getX());
		    main.getConfig().set(args[0] + ".y", loc.getY());
		    main.getConfig().set(args[0] + ".z", loc.getZ());
		    main.getConfig().set(args[0] + ".pitch", loc.getPitch());
		    main.getConfig().set(args[0] + ".yaw", loc.getYaw());

		    main.saveConfig();
		    p.sendMessage(main.getConfig().getString("prefix") + "§aSpawn sucessfully setted!");

		    return true;
		}
	    } else {

		p.sendMessage(main.getConfig().getString("messages.nopermission"));

	    }

	}

	return false;
    }
}
