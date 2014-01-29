package com.polycoding.darkwizards.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.polycoding.darkwizards.DarkWizardKiller;
import com.polycoding.darkwizards.tasks.BankHandling;
import com.polycoding.darkwizards.tasks.BankTravelling;
import com.polycoding.darkwizards.tasks.Combat;
import com.polycoding.darkwizards.tasks.WizardTravelling;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel label1 = new JLabel("Food Id");
	JLabel label2 = new JLabel("Food Quantity");
	JLabel label3 = new JLabel("No food id = no banking");
	JLabel label4 = new JLabel("No food quantity = 8 food");

	JNumberField foodId = new JNumberField("");
	JNumberField foodQuantity = new JNumberField("");

	JButton btnStart = new JButton("Start");

	private DarkWizardKiller dwk;

	public GUI(DarkWizardKiller arg0) {
		dwk = arg0;

		this.setSize(200, 225);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		label3.setBounds(5, 5, 150, 25);
		this.add(label3);

		label4.setBounds(label3.getX(), label3.getY() + 15, 150, 25);
		this.add(label4);

		label1.setBounds(5, label4.getY() + 30, 150, 25);
		this.add(label1);

		foodId.setBounds(5, label1.getY() + 25, 150, 25);
		this.add(foodId);

		label2.setBounds(5, foodId.getY() + 25, 150, 25);
		this.add(label2);

		foodQuantity.setBounds(5, label2.getY() + 25, 150, 25);
		this.add(foodQuantity);

		btnStart.setBounds(5, foodQuantity.getY() + 35, 75, 25);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStart(e);
			}
		});
		this.add(btnStart);

		this.setVisible(true);

	}

	private void btnStart(ActionEvent e) {
		final String idText = foodId.getText();
		final String quantityText = foodQuantity.getText();

		if (idText.isEmpty() && quantityText.isEmpty()) {
			dwk.useFood = false;

		} else if (!idText.isEmpty() && quantityText.isEmpty()) {
			dwk.useFood = true;
			dwk.foodId = Integer.parseInt(idText);
			dwk.foodAmount = 8;

		} else if (!idText.isEmpty() && !quantityText.isEmpty()) {
			dwk.useFood = true;
			dwk.foodId = Integer.parseInt(idText);
			dwk.foodAmount = Integer.parseInt(quantityText);

		} else if (idText.isEmpty() && !quantityText.isEmpty()) {
			JOptionPane.showMessageDialog(null, "wut");
			return;
		}

		dwk.guiIsDone = true;
		dwk.startTime = 0;
		this.dispose();

		dwk.taskList.add(new BankTravelling(dwk));
		dwk.taskList.add(new BankHandling(dwk));
		dwk.taskList.add(new Combat(dwk));
		dwk.taskList.add(new WizardTravelling(dwk));
	}

}