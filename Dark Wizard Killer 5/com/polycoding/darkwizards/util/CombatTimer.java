package com.polycoding.darkwizards.util;

import org.powerbot.script.methods.MethodContext;

import com.polycoding.darkwizards.util.scriptcore.MyMethodProvider;

/*
 * ignore
 */
public class CombatTimer extends MyMethodProvider implements Runnable {

	public CombatTimer(MethodContext ctx) {
		super(ctx);
		combatTimer = new Timer(2400); // 4 game ticks
	}

	@Override
	public void run() {
		while (true) {
			if (ctx.players.local().getAnimation() == -1) {
				combatTimer.reset();
			}
			sleep(600);
		}
	}
}
