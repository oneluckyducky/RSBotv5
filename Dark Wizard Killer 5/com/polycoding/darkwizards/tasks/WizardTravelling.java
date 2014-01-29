package com.polycoding.darkwizards.tasks;

import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.util.Timer;
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
			final Timer openTimer = new Timer(1500);
			while (openTimer.isRunning()) {
				sleep(100);
				if (!isWidgetChildVisible(varrockChoice))
					openTimer.reset();
			}
		}
		if (isWidgetChildVisible(varrockChoice)) {
			if (varrockChoice.getTextureId() == varrockUnlockedTexture) {
				log("Teleporting....");
				varrockChoice.interact("Teleport");
				final Timer timer = new Timer(2000);
				while (timer.isRunning()) {
					if (ctx.players.local().getAnimation() != -1)
						timer.reset();
				}
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
