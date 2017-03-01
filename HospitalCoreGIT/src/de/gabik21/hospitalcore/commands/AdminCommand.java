package de.gabik21.hospitalcore.commands;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.util.Inventories;

public class AdminCommand implements CommandExecutor {

    private HospitalCore main;
    private HashMap<String, ItemStack[]> invsave = new HashMap<String, ItemStack[]>();
    private HashMap<String, ItemStack[]> armorsave = new HashMap<String, ItemStack[]>();

    public AdminCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) {

	    sender.sendMessage("You can only use this command as a player!");
	    return true;

	}

	Player p = (Player) sender;

	if (label.equalsIgnoreCase("admin")) {

	    if (args.length == 0) {

		if (p.hasPermission("Owner") || p.hasPermission("Moderator") || p.hasPermission("Supporter")) {

		    if (HospitalCore.getData(p).isInAdminmode()) {

			disableAdminMode(p);

		    } else {

			enableAdminMode(p);

		    }

		} else {

		    p.sendMessage(
			    main.getConfig().getString("prefix") + main.getConfig().getString("messages.nopermission"));

		}

		return true;
	    }

	}

	return false;
    }

    private void enableAdminMode(Player p) {

	if (invsave.containsKey(p.getName()))
	    invsave.remove(p.getName());
	if (armorsave.containsKey(p.getName()))
	    armorsave.remove(p.getName());

	invsave.put(p.getName(), p.getInventory().getContents());
	armorsave.put(p.getName(), p.getInventory().getArmorContents());

	p.getInventory().setArmorContents(null);
	p.getInventory().clear();

	PlayerData pd = HospitalCore.getData(p);
	pd.setAdminmode(true);
	p.setAllowFlight(true);

	Inventories.ADMIN.apply(p);

	main.disguise(p);
	p.setGameMode(GameMode.CREATIVE);

	p.sendMessage(main.getConfig().getString("messages.commands.adminon"));

    }

    private void disableAdminMode(Player p) {

	if (!invsave.containsKey(p.getName()) || !armorsave.containsKey(p.getName())) {

	    p.sendMessage(main.getConfig().getString("prefix")
		    + " §cAn error accurred while disabling the staff mode. Please reconnect!");

	}

	PlayerData pd = HospitalCore.getData(p);

	if (pd.canBuild())
	    p.performCommand("build");

	pd.setAdminmode(false);
	p.setAllowFlight(false);
	p.setGameMode(GameMode.SURVIVAL);

	p.getInventory().setContents(invsave.get(p.getName()));
	p.getInventory().setArmorContents(armorsave.get(p.getName()));

	armorsave.remove(p.getName());
	invsave.remove(p.getName());

	main.undisguise(p);

	p.sendMessage(main.getConfig().getString("messages.commands.adminoff"));

    }

}
