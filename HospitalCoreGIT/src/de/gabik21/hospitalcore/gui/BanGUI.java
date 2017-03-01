package de.gabik21.hospitalcore.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.gabik21.api.GabikAPI;
import de.gabik21.hospitalcore.types.AbstractGUI;

public class BanGUI extends AbstractGUI {

    public BanGUI(Player player, final String cmd, boolean bol) {
	super(9, "§c§lConfirm ban", player);

	AbstractAction confirm = new AbstractAction() {
	    public void click(Player player) {
		player.performCommand(cmd);
		player.closeInventory();
	    }
	};

	AbstractAction cancel = new AbstractAction() {
	    public void click(Player player) {
		player.closeInventory();
	    }
	};

	setItem(GabikAPI.createWool("§c§lCancel!", DyeColor.RED, 1), 0, cancel);
	setItem(GabikAPI.createItem(Material.BOOK, "§c§l" + cmd), 4, null);
	setItem(GabikAPI.createWool("§a§lConfirm!", DyeColor.LIME, 1), 8, confirm);

	player.openInventory(getInventory());

    }

    public BanGUI(Player player, final String target) {
	super(27, "§c§lBanGUI", player);

	AbstractAction cheating = new AbstractAction() {
	    public void click(Player player) {
		player.closeInventory();
		new BanGUI(player, "tempban " + target + " 20 d Cheating", false);
	    }
	};

	AbstractAction foundinss = new AbstractAction() {

	    public void click(Player player) {
		player.closeInventory();

		new BanGUI(player, "tempban " + target + " 20 d Hacked Client found in SS", false);
	    }
	};

	AbstractAction autoclicker = new AbstractAction() {
	    public void click(Player player) {
		player.closeInventory();

		new BanGUI(player, "tempban " + target + " 8 d Autoclicking", false);

	    }
	};

	AbstractAction ssrefuse = new AbstractAction() {
	    public void click(Player player) {
		player.closeInventory();

		new BanGUI(player, "tempban " + target + " 3 d Refusing to SS", false);
	    }
	};

	AbstractAction illegalclientmod = new AbstractAction() {
	    public void click(Player player) {
		player.closeInventory();

		new BanGUI(player, "tempban " + target + " 3 d Illegal client modification", false);
	    }
	};

	setItem(GabikAPI.createItem(Material.SKULL_ITEM, "§c§l" + target), 4, null);
	setItem(GabikAPI.createItem(Material.IRON_SWORD, "§c§lCheating"), 9, cheating);
	setItem(GabikAPI.createItem(Material.GLASS, "§c§lHacked Client Found in SS"), 13, foundinss);
	setItem(GabikAPI.createItem(Material.GOLD_SWORD, "§c§lAutoclicking"), 17, autoclicker);
	setItem(GabikAPI.createItem(Material.BRICK_STAIRS, "§c§lRefusing to SS"), 24, ssrefuse);
	setItem(GabikAPI.createItem(Material.DISPENSER, "§c§lIllegal client modification"), 20, illegalclientmod);
	player.openInventory(getInventory());

    }
}
