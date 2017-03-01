package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class PayCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (args.length == 2) {

	    Player t = Bukkit.getPlayer(args[0]);
	    long l;

	    try {
		l = Math.abs(Long.parseLong(args[1]));
	    } catch (NumberFormatException e) {
		p.sendMessage("§cSecond argument must be a number");
		return true;
	    }

	    if (l < 20) {
		p.sendMessage("§cYou have to pay at least 20$");
		return true;
	    }

	    if (t == null) {
		p.sendMessage("§cThis player isn't online!");
		return true;
	    }

	    if (!pd.hasMoney(l)) {
		p.sendMessage("§cYou don't have enough money do /money to check your balance!");
		return true;
	    }

	    PlayerData td = HospitalCore.getData(t);
	    td.addMoney(l);
	    pd.addMoney(-l);

	    p.sendMessage("§aSucessfully sent " + l + "$ to " + t.getName());
	    t.sendMessage("§aYou received " + l + "$ from " + p.getName());

	    return true;

	}

	return false;
    }

}
