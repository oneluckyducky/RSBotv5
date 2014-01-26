package com.polycoding.darkwizards.util.scriptcore;

import org.powerbot.script.methods.Hud;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Widget;

import com.polycoding.darkwizards.util.Timer;

public class MyMethodProvider extends MethodProvider {

	public Timer combatTimer = new Timer(3000);

	protected final int homeWidget = 1465;
	protected final int homeComponent = 11;

	protected final int lodestoneWidget = 1092;
	protected final int varrockComponent = 51;

	protected final int varrockUnlockedTexture = 10102;

	protected Component homeTele = null;
	protected Component varrockChoice = null;

	public MyMethodProvider(MethodContext arg0) {
		super(arg0);
	}

	

	public boolean isIdle() {
		final Player local = ctx.players.local();
		return (ctx.players.local() != null && !local.isInMotion()
				&& local.getAnimation() == -1 && !local.isInCombat() && (local
				.getInteracting() == null && !local.isInCombat() || local
				.getInteracting().getHealthRatio() == 0));
	}

	public boolean isMomentumEnabled() {
		return ctx.settings.get(1040) > 0;
	}

	public int getPlayerAnimation() {
		return ctx.players.local().getAnimation();
	}

	public boolean isSlotEmpty(final int slot) {
		return ctx.backpack.getItemAt(slot) == null;
	}

	public boolean isWidgetChildVisible(final Component wc) {
		return wc != null && wc.isValid() && wc.isVisible() && wc.isOnScreen();
	}

	public boolean isWidgetVisible(final Widget w) {
		return w != null && w.isValid();
	}

	public boolean inventoryContains(final int... ids) {
		ctx.hud.view(Hud.Window.BACKPACK);
		return ctx.backpack.select().id(ids).count() > 0;
	}

	public boolean isInGame() {
		return ctx.game.isLoggedIn() && ctx.game.getClientState() == 11
				&& !ctx.lobby.isOpen();
	}
}
