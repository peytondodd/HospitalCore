package de.gabik21.hospitalcore.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.gui.BanGUI;
import de.gabik21.hospitalcore.util.BanManager;
import de.gabik21.hospitalcore.util.BanUnit;

public class BanCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {

	if (!sender.hasPermission("Admin") && !sender.hasPermission("Moderator")
		&& !sender.hasPermission("Supporter")) {

	    sender.sendMessage("§cInsufficient permissions.");
	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("ban")) {

	    if (args.length < 2)
		return false;

	    final Player t = Bukkit.getPlayer(args[0]);
	    final String toban = args[0];

	    final StringBuilder reasonbuilder = new StringBuilder();
	    for (int i = 1; i < args.length; i++) {
		if (i > 1)
		    reasonbuilder.append(" ");
		reasonbuilder.append(args[i]);
	    }

	    new BukkitRunnable() {

		@SuppressWarnings("deprecation")
		public void run() {

		    UUID uuid = Bukkit.getOfflinePlayer(toban).getUniqueId();

		    if (BanManager.isBanned(uuid.toString()))
			BanManager.unban(uuid.toString());

		    BanManager.ban(uuid.toString(), toban, reasonbuilder.toString(), -1, sender.getName());

		    final String remaining = BanManager.getRemainingTime(uuid.toString());
		    Player[] players = Bukkit.getOnlinePlayers().clone();
		    for (Player all : players)
			if (all.isOnline())
			    all.sendMessage(
				    "§c-------------\n§c" + sender.getName() + " banned player " + toban + "\nTime: "
					    + "forever \nReason: " + reasonbuilder.toString() + "\n§c-------------");
		    System.out.println("§c-------------\n§c" + sender.getName() + " banned player " + toban + "\nTime: "
			    + "forever \nReason: " + reasonbuilder.toString() + "\n§c-------------");

		    new BukkitRunnable() {

			public void run() {

			    if (t.isOnline())
				t.kickPlayer("§c-------------\n§cYou have been banned from the server. \nReason: "
					+ reasonbuilder.toString() + "\nRemaining time: " + remaining
					+ "\n§cFalsely banned? Join ts.pvphospital.eu\n §c-------------");

			}
		    }.runTask(HospitalCore.inst());

		}
	    }.runTaskAsynchronously(HospitalCore.inst());

	    return true;
	}

	if (cmd.getName().equalsIgnoreCase("tempban")) {

	    if (args.length == 1 && sender instanceof Player) {

		new BanGUI((Player) sender, args[0]);
		return true;
	    }

	    if (args.length < 4)
		return false;

	    final Player t = Bukkit.getPlayer(args[0]);
	    final String toban = args[0];

	    final StringBuilder reasonbuilder = new StringBuilder();
	    for (int i = 3; i < args.length; i++) {
		if (i > 3)
		    reasonbuilder.append(" ");
		reasonbuilder.append(args[i]);
	    }

	    String unitString = args[2];
	    List<String> unitList = BanUnit.getUnitsAsString();
	    if (!unitList.contains(unitString.toLowerCase()))
		return false;

	    final BanUnit unit = BanUnit.getUnit(unitString);
	    int timeint;
	    try {
		timeint = Integer.valueOf(args[1]);
	    } catch (NumberFormatException e) {
		sender.sendMessage("§cValue is too high for an Integer. INTEGER_MAX_VALUE = " + Integer.MAX_VALUE);
		return true;
	    }

	    final long time = timeint * unit.getToSecond();

	    new BukkitRunnable() {
		@SuppressWarnings("deprecation")
		public void run() {

		    UUID uuid = Bukkit.getOfflinePlayer(toban).getUniqueId();

		    if (BanManager.isBanned(uuid.toString()))
			BanManager.unban(uuid.toString());

		    BanManager.ban(uuid.toString(), toban, reasonbuilder.toString(), time + 1, sender.getName());

		    final String remaining = BanManager.getRemainingTime(uuid.toString());
		    Player[] players = Bukkit.getOnlinePlayers().clone();
		    for (Player all : players)
			if (all.isOnline())
			    all.sendMessage("§c-------------\n§c" + sender.getName() + " banned player " + toban
				    + "\nTime: " + remaining + "\nReason: " + reasonbuilder.toString()
				    + "\n§c-------------");
		    System.out.println("§c-------------\n§c" + sender.getName() + " banned player " + toban + "\nTime: "
			    + remaining + "\nReason: " + reasonbuilder.toString() + "\n§c-------------");

		    new BukkitRunnable() {

			public void run() {

			    if (t.isOnline())
				t.kickPlayer("§c-------------\nYou have been banned from the server. \nReason: "
					+ reasonbuilder.toString() + "\nRemaining time: " + remaining
					+ "\n§cFalsely banned? Join ts.pvphospital.eu" + "\n§c-------------");

			}
		    }.runTask(HospitalCore.inst());
		}
	    }.runTaskAsynchronously(HospitalCore.inst());

	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("unban")) {

	    if (args.length != 1)
		return false;

	    final String name = args[0];

	    new BukkitRunnable() {
		@SuppressWarnings("deprecation")
		public void run() {

		    UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

		    if (!BanManager.isBanned(uuid.toString())) {
			sender.sendMessage("§aIsn't banned.");
			return;
		    }

		    BanManager.unban(uuid.toString());

		    Player[] players = Bukkit.getOnlinePlayers().clone();
		    for (Player all : players)
			if (all.isOnline())
			    all.sendMessage("§a" + name + " has been unbanned by " + sender.getName());

		}
	    }.runTaskAsynchronously(HospitalCore.inst());
	    return true;

	}

	if (cmd.getName().equalsIgnoreCase("bancheck")) {

	    if (args.length == 2 && args[0].equalsIgnoreCase("list")) {

		final String banner = args[1];

		new BukkitRunnable() {
		    public void run() {
			BanManager.getBannedPlayersBy(sender, banner);
		    }
		}.runTaskAsynchronously(HospitalCore.inst());

		return true;

	    }

	    if (args.length != 1)
		return false;

	    final String name = args[0];

	    new BukkitRunnable() {
		public void run() {

		    if (name.equalsIgnoreCase("list")) {

			BanManager.getBannedPlayers(sender);
			return;

		    }

		    @SuppressWarnings("deprecation")
		    UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

		    if (!BanManager.isBanned(uuid.toString()))
			sender.sendMessage("§aThe player isn't banned");
		    else {

			sender.sendMessage("§c-------------");
			sender.sendMessage("§cName: " + name);
			sender.sendMessage("§cReason: " + BanManager.getReason(uuid.toString()));
			sender.sendMessage("§cTime left: " + BanManager.getRemainingTime(uuid.toString()));
			sender.sendMessage("§cBanned by: " + BanManager.getBanner(uuid.toString()));
			sender.sendMessage("§c-------------");

		    }

		}
	    }.runTaskAsynchronously(HospitalCore.inst());
	}

	return false;
    }
}
