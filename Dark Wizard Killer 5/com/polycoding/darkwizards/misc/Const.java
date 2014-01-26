package com.polycoding.darkwizards.misc;

import java.awt.Image;

public class Const {

	public final static Image BAR = StaticMethods
			.getImage("http://puu.sh/1p9xg.png");

	public static final int[] WIZARD_IDS = { 8871, 8872, 8873, 8874 };

	public static final int[] LOOTABLE_ITEMS = { 556, 555, 554, 557, 561, 565,
			562, 559, 563, 564, 558, 8015, 24154 };

	public static final String[] ITEM_NAMES = { "Earth rune", "Air rune",
			"Water rune", "Fire rune", "Mind rune", "Body rune", "Chaos rune",
			"Nature rune", "Law rune", "Cosmic rune", "Blood rune",
			"Staff of air", "Water talisman", "Fire Talisman",
			"Earth talisman", "Staf of water" };

	/*
	 * public static final Filter<NPC> WIZRD_FILTER = new Filter<NPC>() {
	 * 
	 * @Override public boolean accept(NPC n) { return
	 * n.getName().equalsIgnoreCase("dark wizard") && n.getLocation().canReach()
	 * && n.getAnimation() != 836 && (n.getInteracting() == null ||
	 * n.getInteracting() .equals(Players.getLocal())); }
	 * 
	 * };
	 */
}
