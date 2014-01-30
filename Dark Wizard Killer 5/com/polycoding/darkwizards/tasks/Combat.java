package com.polycoding.darkwizards.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.lang.GroundItemQuery;
import org.powerbot.script.util.Condition;
import org.powerbot.script.util.GeItem;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Interactive;
import org.powerbot.script.wrappers.Npc;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class Combat extends Task {

	private final DarkWizardKiller dwk;

	public Combat(DarkWizardKiller arg0) {
		super(arg0);
		dwk = arg0;
	}

	@Override
	public boolean activate() {
		boolean food = dwk.useFood ? inventoryContains(dwk.foodId) : true;
		return DarkWizardKiller.CIRCLE.contains(ctx.players.local())
				&& !ctx.players.local().isInCombat() && food;
	}

	@Override
	public void execute() {
		if (dwk.useFood && ctx.players.local().getHealthPercent() <= 51) {
			ctx.backpack.select().id(dwk.foodId).shuffle().poll()
					.interact("Eat");
			sleep(600); // 1 tick to eat before retrying
			return;
		}
		if (!isMomentumActive()) {
			log("Activating momentum");
			ctx.keyboard.send("1");
		}
		Npc wizard = ctx.npcs.select().id(DarkWizardKiller.WIZARD_IDS)
				.select(Interactive.areOnScreen()).nearest().limit(3).shuffle()
				.poll();
		if (!wizard.isOnScreen()) {
			ctx.movement.stepTowards(wizard);
			ctx.camera.turnTo(wizard);
		} else {
			log("Attacking @ " + wizard.getLocation().toString());
			if (!wizard.isInCombat() && wizard.interact("Attack")) {
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return !ctx.players.local().isInMotion();
					}
				}, 150, 20);
			}
		}
		if (checkForLoot())
			return;
	}

	private boolean checkForLoot() {
		GroundItemQuery<GroundItem> itemQuery = ctx.groundItems.select()
				.id(DarkWizardKiller.LOOTABLE_ITEMS).nearest();
		if (itemQuery.size() > 0) {
			log(itemQuery.size() + " grounditems!");
			final GroundItem item = itemQuery.shuffle().poll();
			log("Taking " + item.getName());
			if (item.interact("Take")) {
	
				final Timer timer = new Timer(2000);
				while (item.isValid()) {
					if (ctx.players.local().isInMotion())
						timer.reset();
					if (!timer.isRunning() || !item.isValid())
						break;
				}
				dwk.profit += GeItem.getPrice(item.getId());
			}
			return true;
		}
		return false;
	}
}
