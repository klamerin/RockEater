package fallingOscars;

import javax.swing.JFrame;

public class FallingOscarsEngine {

	public static void main(String[] args) {

		// set standard JFrame settings
		JFrame gameFrame = new JFrame();
		gameFrame.setSize(800, 600);
		GamePanel gamePanel = new GamePanel();
		gameFrame.add(gamePanel);
		gameFrame.setTitle("Falling Oscars");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);

		while (true) {
			gamePanel.oscarFall();
		}

	}
}
