package fallingOscars;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	// declare variables, no time to set visibility and getters/setters=)
	JLabel leo = null;
	int leoX = 380;
	int leoY = 530;
	int leoSize = 40;
	JLabel oscar = null;
	int oscarSize = 20;
	int oscarX;
	int oscarY;
	int stepY;
	InfoLabel scoreLabel = null;
	InfoLabel livesLabel = null;
	int lives = 3;
	int score = 0;
	JLabel oscarWin = null;
	JLabel oscarLose = null;
	JLabel gameOver = null;

	Random randGen = new Random();

	// gamepanel constructor with all needed labels created and added
	public GamePanel() {
		setLayout(null);

		// create and add leo
		leo = new JLabel();
		leo.setSize(leoSize, leoSize);
		leo.setLocation(leoX, leoY);
		ImageIcon leoIcon = new ImageIcon("img//leo2.jpg");
		leo.setIcon(leoIcon);
		leo.setOpaque(true);
		leo.setVisible(true);
		add(leo);
		addMouseMotionListener(new LeoListener());

		// create and add oscar
		oscar = new JLabel();
		oscar.setSize(oscarSize, oscarSize);
		oscar.setLocation(randGen.nextInt(780), 0);
		ImageIcon oscarIcon = new ImageIcon("img//oscars.jpg");
		oscar.setIcon(oscarIcon);
		oscar.setOpaque(true);
		oscar.setVisible(true);
		add(oscar);

		// create and add score label (InfoLabel class)
		scoreLabel = new InfoLabel(20);
		scoreLabel.setText("Score: " + score);
		add(scoreLabel);

		// create and add lives label (InfoLabel class)
		livesLabel = new InfoLabel(45);
		livesLabel.setText("Lives: " + lives);
		add(livesLabel);

		// create and add win gif
		oscarWin = new JLabel();
		oscarWin.setSize(500, 213);
		oscarWin.setLocation(150, 150);
		ImageIcon oscarWinIcon = new ImageIcon("img//leooscar.gif");
		oscarWin.setIcon(oscarWinIcon);
		oscarWin.setOpaque(true);
		oscarWin.setVisible(false);
		add(oscarWin);

		// create and add lose gif
		oscarLose = new JLabel();
		oscarLose.setSize(500, 252);
		oscarLose.setLocation(150, 150);
		ImageIcon oscarLoseIcon = new ImageIcon("img//leocrying.gif");
		oscarLose.setIcon(oscarLoseIcon);
		oscarLose.setOpaque(true);
		oscarLose.setVisible(false);
		add(oscarLose);

		// create and add game over label
		gameOver = new JLabel();
		gameOver.setSize(190, 30);
		gameOver.setLocation(325, 100);
		gameOver.setOpaque(false);
		gameOver.setVisible(false);
		gameOver.setText("GAME OVER");
		gameOver.setFont(new Font("Serif", Font.BOLD, 30));
		gameOver.setForeground(Color.RED);
		add(gameOver);
		playSound();
	}

	// draw background
	public void paintComponent(Graphics g) {
		Image background = new ImageIcon("img//hollywood.jpg").getImage();
		g.drawImage(background, 0, 0, this);
	}

	// make the oscar fall through the screen and get back up
	public void oscarFall() {
		oscarX = oscar.getX();
		oscarY = oscar.getY();
		stepY = 1;
		while (oscarY <= 580) {
			// move oscars down the screen
			oscar.setLocation(oscarX, oscarY);
			oscarY += stepY;

			// check if Leo catches oscars
			if ((oscar.getY() + oscarSize) >= leo.getY()
					&& oscar.getY() <= leo.getY()
					&& (oscar.getX() + oscarSize) > leo.getX()
					&& oscar.getX() < (leo.getX() + leoSize)) {
				oscarY = 0;
				oscarX = randGen.nextInt(780);

				// change score
				score++;
				scoreLabel.setText("Score: " + score);

				// pause thread and show win gif when oscar is caught
				oscarWin.setVisible(true);
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// hide win gif again
				oscarWin.setVisible(false);

				// get bonus live every ten points
				if (score % 10 == 0) {
					lives++;
					livesLabel.setText("Lives: " + lives);
				}
			}

			// check if Oscar falls uncaught
			if (oscarY == 580) {
				oscarY = 0;
				oscarX = randGen.nextInt(780);

				// change lives
				lives--;
				if (lives < 0) {
					gameOver();
				} else {
					livesLabel.setText("Lives: " + lives);

					// pause thread and show lose gif when oscar falls uncaught
					oscarLose.setVisible(true);
					try {
						Thread.sleep(350);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// hide lose gif again
					oscarLose.setVisible(false);
				}
			}

			// thread sleep so the oscar appears like it's moving down
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// remove oscar and info labels from screen when the game is over, leave win
	// gif and game over label
	public void gameOver() {
		oscarLose.setVisible(true);
		gameOver.setVisible(true);
		remove(oscar);
		remove(livesLabel);
		remove(scoreLabel);
		repaint();
	}

	// set background music
	public void playSound() {
		String file = "img//Titanic Theme Song.wav";
		File soundFile;
		Clip clip;
		AudioInputStream audioIn;
		soundFile = new File(file);
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// create listener to move leo with the mouse
	public class LeoListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			leoX = e.getX();
			if (leoX > getWidth() - leo.getWidth()) {
				leoX = getWidth() - leo.getWidth();
			}
			leo.setLocation(leoX, leoY);
		}

	}
}
