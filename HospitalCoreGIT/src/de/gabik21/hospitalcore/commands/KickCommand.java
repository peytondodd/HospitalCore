package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!sender.hasPermission("Supporter") && !sender.hasPermission("Moderator"))
	    return true;

	if (args.length < 2)
	    return false;

	Player t = Bukkit.getPlayer(args[0]);

	if (t == null)
	    return false;

	StringBuilder reasonbuilder = new StringBuilder();
	for (int i = 1; i < args.length; i++) {
	    if (i > 1)
		reasonbuilder.append(" ");
	    reasonbuilder.append(args[i]);
	}

	t.kickPlayer("§cYou have been kicked from the server!\n§cReason: " + reasonbuilder.toString());
	Bukkit.broadcastMessage(
		"§c" + t.getName() + " has been kicked by " + sender.getName() + " for " + reasonbuilder.toString());
	return true;
    }

}
