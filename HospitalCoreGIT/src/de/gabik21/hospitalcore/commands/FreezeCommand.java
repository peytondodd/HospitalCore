package de.gabik21.hospitalcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.types.PlayerData;

public class FreezeCommand implements CommandExecutor {

    private HospitalCore main;

    public FreezeCommand(HospitalCore main) {

	this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (label.equalsIgnoreCase("freeze")) {

	    if (args.length == 1) {

		if (Bukkit.getPlayer(args[0]) == null)
		    return false;

		if (sender.hasPermission("Owner") || sender.hasPermission("Moderator")
			|| sender.hasPermission("Supporter")) {

		    Player t = Bukkit.getPlayer(args[0]);
		    PlayerData td = HospitalCore.getData(t);

		    if (td.isFrozen()) {

			unfreeze(t, td);
			sender.sendMessage(main.getConfig().getString("prefix") + main.getConfig()
				.getString("messages.commands.unfreeze").replace("<player>", t.getName()));

		    } else {

			freeze(t, td);
			sender.sendMessage(main.getConfig().getString("prefix") + main.getConfig()
				.getString("messages.commands.freeze").replace("<player>", t.getName()));
		    }

		    return true;

		} else {

		    sender.sendMessage(main.getConfig().getString("messages.nopermission"));
		    return true;
		}
	    }

	}

	return false;
    }

    private void freeze(final Player t, final PlayerData td) {

	t.setWalkSpeed(0);
	td.setFrozen(true);
	send(t);

	new BukkitRunnable() {
	    public void run() {

		if (t.isOnline() && td.isFrozen())
		    send(t);
		else
		    cancel();

	    }
	}.runTaskTimerAsynchronously(main, 10 * 20L, 10 * 20);

    }

    private void send(Player t) {
	t.sendMessage("§c-------------");
	t.sendMessage("§cYou have been frozen");
	t.sendMessage("§cby a staff member.");
	t.sendMessage("§cPlease follow his instructions.");
	t.sendMessage("§cDo not log out.");
	t.sendMessage("§c-------------");
    }

    private void unfreeze(Player t, PlayerData td) {

	td.setWalkSpeed(0.2F);
	t.removePotionEffect(PotionEffectType.JUMP);

	td.setFrozen(false);

	t.sendMessage(main.getConfig().getString("prefix") + "§aYou have been unfrozen");

    }

}
