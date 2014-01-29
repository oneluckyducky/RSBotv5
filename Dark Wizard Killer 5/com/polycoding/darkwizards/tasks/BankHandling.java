package com.polycoding.darkwizards.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
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
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.bank.isOpen()
								|| !ctx.players.local().isInMotion();
					}
				}, 150, 15);
			}
		} else {
			if (!ctx.backpack.select().isEmpty()) {
				log("Depositing everything");
				ctx.bank.depositInventory();
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.backpack.isEmpty();
					}
				}, 150, 20);
			}
			if (ctx.backpack.select().isEmpty()) {
				log("Bag is empty, time to withdraw food!");
				final Item food = ctx.bank.select().id(dwk.foodId).poll();
				if (ctx.bank.select().id(food.getId()).isEmpty())
					stopScript("Out of food!", true);
				if (ctx.bank.withdraw(food.getId(), dwk.foodAmount)) {
					log("Withdrawing " + food.getName());
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return !ctx.backpack.select().id(food.getId())
									.isEmpty();
						}
					}, 150, 20);
					if (ctx.backpack.size() > 0) {
						log("closing bank");
						ctx.bank.close();
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								return !ctx.bank.isOpen();
							}
						}, 150, 15);
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
