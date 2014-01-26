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

import com.polycoding.darkwizards.gui.GUI;
import com.polycoding.darkwizards.misc.StaticMethods;
import com.polycoding.darkwizards.misc.Variables;
import com.polycoding.darkwizards.tasks.BankHandling;
import com.polycoding.darkwizards.tasks.BankTravelling;
import com.polycoding.darkwizards.tasks.Combat;
import com.polycoding.darkwizards.tasks.WizardTravelling;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

@Manifest(name = "Dark Wizard Killer by 1LD", description = "Kills them in varrock. Uses lodestone, banks, eats, & loots")
public class DarkWizardKiller extends PollingScript implements PaintListener {

	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	private ArrayList<Task> taskList = new ArrayList<Task>();

	public static Timer scriptTimer = new Timer(0);

	public static int poll = 373;

	private GUI gui;

	public static Task currentTask = null;

	/*
	 * private CombatTimer combatTimer = null; private Thread timerThread =
	 * null;
	 */// ignore

	@Override
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui = new GUI();
			}
		});
		while (!Variables.guiIsDone) {
			sleep(10);
		}
		Variables.startTime = System.currentTimeMillis();

		final Skills skills = ctx.skills;
		for (int i = 0; i < 7; i++)
			Variables.startingExperience += skills.getExperience(i);
		System.out.println("Starting exp: " + Variables.startingExperience);

		taskList.add(new BankTravelling(ctx));
		taskList.add(new BankHandling(ctx));
		taskList.add(new Combat(ctx));
		taskList.add(new WizardTravelling(ctx));
	}

	@Override
	public int poll() {
		if (poll == -1)
			getController().stop();
		for (Task task : taskList) {
			if (task.activate()) {
				currentTask = task;
				task.execute();
			}
		}
		return poll;
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

		int gainedExp = currentExp - Variables.startingExperience;

		final int hourlyExp = StaticMethods.getPerHour(gainedExp,
				Variables.startTime);

		final int hourlyProfit = StaticMethods.getPerHour(Variables.profit,
				Variables.startTime);

		final int hourlyKills = StaticMethods.getPerHour(
				(int) (gainedExp / 65.5), Variables.startTime);

		final String gainedExpString = df.format(gainedExp);
		final String hourlyExpString = df.format(hourlyExp);

		final String gainedProfitString = df.format(Variables.profit);
		final String hourlyProfitString = df.format(hourlyProfit);

		final String killsString = df.format(gainedExp / 65.5);
		final String hourlyKillsString = df.format(hourlyKills);

		// -- Fill top bar
		g.setColor(new Color(0, 0, 0, 0.80f));
		// g.fillRect(738, 49, 352, 172);
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

		// -- Status and label
		g.setColor(Color.GRAY);
		g.setFont(new Font("Kristen ITC", Font.BOLD, 11));
		g.drawString("Dark Wizard Killer by OneLuckyDuck", 25, 64);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawString("Status: " + Variables.status, 25, 188);

	}
}
