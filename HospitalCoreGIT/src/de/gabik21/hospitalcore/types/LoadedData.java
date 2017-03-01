package de.gabik21.hospitalcore.types;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadedData {

    private Map<Kit, AtomicInteger> ownedkits = new HashMap<Kit, AtomicInteger>();
    private long money = -1;
    private KitConfiguration[] kitconfigs;
    private Stats stats;
    private int chests;

    public void insertData(long money, Map<Kit, AtomicInteger> ownedkits, KitConfiguration[] kitconfigs, Stats stats,
	    int chests) {
	this.money = money;
	this.ownedkits = ownedkits;
	this.kitconfigs = kitconfigs;
	this.stats = stats;
	this.chests = chests;
    }

    public long getMoney() {
	return money;
    }

    public Map<Kit, AtomicInteger> getOwnedkits() {
	return ownedkits;
    }

    public KitConfiguration[] getKitconfigs() {
	return kitconfigs;
    }

    public Stats getStats() {
	return stats;
    }

    public int getChests() {
	return chests;
    }

}
