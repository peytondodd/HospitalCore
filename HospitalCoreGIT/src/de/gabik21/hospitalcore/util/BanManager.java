package de.gabik21.hospitalcore.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.gabik21.hospitalcore.MySQL;

public class BanManager {

    public static void ban(String uuid, String playername, String reason, long seconds, String banner) {

	long end = 0L;

	if (seconds == -1)
	    end = -1;
	else
	    end = System.currentTimeMillis() + (seconds * 1000);

	String raw = "INSERT INTO bans (Name, UUID, End, Reason, Banner) VALUES('%s','%s','%s','%s','%s')";
	String qry = String.format(raw, playername, uuid, String.valueOf(end), reason, banner);

	MySQL.executeUpdate(qry);

    }

    public static void unban(String uuid) {

	try {

	    PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM bans WHERE UUID = ?");
	    ps.setString(1, uuid);

	    ps.executeUpdate();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static boolean isBanned(String uuid) {

	if (getEnd(uuid) == null)
	    return false;

	if (System.currentTimeMillis() > getEnd(uuid) && getEnd(uuid) > -1) {

	    unban(uuid);
	    return false;
	}

	return true;

    }

    public static String getReason(String uuid) {

	Pair<ResultSet, PreparedStatement> p = MySQL
		.executeQuery("SELECT Reason FROM bans WHERE UUID = '" + uuid + "'");
	ResultSet rs = p.getKey();
	try {
	    while (rs.next())
		return rs.getString("Reason");
	    p.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;

    }

    public static String getBanner(String uuid) {

	Pair<ResultSet, PreparedStatement> p = MySQL
		.executeQuery("SELECT Banner FROM bans WHERE UUID = '" + uuid + "'");
	ResultSet rs = p.getKey();
	try {
	    while (rs.next())
		return rs.getString("Banner");
	    p.getValue().close();
	} catch (SQLException e) {

	    e.printStackTrace();

	}
	return null;

    }

    public static Long getEnd(String uuid) {

	Pair<ResultSet, PreparedStatement> p = MySQL.executeQuery("SELECT End FROM bans WHERE UUID = '" + uuid + "'");
	ResultSet rs = p.getKey();
	try {
	    while (rs.next())
		return Long.valueOf(rs.getLong("End"));
	    p.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return null;

    }

    public static List<String> getBannedPlayersBy(CommandSender sender, String banner) {

	List<String> temp = new ArrayList<String>();

	Pair<ResultSet, PreparedStatement> p = MySQL
		.executeQuery("SELECT Name FROM bans WHERE Banner = '" + banner + "'");
	ResultSet rs = p.getKey();
	try {
	    while (rs.next())
		temp.add(rs.getString("Name"));
	    p.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	Iterator<String> litr = temp.iterator();

	while (litr.hasNext()) {
	    String s = litr.next();
	    @SuppressWarnings("deprecation")
	    OfflinePlayer player = Bukkit.getOfflinePlayer(s);
	    if (player != null && !isBanned(player.getUniqueId().toString())) {
		litr.remove();
	    } else {
		sender.sendMessage("§e" + s);
	    }
	}

	sender.sendMessage("§e" + temp.size() + " players banned in total");

	return temp;
    }

    public static List<String> getBannedPlayers(CommandSender sender) {

	List<String> temp = new ArrayList<String>();

	Pair<ResultSet, PreparedStatement> p = MySQL.executeQuery("SELECT Name FROM bans");
	ResultSet rs = p.getKey();

	try {
	    while (rs.next())
		temp.add(rs.getString("Name"));
	    p.getValue().close();

	} catch (SQLException e) {

	    e.printStackTrace();

	}

	Iterator<String> litr = temp.iterator();

	while (litr.hasNext()) {
	    String s = litr.next();
	    @SuppressWarnings("deprecation")
	    OfflinePlayer player = Bukkit.getOfflinePlayer(s);
	    if (player != null && !isBanned(player.getUniqueId().toString())) {
		litr.remove();
	    } else {
		sender.sendMessage("§e" + s);
	    }
	}

	sender.sendMessage("§e" + temp.size() + " players banned in total");

	return temp;

    }

    public static String getRemainingTime(String uuid) {

	if (getEnd(uuid) == -1)
	    return "§cPermanent";

	long time = getEnd(uuid) - System.currentTimeMillis();

	long seconds = (time / 1000) % 60;
	long minutes = ((time / (1000 * 60)) % 60);
	long hours = ((time / (1000 * 60 * 60)) % 24);
	long days = ((time / (1000 * 60 * 60 * 24)));

	String returnstring = "§c";

	if (days > 0)
	    returnstring = returnstring + days + " day(s) ";

	if (hours > 0)
	    returnstring = returnstring + hours + " hour(s) ";

	if (minutes > 0)
	    returnstring = returnstring + minutes + " minute(s) ";

	if (seconds > 0)
	    returnstring = returnstring + seconds + " second(s)";

	return returnstring;

    }

}
