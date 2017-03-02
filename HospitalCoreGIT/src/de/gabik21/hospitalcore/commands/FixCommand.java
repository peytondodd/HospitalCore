package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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
		for (Entity ent : p.getNearbyEntities(50, 50, 50)) {
		    if (ent instanceof Player) {
			main.resetPosition((Player) ent, p);
		    }
		}
		p.sendMessage(main.getConfig().getString("messages.commands.fix"));

		return true;

	    }

	}

	return false;
    }

}
