package de.gabik21.hospitalcore.commands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class SpawnCommand implements CommandExecutor {

    private HospitalCore main;
    private ArrayList<String> teleportqueue = new ArrayList<String>();
    private static final String SPAWN_MESSAGE = "§aTeleported to the spawn";

    public SpawnCommand(HospitalCore main) {

	this.main = main;

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player))
	    return true;

	final Player p = (Player) sender;
	final PlayerData pd = HospitalCore.getData(p);

	if (args.length == 0) {

	    if (pd.isIn1v1() || teleportqueue.contains(p.getName()) || pd.isHg()
		    || (pd.getTeam() != null && pd.getTeam().isQueued()))
		return true;

	    if (pd.isIngame() && !pd.isInChallenge()) {

		final Location curloc = p.getLocation();
		teleportqueue.add(p.getName());
		p.sendMessage(
			main.getConfig().getString("prefix") + "§cYou'll be teleported in 4 seconds. Do not move!");

		new BukkitRunnable() {
		    public void run() {

			if (p.isOnline()) {

			    if (p.getLocation().getBlockX() == curloc.getBlockX()
				    && p.getLocation().getBlockZ() == curloc.getBlockZ()) {

				pd.spawn();
				p.sendMessage(HospitalCore.PREFIX + SPAWN_MESSAGE);

			    } else {

				p.sendMessage(HospitalCore.PREFIX + "§cTeleportation cancelled. You moved!");

			    }
			}

			teleportqueue.remove(p.getName());

		    }
		}.runTaskLater(main, 4 * 20);

		return true;

	    }

	    pd.setSpawnAtChallenge(false);
	    pd.spawn();
	    p.sendMessage(HospitalCore.PREFIX + SPAWN_MESSAGE);

	    return true;

	}

	return false;
    }

}
