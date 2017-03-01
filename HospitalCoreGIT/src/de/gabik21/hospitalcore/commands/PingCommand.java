package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;

public class PingCommand implements CommandExecutor {

    private HospitalCore main;

    public PingCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (args.length == 0) {

	    p.sendMessage(main.getConfig().getString("prefix") + main.getConfig().getString("messages.commands.ping")
		    .replace("<ping>", String.valueOf(getPing(p))));

	    return true;

	}

	return false;
    }

    public int getPing(Player player) {
	return ((CraftPlayer) player).getHandle().ping;
    }
}
