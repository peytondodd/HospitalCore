package de.gabik21.hospitalcore.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.gabik21.api.GabikAPI;
import de.gabik21.hospitalcore.types.AbstractGUI;

public class MuteGUI extends AbstractGUI {

    public MuteGUI(Player player, String target) {
	super(27, "§c§lMuteGUI", player);

	AbstractAction toxic = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 5 m Toxic chat behaviour");
	    }
	};

	AbstractAction badsportsmanship = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 5 m Bad sportsmanship");
	    }
	};

	AbstractAction spam = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 5 m Spam");
	    }
	};

	AbstractAction advertising = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 30 m Advertising");
	    }
	};
	AbstractAction homophobicslurs = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 10 m Homophobic slurs");
	    }
	};

	AbstractAction suicide = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();
		player.performCommand("tempmute " + target + " 20 m Encouraging suicide");
	    }
	};

	setItem(GabikAPI.createItem(Material.SKULL_ITEM, "§c§l" + target), 4, null);
	setItem(GabikAPI.createItem(Material.BLAZE_POWDER, "§c§lToxic chat behaviour"), 9, toxic);
	setItem(GabikAPI.createItem(Material.POISONOUS_POTATO, "§c§lBad sportsmanship"), 13, badsportsmanship);
	setItem(GabikAPI.createItem(Material.COMMAND_MINECART, "§c§lSpam"), 17, spam);
	setItem(GabikAPI.createItem(Material.EXPLOSIVE_MINECART, "§c§lAdvertising"), 20, advertising);
	setItem(GabikAPI.createItem(Material.CARROT, "§c§lHomophobic slurs"), 24, homophobicslurs);
	setItem(GabikAPI.createItem(Material.DIAMOND_SWORD, "§c§lSuicidal encouragement"), 26, suicide);

	player.openInventory(getInventory());

    }
}
