package fallingOscars;

import java.awt.Color;

import javax.swing.JLabel;

public class InfoLabel extends JLabel {
	// info labels constructor (used twice, save a few lines of repeating code)
	public InfoLabel(int height) {
		setSize(90, 20);
		setLocation(700, height);
		setOpaque(false);
		setVisible(true);
		setForeground(Color.PINK);
	}
}
