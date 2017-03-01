package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;

public class FixCommand implements CommandExecutor {

    private HospitalCore main;

    public FixCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) {

	    sender.sendMessage("You can only use this as a player");
	    return true;

	}

	final Player p = (Player) sender;

	if (label.equalsIgnoreCase("fix")) {

	    if (args.length == 0) {

		main.resetPosition(p);

		p.sendMessage(main.getConfig().getString("messages.commands.fix"));

		return true;

	    }

	    if (args.length == 1) {

		if (Bukkit.getPlayer(args[0]) == null) {

		    p.sendMessage(main.getConfig().getString("prefix") + " §cThe Player doesn't exist!");
		    return true;

		}

		main.resetPosition(Bukkit.getPlayer(args[0]));

		p.sendMessage(main.getConfig().getString("messages.commands.fixother").replace("<player>",
			Bukkit.getPlayer(args[0]).getName()));

		return true;

	    }

	}

	return false;
    }

}
