package de.gabik21.hospitalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.Chest;
import de.gabik21.hospitalcore.types.PlayerData;

public class ChestCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (pd.getChests() <= 0)
	    return true;

	pd.setChests(pd.getChests() - 1);
	new Chest(p);

	return true;
    }

}
