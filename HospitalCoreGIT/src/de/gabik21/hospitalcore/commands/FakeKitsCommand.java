package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class FakeKitsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	if (!sender.hasPermission("Owner"))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (pd.isFakekits()) {
	    pd.setFakekits(false);
	    p.sendMessage("§aFake kits disabled!");
	} else {
	    pd.setFakekits(true);
	    p.sendMessage("§aFake kits enabled!");
	}

	return true;
    }

}
