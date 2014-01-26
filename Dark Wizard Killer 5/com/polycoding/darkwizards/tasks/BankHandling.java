package com.polycoding.darkwizards.tasks;

import org.powerbot.script.methods.Bank;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.misc.Variables;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class BankHandling extends Task {

	private Bank bank = null; // tired of typing ctx, haha

	public BankHandling(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public void execute() {
		bank = ctx.bank;
		log("Banking start");
		if (!bank.isOpen()) {
			log("Opening booth");
			if (open()) {
				final Timer openTimer = new Timer(1000);
				while (openTimer.isRunning()) {
					sleep(100);
					if (!bank.isOpen() || ctx.players.local().isInMotion())
						openTimer.reset();
				}
			}
		} else {
			if (!ctx.backpack.select().isEmpty()) {
				log("Depositing everything");
				bank.depositInventory();
				final Timer depositTimer = new Timer(1000);
				while (depositTimer.isRunning()) {
					sleep(100);
					if (!ctx.backpack.select().isEmpty())
						depositTimer.reset();
				}
			}
			if (ctx.backpack.select().isEmpty()) {
				log("Bag is empty, time to withdraw food!");
				Item food = bank.select().id(Variables.foodId).poll();
				if (bank.select().id(food.getId()).isEmpty())
					stopScript("Out of food!", true);
				if (bank.withdraw(food.getId(), Variables.foodAmount)) {
					log("Withdrawing " + food.getName());
					final Timer withdrawTimer = new Timer(1000);
					while (withdrawTimer.isRunning()) {
						sleep(100);
						if (ctx.backpack.select().id(food.getId()).isEmpty())
							withdrawTimer.reset();
					}
					if (ctx.backpack.size() > 0) {
						log("closing bank");
						bank.close();
						final Timer timer = new Timer(1000);
						while (timer.isRunning()) {
							sleep(100);
							if (bank.isOpen())
								timer.reset();
						}
						lodestoneToVarrock();
					}
				}
			}
		}
	}

	@Override
	public boolean activate() {
		Tile localTile = ctx.players.local().getLocation();
		return Variables.useFood
				&& ctx.backpack.select().id(Variables.foodId).isEmpty()
				&& localTile.getY() >= 3424 && localTile.getPlane() == 0;
	}

	public boolean open() {
		GameObject booth = ctx.objects.select().id(782, 20980).nearest()
				.limit(3).shuffle().poll();
		return booth.interact("Bank");
	}

}
