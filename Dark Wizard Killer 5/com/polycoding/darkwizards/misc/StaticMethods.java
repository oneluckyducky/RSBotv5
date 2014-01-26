package com.polycoding.darkwizards.misc;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class StaticMethods {

	public static void showMessageBox(final String title, final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.INFORMATION_MESSAGE);

			}

		});
	}

	public static int getPrice(final int id) {
		try {
			String price;
			final URL url = new URL(
					"http://open.tip.it/json/ge_single_item?item=" + id);
			final URLConnection con = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.contains("mark_price")) {
					price = line.substring(line.indexOf("mark_price") + 13,
							line.indexOf(",\"daily_gp") - 1);
					price = price.replace(",", "");
					return Integer.parseInt(price);

				}
			}
		} catch (final Exception ignored) {
			return -1;
		}
		return -1;
	}

	public static int getPerHour(final int base, final long time) {
		return (int) ((base) * 3600000D / (System.currentTimeMillis() - time));
	}

	public static int[] listToArray(final List<Integer> list) {
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = list.get(i).intValue();
		}
		return ret;
	}

	public static Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
}