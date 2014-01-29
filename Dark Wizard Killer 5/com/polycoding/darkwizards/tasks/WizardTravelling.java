package com.polycoding.darkwizards.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.util.scriptcore.Task;

public class WizardTravelling extends Task {

	protected final int homeWidget = 1465;
	protected final int homeComponent = 11;

	protected final int lodestoneWidget = 1092;
	protected final int varrockComponent = 51;

	protected final int varrockUnlockedTexture = 10102;

	protected Component homeTele = null;
	protected Component varrockChoice = null;

	private final DarkWizardKiller dwk;

	public WizardTravelling(DarkWizardKiller arg0) {
		super(arg0);
		dwk = arg0;
	}

	@Override
	public void execute() {
		homeTele = ctx.widgets.get(homeWidget, homeComponent);
		varrockChoice = ctx.widgets.get(lodestoneWidget, varrockComponent);
		if (homeTele.click()) {
			log("Lodestone to varrock");
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return isWidgetChildVisible(varrockChoice);
				}
			}, 150, 25);
		}
		if (isWidgetChildVisible(varrockChoice)) {
			if (varrockChoice.getTextureId() == varrockUnlockedTexture) {
				log("Teleporting....");
				varrockChoice.interact("Teleport");
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.players.local().getAnimation() == -1;
					}
				}, 150, 25);

			} else {
				stopScript("Varrock lodestone not unlocked!", true);
			}
		}
	}

	@Override
	public boolean activate() {
		Tile localTile = ctx.players.local().getLocation();
		boolean lodeValid = !ctx.objects.select().id(69840).isEmpty();
		return (dwk.useFood && !ctx.backpack.select().id(dwk.foodId).isEmpty() && !lodeValid)
				|| (!lodeValid && !DarkWizardKiller.CIRCLE.contains(ctx.players
						.local()))
				|| (!lodeValid && dwk.useFood
						&& !ctx.backpack.select().id(dwk.foodId).isEmpty()
						&& localTile.getY() >= 3424 && localTile.getPlane() == 0);
	}

}
