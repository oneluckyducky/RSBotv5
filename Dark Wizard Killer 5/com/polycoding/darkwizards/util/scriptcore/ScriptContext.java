package com.polycoding.darkwizards.util.scriptcore;

import org.powerbot.script.methods.Hud;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Widget;

import com.polycoding.darkwizards.DarkWizardKiller;

public class ScriptContext extends MethodProvider {

	public ScriptContext(DarkWizardKiller arg0) {
		super(arg0.getController().getContext());
	}

	public boolean isMomentumEnabled() {
		return ctx.settings.get(1040) > 0;
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
