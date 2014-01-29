package com.polycoding.darkwizards.util.scriptcore;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.polycoding.darkwizards.DarkWizardKiller;

public abstract class Task extends ScriptContext {

	Date d = null;

	DarkWizardKiller dwk = null;

	public Task(DarkWizardKiller arg0) {
		super(arg0);
		dwk = arg0;
		d = new Date();
	}

	public abstract void execute();

	public abstract boolean activate();

	public void log(Object o) {
		dwk.status = o.toString();

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
		dwk.taskList.clear();
	}

	public void sleep(final long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
