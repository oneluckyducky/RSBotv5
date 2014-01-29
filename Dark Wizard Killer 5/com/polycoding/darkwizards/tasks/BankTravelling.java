package com.polycoding.darkwizards.tasks;

import java.util.EnumSet;
import java.util.concurrent.Callable;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Path.TraversalOption;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class BankTravelling extends Task {

	private final Tile[] tilesToBank = new Tile[] { new Tile(3211, 3378, 0),
			new Tile(3210, 3398, 0), new Tile(3199, 3415, 0),
			new Tile(3190, 3429, 0) };

	private final DarkWizardKiller dwk;

	public BankTravelling(DarkWizardKiller arg0) {
		super(arg0);
		dwk = arg0;
	}

	@Override
	public void execute() {
		log("Gotta bank!");
		for (Tile tile : tilesToBank) {
			log("Walking to: " + tile.toString());
			ctx.movement.findPath(tile).traverse(
					EnumSet.of(TraversalOption.HANDLE_RUN));
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return !ctx.players.local().isInMotion();
				}
			});
		}
	}

	@Override
	public boolean activate() {
		return dwk.useFood && ctx.backpack.select().id(dwk.foodId).isEmpty()
				&& DarkWizardKiller.CIRCLE.contains(ctx.players.local());
	}

}
