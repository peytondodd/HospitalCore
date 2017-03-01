package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.gabik21.hospitalcore.Listeners;

public class GlobalMuteCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!sender.hasPermission("Moderator") || !sender.hasPermission("Supporter"))
	    return true;

	if (Listeners.GLOBAL_CHATMUTE.get()) {
	    Bukkit.broadcastMessage("§aGlobal chat is now unmuted");
	    Listeners.GLOBAL_CHATMUTE.set(false);
	} else {
	    Bukkit.broadcastMessage("§aGlobal chat is now muted");
	    Listeners.GLOBAL_CHATMUTE.set(true);
	}

	return true;
    }

}
