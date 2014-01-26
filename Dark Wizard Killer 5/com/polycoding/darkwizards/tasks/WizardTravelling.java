package com.polycoding.darkwizards.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.misc.Variables;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class WizardTravelling extends Task {

	public WizardTravelling(MethodContext arg0) {
		super(arg0);
	}

	@Override
	public void execute() {
		lodestoneToVarrock();
	}

	@Override
	public boolean activate() {
		Tile localTile = ctx.players.local().getLocation();
		boolean lodeValid = !ctx.objects.select().id(69840).isEmpty();
		return (Variables.useFood
				&& !ctx.backpack.select().id(Variables.foodId).isEmpty() && !lodeValid)
				|| (!lodeValid && !circle.contains(ctx.players.local()))
				|| (!lodeValid
						&& Variables.useFood
						&& !ctx.backpack.select().id(Variables.foodId)
								.isEmpty() && localTile.getY() >= 3424 && localTile
						.getPlane() == 0);
	}

}
