package com.polycoding.darkwizards.tasks;

import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class BankHandling extends Task {

	private final DarkWizardKiller dwk;

	public BankHandling(DarkWizardKiller arg0) {
		super(arg0);
		dwk = arg0;
	}

	@Override
	public void execute() {
		log("Banking start");
		if (!ctx.bank.isOpen()) {
			log("Opening booth");
			if (open()) {
				final Timer openTimer = new Timer(1000);
				while (openTimer.isRunning()) {
					sleep(100);
					if (!ctx.bank.isOpen() || ctx.players.local().isInMotion())
						openTimer.reset();
				}
			}
		} else {
			if (!ctx.backpack.select().isEmpty()) {
				log("Depositing everything");
				ctx.bank.depositInventory();
				final Timer depositTimer = new Timer(1000);
				while (depositTimer.isRunning()) {
					sleep(100);
					if (!ctx.backpack.select().isEmpty())
						depositTimer.reset();
				}
			}
			if (ctx.backpack.select().isEmpty()) {
				log("Bag is empty, time to withdraw food!");
				Item food = ctx.bank.select().id(dwk.foodId).poll();
				if (ctx.bank.select().id(food.getId()).isEmpty())
					stopScript("Out of food!", true);
				if (ctx.bank.withdraw(food.getId(), dwk.foodAmount)) {
					log("Withdrawing " + food.getName());
					final Timer withdrawTimer = new Timer(1000);
					while (withdrawTimer.isRunning()) {
						sleep(100);
						if (ctx.backpack.select().id(food.getId()).isEmpty())
							withdrawTimer.reset();
					}
					if (ctx.backpack.size() > 0) {
						log("closing bank");
						ctx.bank.close();
						final Timer timer = new Timer(1000);
						while (timer.isRunning()) {
							sleep(100);
							if (ctx.bank.isOpen())
								timer.reset();
						}
					}
				}
			}
		}
	}

	@Override
	public boolean activate() {
		Tile localTile = ctx.players.local().getLocation();
		return dwk.useFood && ctx.backpack.select().id(dwk.foodId).isEmpty()
				&& localTile.getY() >= 3424 && localTile.getPlane() == 0;
	}

	public boolean open() {
		GameObject booth = ctx.objects.select().id(782, 20980).nearest()
				.limit(3).shuffle().poll();
		return booth.interact("Bank");
	}

}
