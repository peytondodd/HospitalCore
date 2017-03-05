package de.gabik21.hospitalcore.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.Listeners;
import de.gabik21.hospitalcore.MySQL;
import de.gabik21.hospitalcore.types.PlayerData;

public class NickCommand implements CommandExecutor {

    private HospitalCore main;

    public NickCommand(HospitalCore main) {

	this.main = main;
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, final String[] args) {

	if (!(sender instanceof Player))
	    return true;

	final Player p = (Player) sender;
	PlayerData pd = HospitalCore.getData(p);

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("off")) {

		pd.setNick(null);
		DisguiseAPI.disguiseEntity(p, new MobDisguise(DisguiseType.ZOMBIE));
		DisguiseAPI.getDisguise(p).removeDisguise();
		p.setDisplayName(p.getName());
		p.setPlayerListName(p.getName());
		Listeners.setNick(p, PermissionsEx.getUser(p).getPrefix());

		return true;
	    }

	    if (args[0].equalsIgnoreCase("random") && (p.hasPermission("YouTuber") || p.hasPermission("Moderator"))) {
		new BukkitRunnable() {
		    public void run() {
			String name = "";
			while (true) {
			    try {
				Pair<ResultSet, PreparedStatement> p = MySQL
					.executeQuery("SELECT * FROM nicknames ORDER BY RAND() LIMIT 1");
				ResultSet rs = p.getKey();
				if (rs.next())
				    name = rs.getString("Nick");
				p.getValue().close();
			    } catch (SQLException e) {
				e.printStackTrace();
			    }
			    for (PlayerData data : PlayerData.map.values())
				if (data.getNick().equalsIgnoreCase(name))
				    continue;
			    break;
			}
			PlayerDisguise nick = new PlayerDisguise(name);
			nick.setKeepDisguiseOnPlayerDeath(true);
			List<String> playersToViewDisguise = new ArrayList<String>();

			Player[] online = Bukkit.getOnlinePlayers().clone();

			for (Player pl : online) {

			    if (pl.hasPermission("Supporter") || pl.hasPermission("Moderator")
				    || pl.hasPermission("YouTuber") || pl.hasPermission("Owner"))
				continue;
			    playersToViewDisguise.add(pl.getName());

			}
			final String finalname = name;
			new BukkitRunnable() {
			    public void run() {
				DisguiseAPI.disguiseToPlayers(p, nick, playersToViewDisguise);
				p.setDisplayName(finalname);
				p.setPlayerListName(p.getDisplayName());
			    }
			}.runTask(main);
			Listeners.setNick(p, PermissionsEx.getUser(args[0]).getPrefix());

			p.sendMessage("§aNow nicked as " + name);

		    }
		}.runTaskAsynchronously(main);
		return true;
	    }

	    if (p.hasPermission("Owner")) {

		if (args[0].length() > 16) {

		    p.sendMessage("§cLOL thats too long");
		    return true;
		}

		pd.setNick(args[0]);

		new BukkitRunnable() {
		    public void run() {
			PlayerDisguise nick = new PlayerDisguise(args[0]);
			nick.setKeepDisguiseOnPlayerDeath(true);
			List<String> playersToViewDisguise = new ArrayList<String>();

			Player[] online = Bukkit.getOnlinePlayers().clone();

			for (Player pl : online) {

			    if (pl.hasPermission("Supporter") || pl.hasPermission("Moderator")
				    || pl.hasPermission("YouTuber") || pl.hasPermission("Owner"))
				continue;
			    playersToViewDisguise.add(pl.getName());

			}
			new BukkitRunnable() {
			    public void run() {
				DisguiseAPI.disguiseToPlayers(p, nick, playersToViewDisguise);
			    }
			}.runTask(main);
			Listeners.setNick(p, PermissionsEx.getUser(args[0]).getPrefix());

			p.sendMessage("§aNow nicked.");

		    }
		}.runTaskAsynchronously(main);

		p.setDisplayName(args[0]);
		p.setPlayerListName(p.getDisplayName());

		return true;

	    } else {

		p.sendMessage(
			main.getConfig().getString("prefix") + main.getConfig().getString("messages.nopermission"));
		return true;
	    }

	}

	if (args.length == 2) {

	    if (args[0].equalsIgnoreCase("add")) {
		new BukkitRunnable() {
		    @Override
		    public void run() {
			MySQL.executeUpdate("INSERT INTO nicknames (Nick) VALUES('" + args[1] + "')");
			sender.sendMessage("Added nickname to database: " + args[1]);
		    }
		}.runTaskAsynchronously(main);
		return true;
	    } else if (args[0].equalsIgnoreCase("remove")) {
		new BukkitRunnable() {
		    @Override
		    public void run() {
			int i = MySQL.executeUpdate("DELETE FROM nicknames WHERE Nick = '" + args[0] + "'");
			sender.sendMessage("Removed " + i + " rows");
		    }
		}.runTaskAsynchronously(main);
		return true;
	    }
	}

	return false;
    }

}
