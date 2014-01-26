package com.polycoding.darkwizards.tasks;

import org.powerbot.script.lang.GroundItemQuery;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.GeItem;
import org.powerbot.script.wrappers.GroundItem;
import org.powerbot.script.wrappers.Interactive;
import org.powerbot.script.wrappers.Npc;

import com.polycoding.darkwizards.misc.Const;
import com.polycoding.darkwizards.misc.Variables;
import com.polycoding.darkwizards.util.Timer;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class Combat extends Task {

	public Combat(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public boolean activate() {
		boolean food = Variables.useFood ? inventoryContains(Variables.foodId)
				: true;
		return circle.contains(ctx.players.local())
				&& !ctx.players.local().isInCombat() && food;
	}

	@Override
	public void execute() {
		if (Variables.useFood && ctx.players.local().getHealthPercent() <= 51) {
			ctx.backpack.select().id(Variables.foodId).shuffle().poll()
					.interact("Eat");
			sleep(600); // 1 tick to eat before retrying
			return;
		}
		if (!isMomentumActive()) {
			log("Activating momentum");
			ctx.keyboard.send("1");
		}
		Npc wizard = ctx.npcs.select().id(Const.WIZARD_IDS)
				.select(Interactive.areOnScreen()).nearest().limit(2).shuffle()
				.poll();
		if (!wizard.isOnScreen()) {
			ctx.movement.stepTowards(wizard);
			ctx.camera.turnTo(wizard);
		} else {
			log("Attacking @ " + wizard.getLocation().toString());
			if (!wizard.isInCombat() && wizard.interact("Attack")) {
				final Timer timer = new Timer(2400);
				while (timer.isRunning()) {
					sleep(300);
					if (ctx.players.local().isInMotion())
						timer.reset();
				}
			}
		}
		if (checkForLoot())
			return;
	}

	private boolean isMomentumActive() {
		return ctx.settings.get(682) == 7077923;
	}

	private boolean checkForLoot() {
		GroundItemQuery<GroundItem> itemQuery = ctx.groundItems.select()
				.id(Const.LOOTABLE_ITEMS).nearest();
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
				Variables.profit += GeItem.getPrice(item.getId());
			}
			return true;
		}
		return false;
	}
}
