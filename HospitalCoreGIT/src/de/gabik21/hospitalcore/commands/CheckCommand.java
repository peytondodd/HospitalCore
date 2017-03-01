package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;

public class CheckCommand implements CommandExecutor {

    private HospitalCore main;

    public CheckCommand(HospitalCore main) {

	this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (args.length == 1) {

	    if (Bukkit.getPlayer(args[0]) == null) {
		p.sendMessage("§cThis player isn't online!");
		return true;
	    }

	    if (p.hasPermission("Supporter") || p.hasPermission("Moderator") || p.hasPermission("Owner")) {

		Player t = Bukkit.getPlayer(args[0]);

		p.teleport(t);

		if (!HospitalCore.getData(p).isInAdminmode())
		    p.performCommand("admin");

		return true;
	    } else {

		p.sendMessage(main.getConfig().getString("messages.nopermission"));

		return true;

	    }
	}

	return false;
    }
}
