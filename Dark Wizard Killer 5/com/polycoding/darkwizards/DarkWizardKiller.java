package com.polycoding.darkwizards;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.methods.Skills;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.gui.GUI;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

@Manifest(name = "Dark Wizard Killer by 1LD", description = "Kills them in varrock. Uses lodestone, banks, eats, & loots")
public class DarkWizardKiller extends PollingScript implements PaintListener {

	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public ArrayList<Task> taskList = new ArrayList<Task>();

	public Timer scriptTimer = new Timer(0);

	private GUI gui = null;

	public static Task currentTask = null;

	public int startingExperience = 0, profit = 0, foodId = 0, foodAmount = 0;

	public long startTime = 0;

	public boolean guiIsDone = false, useFood = false;

	public String status = "";

	public static final Area CIRCLE = new Area(new Tile(3212, 3378, 0),
			new Tile(3219, 3361, 0), new Tile(3236, 3362, 0), new Tile(3235,
					3376, 0));

	public static final int[] WIZARD_IDS = { 8871, 8872, 8873, 8874 };

	public static final int[] LOOTABLE_ITEMS = { 556, 555, 554, 557, 561, 565,
			562, 559, 563, 564, 558, 8015, 24154 };

	public static final String[] ITEM_NAMES = { "Earth rune", "Air rune",
			"Water rune", "Fire rune", "Mind rune", "Body rune", "Chaos rune",
			"Nature rune", "Law rune", "Cosmic rune", "Blood rune",
			"Staff of air", "Water talisman", "Fire Talisman",
			"Earth talisman", "Staf of water" };

	@Override
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui = new GUI(DarkWizardKiller.this);
			}
		});
		while (!guiIsDone) {
			sleep(10);
		}
		startTime = System.currentTimeMillis();

		final Skills skills = ctx.skills;
		for (int i = 0; i < 7; i++)
			startingExperience += skills.getExperience(i);
	}

	@Override
	public int poll() {
		if (taskList.isEmpty() && guiIsDone)
			getController().stop();
		for (Task task : taskList) {
			if (task.activate()) {
				currentTask = task;
				task.execute();
			}
		}
		return Random.nextInt(400, 750);
	}

	@Override
	public void repaint(Graphics g1) {
		if (ctx.game.getClientState() != 11)
			return;

		final Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(antialiasing);

		final NumberFormat df = DecimalFormat.getInstance();

		int currentExp = 0;
		Skills skills = ctx.skills;
		for (int i = 0; i < 7; i++)
			currentExp += skills.getExperience(i);

		int gainedExp = currentExp - startingExperience;

		final int hourlyExp = Timer.getPerHour(gainedExp, startTime);

		final int hourlyProfit = Timer.getPerHour(profit, startTime);

		final int hourlyKills = Timer.getPerHour((int) (gainedExp / 65.5),
				startTime);

		final String gainedExpString = df.format(gainedExp);
		final String hourlyExpString = df.format(hourlyExp);

		final String gainedProfitString = df.format(profit);
		final String hourlyProfitString = df.format(hourlyProfit);

		final String killsString = df.format((int) gainedExp / 65.5);
		final String hourlyKillsString = df.format(hourlyKills);

		g.setColor(new Color(0, 0, 0, 0.80f));
		g.fillRect(0, 40, 352, 172);

		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("Run Time: " + scriptTimer.toElapsedString(), 25, 87);
		g.drawString(String.format("Experience Gained (hr): %s   (%s)",
				gainedExpString, hourlyExpString), 25, 108);
		g.drawString(String.format("Profit Gained (hr): %s   (%s)",
				gainedProfitString, hourlyProfitString), 25, 129);
		g.drawString(String.format("Kills (hr): %s   (%s)", killsString,
				hourlyKillsString), 25, 151);

		g.setColor(Color.GRAY);
		g.setFont(new Font("Kristen ITC", Font.BOLD, 11));
		g.drawString("Dark Wizard Killer by OneLuckyDuck", 25, 64);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawString("Status: " + status, 25, 188);
	}
}
