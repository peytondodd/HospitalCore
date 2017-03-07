package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
	if (!sender.hasPermission("Supporter") && !sender.hasPermission("Moderator"))
	    return true;

	for (int i = 0; i < 100; i++)
	    Bukkit.broadcastMessage("");

	Bukkit.broadcastMessage("§aChat has been cleared by " + sender.getName());
	return true;
    }

}
