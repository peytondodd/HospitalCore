package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class SeeKitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!sender.hasPermission("Supporter") && !sender.hasPermission("Moderator"))
	    return true;

	if (args.length == 1) {

	    Player target = Bukkit.getPlayer(args[0]);
	    PlayerData pd = HospitalCore.getData(target);

	    if (target != null) {
		sender.sendMessage("§c" + target.getName() + "'s kits: " + pd.getKitConfig().getNames());
	    } else {
		sender.sendMessage("§cIsn't online.");
	    }

	    return true;

	}

	return false;
    }

}
