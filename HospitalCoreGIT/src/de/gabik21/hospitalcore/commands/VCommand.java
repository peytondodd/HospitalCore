package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class VCommand implements CommandExecutor {

    private HospitalCore main;

    public VCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (args.length == 0) {

	    if (p.hasPermission("Supporter") || p.hasPermission("Moderator") || p.hasPermission("Owner")) {

		if (pd.isDisguised()) {

		    main.undisguise(p);
		    p.sendMessage(main.getConfig().getString("prefix")
			    + main.getConfig().getString("messages.commands.voff"));

		} else {

		    main.disguise(p);
		    p.sendMessage(
			    main.getConfig().getString("prefix") + main.getConfig().getString("messages.commands.von"));

		}

		return true;

	    } else {

		p.sendMessage(main.getConfig().getString("messages.nopermission"));

		return true;
	    }
	}

	return false;
    }

}
