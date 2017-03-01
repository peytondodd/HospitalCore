package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.gui.ReportGui;
import de.gabik21.hospitalcore.types.Report;

public class ReportCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("list")) {

		if (p.hasPermission("Owner") || p.hasPermission("Moderator") || p.hasPermission("Supporter")) {

		    new ReportGui(54, "§cReports", p);

		    return true;

		} else {

		    p.sendMessage("§cNo permission");
		    return true;

		}

	    }

	}

	if (args.length >= 2) {

	    if ((System.currentTimeMillis() - HospitalCore.getData(p).getLastReport()) < 30000) {

		p.sendMessage("§cYou can only report every 30 seconds");
		return true;

	    }

	    HospitalCore.getData(p).report();

	    Player t = Bukkit.getPlayer(args[0]);
	    StringBuilder sb = new StringBuilder();
	    for (int i = 1; i < args.length; i++) {
		sb.append(args[i]).append(" ");
	    }

	    String allArgs = sb.toString().trim();

	    if (t != null) {

		new Report(p.getName(), t.getName(), allArgs);
		p.sendMessage("§aReport sucessfully sent");
		informStaff(
			"§c§l[§2Reports§c§l] §6Player " + t.getName() + " has been reported! Check the report menu!");
		return true;

	    }

	}

	return false;
    }

    @SuppressWarnings("deprecation")
    private void informStaff(String s) {

	for (Player p : Bukkit.getOnlinePlayers()) {

	    if (p.hasPermission("Owner") || p.hasPermission("Moderator") || p.hasPermission("Supporter")) {

		p.sendMessage(s);
		Bukkit.getConsoleSender().sendMessage(s);

	    }

	}

    }

}
