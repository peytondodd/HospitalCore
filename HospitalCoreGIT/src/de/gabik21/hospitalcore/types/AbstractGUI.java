package de.gabik21.hospitalcore.types;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gabik21.hospitalcore.HospitalCore;

public abstract class AbstractGUI {

    protected Inventory inventory;
    protected Map<Integer, AbstractAction> actions = new HashMap<Integer, AbstractAction>();

    public AbstractGUI(int slots, String title, Player player) {

	this.inventory = Bukkit.createInventory(null, slots, title);
	HospitalCore.getData(player).setCurrentGui(this);

    }

    public Inventory getInventory() {

	return this.inventory;

    }

    public void setItem(ItemStack item, int slot, AbstractAction action) {
	inventory.setItem(slot, item);
	actions.remove(slot);
	actions.put(slot, action);
    }

    public void clear() {
	inventory.clear();
	actions.clear();
    }

    public AbstractAction getAction(int slot) {
	return this.actions.get(slot);
    }

    public interface AbstractAction {
	void click(Player player);
    }

}
