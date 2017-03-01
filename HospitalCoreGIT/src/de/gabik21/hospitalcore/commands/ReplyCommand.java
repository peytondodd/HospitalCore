package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class ReplyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (pd.getReply() == null || !pd.getReply().isOnline()) {
	    sender.sendMessage("§cYou got nobody to reply to");
	    return true;
	}
	StringBuilder message = new StringBuilder();

	for (int i = 0; i < args.length; i++) {
	    if (i > 0)
		message.append(" ");
	    message.append(args[i]);
	}
	p.performCommand("msg " + pd.getReply().getName() + " " + message);

	return true;
    }
}
