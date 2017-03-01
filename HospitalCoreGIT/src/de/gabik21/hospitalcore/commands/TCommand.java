package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class TCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (pd.getTeam() == null) {

	    p.sendMessage(HospitalCore.inst().getConfig().getString("prefix") + "§cYou aren't in a team.");
	    return true;
	}

	if (args.length >= 1) {

	    StringBuilder sb = new StringBuilder();

	    for (int i = 0; i < args.length; i++) {

		sb.append(args[i]).append(" ");

	    }
	    String allArgs = sb.toString().trim().replaceAll("&", "§");

	    pd.getTeam().sendMessage("§6§l[§cTeam§6§l] §2" + p.getName() + "§7§l> §r" + allArgs);

	    return true;
	}

	return false;
    }

}
