package de.gabik21.hospitalcore.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.math3.util.Pair;
import org.bukkit.scheduler.BukkitRunnable;

import de.gabik21.hospitalcore.HospitalCore;
import de.gabik21.hospitalcore.MySQL;
import de.gabik21.hospitalcore.types.Kit;
import de.gabik21.hospitalcore.types.KitConfiguration;
import de.gabik21.hospitalcore.types.LoadedData;
import de.gabik21.hospitalcore.types.PlayerData;
import de.gabik21.hospitalcore.types.Stats;

public class SavingUnit {

    public static void saveData(final PlayerData pd) {

	final String uuid = pd.getPlayer().getUniqueId().toString();
	final Stats stats = pd.getStats();

	new BukkitRunnable() {
	    public void run() {

		MySQL.executeUpdate(String.format(
			"UPDATE data SET Money = %s, Kills = %s, Deaths = %s, Wins = %s, Chests = %s WHERE UUID = '%s'",
			pd.getMoney(), stats.getKills(), stats.getDeaths(), stats.getWins(), pd.getChests(), uuid));
		MySQL.executeUpdate(String.format("DELETE FROM kitconfigs WHERE UUID = '%s'", uuid));
		MySQL.executeUpdate(String.format("DELETE FROM kits WHERE UUID = '%s'", uuid));
		PreparedStatement ps2 = null;
		try {
		    ps2 = MySQL.getConnection().prepareStatement("INSERT INTO kits (UUID, Kit, Amount) VALUES(?,?,?)");
		} catch (SQLException e2) {
		    e2.printStackTrace();
		}

		for (Entry<Kit, AtomicInteger> entry : pd.getOwnedKits().entrySet()) {
		    try {
			ps2.setString(1, uuid);
			ps2.setString(2, entry.getKey().getName());
			ps2.setInt(3, entry.getValue().get());
			ps2.addBatch();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}

		try {
		    ps2.executeBatch();
		    ps2.close();
		} catch (SQLException e2) {
		    e2.printStackTrace();
		}

		PreparedStatement ps = null;
		try {
		    ps = MySQL.getConnection().prepareStatement("INSERT INTO kitconfigs (UUID, Config) VALUES(?,?)");
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
		for (KitConfiguration kitcfg : pd.getKitConfigs()) {
		    try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(kitcfg);
			byte[] kitcfgBytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(kitcfgBytes);
			ps.setString(1, uuid);
			ps.setBinaryStream(2, bais, kitcfgBytes.length);
			ps.addBatch();
			oos.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }

		}
		try {
		    ps.executeBatch();
		    ps.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}

	    }
	}.runTaskAsynchronously(HospitalCore.inst());

    }

    public static void saveDataSync(PlayerData pd) {

	String uuid = pd.getPlayer().getUniqueId().toString();
	Stats stats = pd.getStats();
	pd.setFakekits(false);

	MySQL.executeUpdate(String.format(
		"UPDATE data SET Money = %s, Kills = %s, Deaths = %s, Wins = %s, Chests = %s WHERE UUID = '%s'",
		pd.getMoney(), stats.getKills(), stats.getDeaths(), stats.getWins(), pd.getChests(), uuid));
	MySQL.executeUpdate(String.format("DELETE FROM kitconfigs WHERE UUID = '%s'", uuid));
	MySQL.executeUpdate(String.format("DELETE FROM kits WHERE UUID = '%s'", uuid));
	PreparedStatement ps2 = null;
	try {
	    ps2 = MySQL.getConnection().prepareStatement("INSERT INTO kits (UUID, Kit, Amount) VALUES(?,?,?)");
	} catch (SQLException e2) {
	    e2.printStackTrace();
	}

	for (Entry<Kit, AtomicInteger> entry : pd.getOwnedKits().entrySet()) {
	    try {
		ps2.setString(1, uuid);
		ps2.setString(2, entry.getKey().getName());
		ps2.setInt(3, entry.getValue().get());
		ps2.addBatch();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	try {
	    ps2.executeBatch();
	    ps2.close();
	} catch (SQLException e2) {
	    e2.printStackTrace();
	}

	PreparedStatement ps = null;
	try {
	    ps = MySQL.getConnection().prepareStatement("INSERT INTO kitconfigs (UUID, Config) VALUES(?,?)");
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}
	for (KitConfiguration kitcfg : pd.getKitConfigs()) {
	    try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(kitcfg);
		oos.close();
		byte[] kitcfgBytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(kitcfgBytes);
		ps.setString(1, uuid);
		ps.setBinaryStream(2, bais, kitcfgBytes.length);
		bais.close();
		ps.addBatch();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	}
	try {
	    ps.executeBatch();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static LoadedData loadData(UUID uuid, boolean createNewData) {
	LoadedData data = new LoadedData();
	long money = -1;
	int kills = -1;
	int deaths = -1;
	int wins = -1;
	int chests = -1;
	Map<Kit, AtomicInteger> kits = new HashMap<Kit, AtomicInteger>();
	KitConfiguration[] kitconfigs = new KitConfiguration[] { new KitConfiguration(), new KitConfiguration(),
		new KitConfiguration(), new KitConfiguration(), new KitConfiguration() };

	Pair<ResultSet, PreparedStatement> p = MySQL
		.executeQuery("SELECT UUID FROM data WHERE UUID = '" + uuid.toString() + "'");
	ResultSet rs = p.getKey();
	try {
	    if (!rs.next()) {
		rs.close();
		if (createNewData) {
		    MySQL.executeUpdate("INSERT INTO data (UUID, Money, Kills, Deaths, Wins, Chests) VALUES('"
			    + uuid.toString() + "',0,0,0,0,4)");
		} else {
		    data.insertData(money, kits, kitconfigs, new Stats(kills, deaths, wins), chests);
		    return data;
		}
	    }
	    p.getValue().close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	Pair<ResultSet, PreparedStatement> p1 = MySQL
		.executeQuery("SELECT * FROM data WHERE UUID = '" + uuid.toString() + "'");
	ResultSet rs1 = p1.getKey();
	try {
	    while (rs1.next()) {
		money = Long.valueOf(rs1.getString("Money"));
		kills = rs1.getInt("Kills");
		deaths = rs1.getInt("Deaths");
		wins = rs1.getInt("Wins");
		chests = rs1.getInt("Chests");
	    }
	    p1.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	Pair<ResultSet, PreparedStatement> rs2 = MySQL
		.executeQuery("SELECT * FROM kits WHERE UUID = '" + uuid.toString() + "'");
	try {

	    Map<String, AtomicInteger> temp = new HashMap<String, AtomicInteger>();
	    while (rs2.getKey().next())
		temp.put(rs2.getKey().getString("Kit"), new AtomicInteger(rs2.getKey().getInt("Amount")));
	    for (String s : temp.keySet())
		for (Kit kit : Kit.values())
		    if (kit.getName().equals(s))
			kits.put(kit, temp.get(s));
	    rs2.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	Pair<ResultSet, PreparedStatement> rs3 = MySQL
		.executeQuery("SELECT Config FROM kitconfigs WHERE UUID = '" + uuid + "'");
	try {
	    int i = 0;
	    while (rs3.getKey().next()) {
		ByteArrayInputStream bis = (ByteArrayInputStream) rs3.getKey().getBinaryStream("Config");
		ObjectInputStream ois = new ObjectInputStream(bis);
		KitConfiguration config = (KitConfiguration) ois.readObject();
		ois.close();
		if (config != null && i < kitconfigs.length)
		    kitconfigs[i] = config;
		i++;
	    }
	    rs3.getValue().close();
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}

	data.insertData(money, kits, kitconfigs, new Stats(kills, deaths, wins), chests);
	return data;
    }

}
