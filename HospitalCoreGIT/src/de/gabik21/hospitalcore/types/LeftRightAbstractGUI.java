package de.gabik21.hospitalcore.types;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class LeftRightAbstractGUI extends AbstractGUI {

    private Map<Integer, AbstractAction> rightactions = new HashMap<Integer, AbstractAction>();

    public LeftRightAbstractGUI(int slots, String title, Player player) {
	super(slots, title, player);
    }

    public void setItem(ItemStack item, int slot, AbstractAction action, AbstractAction right) {

	inventory.setItem(slot, item);
	actions.remove(slot);
	actions.put(slot, action);
	this.rightactions.put(slot, right);

    }

    public AbstractAction getRightAction(int slot) {

	return this.rightactions.get(slot);

    }

}
