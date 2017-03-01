package de.gabik21.hospitalcore.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.gabik21.hospitalcore.HospitalCore;

public class AlertCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!sender.hasPermission("Owner"))
	    return true;

	List<String> bc = HospitalCore.inst().getConfig().getStringList("bc");
	if (args.length >= 2) {

	    if (args[0].equalsIgnoreCase("add")) {

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; i++)
		    sb.append(args[i]).append(" ");

		String allArgs = sb.toString().trim().replaceAll("&", "§");
		bc.add(allArgs);
		HospitalCore.inst().getConfig().set("bc", bc);
		sender.sendMessage("§aAdded: §r" + allArgs);

		return true;
	    }

	    if (args[0].equalsIgnoreCase("remove")) {

		int i = 0;
		try {
		    i = Integer.valueOf(args[1]);
		} catch (NumberFormatException e) {
		    sender.sendMessage("Must be a number");
		}
		String s = bc.remove(i - 1);
		HospitalCore.inst().getConfig().set("bc", bc);
		sender.sendMessage("§aRemoved: §r" + s);

		return true;
	    }

	}

	if (args.length == 1 && args[0].equalsIgnoreCase("list"))
	    for (String s : bc)
		sender.sendMessage(s);

	return true;
    }

}
