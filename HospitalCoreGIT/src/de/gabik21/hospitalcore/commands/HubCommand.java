package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HubCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	sender.sendMessage("§c§lYou are very pathetic in your lifestyle... no one uses /hub, use /spawn instead!");
	return true;
    }

}
