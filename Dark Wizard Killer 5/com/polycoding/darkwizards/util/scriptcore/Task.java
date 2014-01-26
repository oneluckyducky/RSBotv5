package com.polycoding.darkwizards.util.scriptcore;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.Tile;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.misc.Variables;
import com.polycoding.darkwizards.util.Timer;

public abstract class Task extends MyMethodProvider {

	Date d = null;

	protected final Area circle = new Area(new Tile(3212, 3378, 0), new Tile(
			3219, 3361, 0), new Tile(3236, 3362, 0), new Tile(3235, 3376, 0));

	public Task(MethodContext arg0) {
		super(arg0);
		d = new Date();
	}

	public abstract void execute();

	public abstract boolean activate();

	public void log(Object o) {
		Variables.status = o.toString();

		System.out.printf("[DWK] -> %s  @  %s\n", o, DateFormat.getInstance()
				.format(d));
	}

	public void stopScript() {
		stopScript("Stopping...");
	}

	public void stopScript(final String s) {
		stopScript(s, false);
	}

	public void stopScript(final String s, final boolean popup) {
		if (popup)
			JOptionPane.showMessageDialog(null, s);
		log(s);
		DarkWizardKiller.poll = -1;
	}

	public void sleep(final long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void lodestoneToVarrock() {
		homeTele = ctx.widgets.get(homeWidget, homeComponent);
		varrockChoice = ctx.widgets.get(lodestoneWidget, varrockComponent);
		if (homeTele.click()) {
			log("Lodestone to varrock");
			// homeTele.click();
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

}
