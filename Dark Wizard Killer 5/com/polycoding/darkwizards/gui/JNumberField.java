package com.polycoding.darkwizards.gui;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTextField;

/*
 * Jacked from rsbot 4025
 * 
 */
public class JNumberField extends JTextField {
	private static final long serialVersionUID = 1L;
	private static final int[] VALID_KEYS = { KeyEvent.VK_ENTER,
			KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE };

	public JNumberField(final String text) {
		super(text);
		Arrays.sort(VALID_KEYS);
	}

	@Override
	public void processKeyEvent(final KeyEvent e) {
		if (Character.isDigit(e.getKeyChar())
				|| Arrays.binarySearch(VALID_KEYS, e.getKeyCode()) != -1) {
			super.processKeyEvent(e);
			return;
		}
		e.consume();
	}
}