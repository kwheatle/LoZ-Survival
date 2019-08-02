package ca.khalil.game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class HowToPlay {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HowToPlay window = new HowToPlay();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HowToPlay() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 588, 766);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//Only close the window not Stop the program
		frame.getContentPane().setLayout(null);
		
		JTextArea txtrHtp = new JTextArea();
		txtrHtp.setLineWrap(true);
		txtrHtp.setBackground(Color.BLACK);
		txtrHtp.setForeground(Color.ORANGE);
		txtrHtp.setWrapStyleWord(true);
		txtrHtp.setEditable(false);
		txtrHtp.setFont(new Font("Footlight MT Light", Font.PLAIN, 23));
		txtrHtp.setText("This is a wave based game where once you kill all zombies the wave number will increase. Each zombie is worth 1xp.\r\n\r\nEvery wave the zombies get more health and increase in numbers (Change the number in settings). At the begining of each wave you get full health. As you kill zombies (with a weapon) that weapon will level up. Each level requires 5 more xp to rankup than the last. Ranged weapons get 1 extra range and non-ranged weapons do 1 extra damage.\r\n\r\nUse W, A , S, D to move. Q to switch weapons, and Spacebar to attack. Item info (such as range and damage) can be found in the game's title bar\r\n\r\nYou start off with your punch and at round 2 you get your Sword, at round 5 you get a staff that auto attacks emenys in a certain area around you (ranged).\r\n\r\nHardmode (in settings) makes it so that you have no invincibility timer after being hit, it disables weapon xp/levels, disables health regen after each wave, and makes zombies gain more health per wave than usual (extra 2 per wave instead of 1), while also making them attack faster.");
		txtrHtp.setBounds(10, 171, 562, 556);
		frame.getContentPane().add(txtrHtp);
		
		JLabel lblPicture = new JLabel("");
		lblPicture.setIcon(new ImageIcon(HowToPlay.class.getResource("/levels/Logo.png")));
		lblPicture.setBounds(141, -14, 300, 195);
		frame.getContentPane().add(lblPicture);
	}
}
