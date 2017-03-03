package de.gabik21.hospitalcore.types;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import de.gabik21.api.GabikAPI;
import de.gabik21.hospitalcore.abilities.Anchor;
import de.gabik21.hospitalcore.abilities.Grappler;
import de.gabik21.hospitalcore.abilities.Hedgehog;
import de.gabik21.hospitalcore.abilities.Kangaroo;
import de.gabik21.hospitalcore.abilities.Ninja;
import de.gabik21.hospitalcore.abilities.NullKit;
import de.gabik21.hospitalcore.abilities.Phoenix;
import de.gabik21.hospitalcore.abilities.Stomper;
import de.gabik21.hospitalcore.abilities.WhiteBeam;
import de.gabik21.hospitalcore.abilities.blue.Archer;
import de.gabik21.hospitalcore.abilities.blue.Beserker;
import de.gabik21.hospitalcore.abilities.blue.BrickBeam;
import de.gabik21.hospitalcore.abilities.blue.Camel;
import de.gabik21.hospitalcore.abilities.blue.Gambler;
import de.gabik21.hospitalcore.abilities.blue.Hulk;
import de.gabik21.hospitalcore.abilities.blue.Noob;
import de.gabik21.hospitalcore.abilities.blue.Noob2;
import de.gabik21.hospitalcore.abilities.blue.Switcher;
import de.gabik21.hospitalcore.abilities.purple.Assassin;
import de.gabik21.hospitalcore.abilities.purple.CaptainAmerica;
import de.gabik21.hospitalcore.abilities.purple.Gladiator;
import de.gabik21.hospitalcore.abilities.purple.GreenBeam;
import de.gabik21.hospitalcore.abilities.purple.MarioKart;
import de.gabik21.hospitalcore.abilities.purple.NetherBeam;
import de.gabik21.hospitalcore.abilities.purple.SentryGun;
import de.gabik21.hospitalcore.abilities.purple.Snail;
import de.gabik21.hospitalcore.abilities.purple.Tank;
import de.gabik21.hospitalcore.abilities.purple.Zen;
import de.gabik21.hospitalcore.abilities.red.BlackBeam;
import de.gabik21.hospitalcore.abilities.red.BlueBeam;
import de.gabik21.hospitalcore.abilities.red.FisherMan;
import de.gabik21.hospitalcore.abilities.red.Hades;
import de.gabik21.hospitalcore.abilities.red.Monk;
import de.gabik21.hospitalcore.abilities.red.Monkey;
import de.gabik21.hospitalcore.abilities.red.Napalm;
import de.gabik21.hospitalcore.abilities.red.Phantom;
import de.gabik21.hospitalcore.abilities.red.RedBeam;
import de.gabik21.hospitalcore.abilities.red.Scout;
import de.gabik21.hospitalcore.abilities.red.Viper;

public enum Kit {
    ANCHOR(
	    "Anchor",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.ANVIL),
	    new Anchor(),
	    14D,
	    KitLevel.GOLD,
	    Arrays.asList("Take no knockback", "deal no knockback")),
    @SuppressWarnings("deprecation")
    ANTISTOMPER(
	    "Anti Stomper",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getWoolData()),
	    new NullKit(),
	    4D,
	    KitLevel.BLUE,
	    Arrays.asList("You don't take damage", "when you get stomped")),
    ARCHER(
	    "Archer",
	    false,
	    new ItemStack[] { new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 24) },
	    new ItemStack(Material.BOW),
	    new Archer(),
	    10D,
	    KitLevel.BLUE,
	    Arrays.asList("Start with a bow and 24 arrows!", "You also gain arrows by shooting at players!")),
    ASSASSIN(
	    "Assassin",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.REDSTONE),
	    new Assassin(),
	    8D,
	    KitLevel.PURPLE,
	    Arrays.asList("Teleport yourself 7 blocks", "above your enemy to", "get a surprise attack")),
    // AUTOMATIONTURRET(
    // "Automation-Turret",
    // true,
    // new ItemStack[] {},
    // new ItemStack(Material.FENCE),
    // new AutomationTurret(),
    // 15D,
    // KitLevel.GOLD,
    // Arrays.asList("Click your item to spawn", "a turret that will fire",
    // "under your control")),
    BESERKER(
	    "Beserker",
	    false,
	    new ItemStack[] {},
	    GabikAPI.createPotion(PotionType.STRENGTH, false, 1),
	    new Beserker(),
	    10D,
	    KitLevel.BLUE,
	    Arrays.asList("You get a short speed", "and strength boost on every kill")),
    BLACKBEAM(
	    "Black Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.OBSIDIAN),
	    new BlackBeam(),
	    7D,
	    KitLevel.RED,
	    Arrays.asList("Shoot black particles at your opponent", "to give him a wither effect!")),
    BLUEBEAM(
	    "Blue Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.LAPIS_BLOCK),
	    new BlueBeam(),
	    7D,
	    KitLevel.RED,
	    Arrays.asList("Shoot blue particles at your opponent", "to slow him down!")),
    BRICKBEAM(
	    "Brick Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.SMOOTH_BRICK),
	    new BrickBeam(),
	    7D,
	    KitLevel.BLUE,
	    Arrays.asList("Shoot gray particles at your opponent", "to make him weaker!")),
    CAMEL(
	    "Camel",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.SAND),
	    new Camel(),
	    3D,
	    KitLevel.BLUE,
	    Arrays.asList("Get a speed boost", "in the desert")),
    CAPTAINAMERICA(
	    "Captain America",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.EMERALD),
	    new CaptainAmerica(),
	    8D,
	    KitLevel.PURPLE,
	    Arrays.asList("Hold your shield to block", "incoming damage. After 9 hearts",
		    "of damage you get a cooldown")),
    FISHERMAN(
	    "Fisherman",
	    false,
	    new ItemStack[] { new ItemStack(Material.FISHING_ROD) },
	    new ItemStack(Material.FISHING_ROD),
	    new FisherMan(),
	    10D,
	    KitLevel.RED,
	    Arrays.asList("You can fish player", "towards yourself!")),
    GAMBLER(
	    "Gambler",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.STONE_BUTTON),
	    new Gambler(),
	    9D,
	    KitLevel.BLUE,
	    Arrays.asList("Find a button on the map", "and get a random effect", "(tip: most buttons are at trees)")),
    GRANDPA(
	    "Grandpa",
	    false,
	    new ItemStack[] { GabikAPI.createItem(Material.STICK, Enchantment.KNOCKBACK, 2) },
	    new ItemStack(Material.STICK),
	    new NullKit(),
	    1D,
	    KitLevel.BLUE,
	    Arrays.asList("Start with a stick", "with a knockback II enchantment")),
    GRAPPLER(
	    "Grappler",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.LEASH),
	    new Grappler(),
	    10D,
	    KitLevel.DARKRED,
	    Arrays.asList("Hook yourself onto terrain or entities and", "pull yourself towards them.")),
    GREENBEAM(
	    "Green Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.EMERALD_BLOCK),
	    new GreenBeam(),
	    7D,
	    KitLevel.PURPLE,
	    Arrays.asList("Shoot green particles at your opponent", "to poison him")),
    GLADIATOR(
	    "Gladiator",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.IRON_FENCE),
	    new Gladiator(),
	    12D,
	    KitLevel.PURPLE,
	    Arrays.asList("Face your opponent in a", "1v1 glass arena!")),
    HADES(
	    "Hades",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.BONE),
	    new Hades(),
	    8D,
	    KitLevel.RED,
	    Arrays.asList("Click your kit item to", "send some wolves to your enemy")),
    HEDGEHOG(
	    "Hedgehog",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.ARROW),
	    new Hedgehog(),
	    10D,
	    KitLevel.DARKRED,
	    Arrays.asList("Spread out spines that", " will keep you safe for some seconds!")),
    HULK(
	    "Hulk",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.PISTON_STICKY_BASE),
	    new Hulk(),
	    1D,
	    KitLevel.BLUE,
	    Arrays.asList("Pick up your opponents", "and throw them away")),

    KANGAROO(
	    "Kangaroo",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.FIREWORK),
	    new Kangaroo(),
	    10D,
	    KitLevel.GOLD,
	    Arrays.asList("You start with a double jump rocket")),
    MARIOKART(
	    "Mario Kart",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.MINECART),
	    new MarioKart(),
	    8D,
	    KitLevel.PURPLE,
	    Arrays.asList("Get the temporary ability", "to drive in a minecart!")),

    MONK(
	    "Monk",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.BLAZE_ROD),
	    new Monk(),
	    12D,
	    KitLevel.RED,
	    Arrays.asList("Swap your opponents item in hand", "with an item in his inventory")),
    NAPALM(
	    "Napalm",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.FIREBALL),
	    new Napalm(),
	    15D,
	    KitLevel.RED,
	    Arrays.asList("")),
    NEO(
	    "Neo",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.DIAMOND_CHESTPLATE),
	    new NullKit(),
	    10D,
	    KitLevel.RED,
	    Arrays.asList("Reflect projectiles and send", "them back to your enemy",
		    "(tip: This also works with beams)")),
    NETHERBEAM(
	    "Nether Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.NETHERRACK),
	    new NetherBeam(),
	    7D,
	    KitLevel.PURPLE,
	    Arrays.asList("Shoot red particles at your opponent", "to instant damage them")),
    MONKEY(
	    "Monkey",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.VINE),
	    new Monkey(),
	    14D,
	    KitLevel.RED,
	    Arrays.asList("Sneak at a wall to", "get a boost into the", "direction you're looking")),
    NINJA(
	    "Ninja",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.INK_SACK),
	    new Ninja(),
	    10D,
	    KitLevel.DARKRED,
	    Arrays.asList("Crouching teleports you to the", "player you hit in the past 15 seconds!")),
    NOOB(
	    "Noob I",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.STONE_SWORD),
	    new Noob(),
	    5D,
	    KitLevel.BLUE,
	    Arrays.asList("You can't drop your sword", "using this ability")),
    NOOB2(
	    "Noob II",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.MUSHROOM_SOUP),
	    new Noob2(),
	    5D,
	    KitLevel.BLUE,
	    Arrays.asList("You can't drop full mushroom soups", "using this ability")),
    PHANTOM(
	    "Phantom",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.FEATHER),
	    new Phantom(),
	    9D,
	    KitLevel.RED,
	    Arrays.asList("Get temporarily the ability to fly")),
    PHOENIX(
	    "Phoenix",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.GOLDEN_CARROT),
	    new Phoenix(),
	    25D,
	    KitLevel.PLATIN,
	    Arrays.asList("Well... you're basicly god", "You have 2 lifes...")),
    REDBEAM(
	    "Red Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.REDSTONE_BLOCK),
	    new RedBeam(),
	    7D,
	    KitLevel.RED,
	    Arrays.asList("Shoot red particles at your opponent", "to set him on fire")),
    SCOUT(
	    "Scout",
	    false,
	    new ItemStack[] { GabikAPI.createPotion(PotionType.SPEED, true, 2) },
	    GabikAPI.createPotion(PotionType.SPEED, true, 2),
	    new Scout(),
	    4D,
	    KitLevel.RED,
	    Arrays.asList("You get a speed potion at the beginning", "Every player you kill drops a new potion")),
    SENTRYGUN(
	    "Sentrygun",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.ARROW),
	    new SentryGun(),
	    7D,
	    KitLevel.PURPLE,
	    Arrays.asList("You can activate your sentrygun", "by clicking your kit item")),
    SNAIL(
	    "Snail",
	    false,
	    new ItemStack[] {},
	    new Potion(PotionType.SLOWNESS).toItemStack(1),
	    new Snail(),
	    6D,
	    KitLevel.PURPLE,
	    Arrays.asList("Your opponent gets randomly", "slowness whilst fighting him")),

    STOMPER(
	    "Stomper",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.DIAMOND_BOOTS),
	    new Stomper(),
	    12D,
	    KitLevel.DARKRED,
	    Arrays.asList("Jump down on players from high above", "to massive damage to them!")),
    SWITCHER(
	    "Switcher",
	    false,
	    new ItemStack[] { new ItemStack(Material.SNOW_BALL, 16) },
	    new ItemStack(Material.SNOW_BALL),
	    new Switcher(),
	    2D,
	    KitLevel.BLUE,
	    Arrays.asList("Switch places with other players", "by throwing a snowball at them!",
		    "You also gain new balls by shooting at players")),
    TANK(
	    "Tank",
	    false,
	    new ItemStack[] {},
	    new ItemStack(Material.TNT),
	    new Tank(),
	    3D,
	    KitLevel.PURPLE,
	    Arrays.asList("Killing a player sets off an explosion,", "you're immune to explosion damage!")),

    VIPER(
	    "Viper",
	    false,
	    new ItemStack[] {},
	    GabikAPI.createPotion(PotionType.POISON, false, 1),
	    new Viper(),
	    6D,
	    KitLevel.RED,
	    Arrays.asList("Your opponent gets randomly", " poison whilst fighting him!")),

    WHITEBEAM(
	    "White Beam",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.QUARTZ_BLOCK),
	    new WhiteBeam(),
	    7D,
	    KitLevel.DARKRED,
	    Arrays.asList("Shoot white particles at your opponent", "to blind him!")),
    ZEN(
	    "Zen",
	    true,
	    new ItemStack[] {},
	    new ItemStack(Material.MAGMA_CREAM),
	    new Zen(),
	    9D,
	    KitLevel.PURPLE,
	    Arrays.asList("Teleport yourself to the nearest", "player using your kit item!"));

    private String name;
    private boolean clickItem;
    private ItemStack[] items;
    private ItemStack invitem;
    private Ability ability;
    private double points;
    private KitLevel level;
    private List<String> description;

    private Kit(String name, boolean clickItem, ItemStack[] items, ItemStack invitem, Ability ability, double points,
	    KitLevel level, List<String> description) {

	this.name = name;
	this.clickItem = clickItem;
	this.items = items;
	this.invitem = invitem;
	this.ability = ability;
	this.points = points;
	this.level = level;
	this.description = description;

    }

    public List<String> getDescription() {
	return this.description;
    }

    public KitLevel getLevel() {
	return level;
    }

    public boolean hasClickItem() {
	return clickItem;
    }

    public double getPoints() {
	return points;
    }

    public String getName() {
	return this.name;
    }

    public ItemStack[] getItems() {
	return this.items;
    }

    public ItemStack getInvItem() {
	return this.invitem;
    }

    public Ability getAbility() {
	return ability;
    }

    public long getCooldown() {
	return getAbility().getCooldown();
    }

}
