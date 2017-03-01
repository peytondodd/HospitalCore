package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.LoadedData;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.SavingUnit;

public class MoneyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {

	if (!(sender instanceof Player))
	    return true;

	final Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (args.length == 0) {
	    p.sendMessage("§e§lYour current balance is §c§l" + pd.getMoney() + "$");
	    return true;
	}

	if (args.length == 3 && args[0].equalsIgnoreCase("give") && p.hasPermission("Owner")) {

	    Player t = Bukkit.getPlayer(args[1]);
	    long l;

	    try {

		l = Long.parseLong(args[2]);

	    } catch (NumberFormatException e) {

		p.sendMessage("§cSecond argument must be a number");
		return true;

	    }

	    if (t == null) {

		p.sendMessage("§cThis player isn't online!");
		return true;
	    }

	    PlayerData td = HospitalCore.getData(t);
	    td.addMoney(l);

	    p.sendMessage("§aSucessfully sent " + l + "$ to " + t.getName());
	    t.sendMessage("§aYou received " + l + "$ from " + p.getName());

	    return true;

	}

	if (args.length == 1) {

	    Player t = Bukkit.getPlayer(args[0]);

	    if (t == null) {

		p.sendMessage("§ePlayer isn't online, retrieving information from database...");
		new BukkitRunnable() {
		    @SuppressWarnings("deprecation")
		    public void run() {
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			LoadedData data = SavingUnit.loadData(target.getUniqueId(), false);
			p.sendMessage("§e" + target.getName() + "'s balance is " + data.getMoney());
		    }
		}.runTaskAsynchronously(HospitalCore.inst());
		return true;
	    }

	    PlayerData td = HospitalCore.getData(t);
	    p.sendMessage("§e" + t.getName() + "'s balance is " + td.getMoney());
	    return true;

	}

	return false;
    }

}
