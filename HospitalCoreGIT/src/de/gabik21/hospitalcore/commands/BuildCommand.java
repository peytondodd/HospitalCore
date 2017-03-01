package de.gabik21.hospitalcore.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class BuildCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
	if (!(sender instanceof Player))
	    return true;

	Player p = (Player) sender;

	if (p.hasPermission("Moderator") || p.hasPermission("Builder")) {

	    PlayerData pd = HospitalCore.getData(p);

	    if (pd.canBuild())
		disableBuild(p, pd);
	    else
		enableBuild(p, pd);

	}

	return false;
    }

    private void disableBuild(Player p, PlayerData pd) {
	pd.setBuild(false);
	p.getInventory().removeItem(new ItemStack(Material.WOOD_AXE));
	if (!pd.isInAdminmode())
	    p.setGameMode(GameMode.SURVIVAL);
	p.sendMessage("§7You are no longer in §5§lBuild §7mode.");

    }

    private void enableBuild(Player p, PlayerData pd) {
	pd.setBuild(true);
	p.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
	p.setGameMode(GameMode.CREATIVE);
	p.sendMessage("§7You are now in §5§lBuild §7mode.");

    }

}
