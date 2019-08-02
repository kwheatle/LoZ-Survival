package ca.khalil.game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/*
 * 
 * My own class
 * Deals with the Main Menu
 * 
 * */


public class MainMenu {

	private JFrame menu;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.menu.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public MainMenu() {
		initialize();
		Game.zombiesPerWave = 5; //Here in case they don't run setting
		Game.hardmode = false; //Again in case they don't open the settings window
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		menu = new JFrame();
		menu.getContentPane().setBackground(Color.BLACK);
		menu.setTitle("Khalil's Summative | Menu");
		menu.setBounds(500, 100, 565, 541);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.getContentPane().setLayout(null);

		JTextArea txtrMenu = new JTextArea();
		txtrMenu.setText("> Play <\r\n\r\nHow To Play\r\n\r\nSettings\r\n\r\nExit");
		txtrMenu.setForeground(Color.ORANGE);
		txtrMenu.setEditable(false);
		txtrMenu.setFocusable(true);
		txtrMenu.setFont(new Font("Algerian", Font.PLAIN, 26));
		txtrMenu.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) { //Cycling through menu selections
					if (txtrMenu.getText().contains("> Play")) {
						txtrMenu.setText("Play\r\n\r\nHow To Play\r\n\r\nSettings\r\n\r\n> Exit <");						
					} else if (txtrMenu.getText().contains("> How")) {
						txtrMenu.setText("> Play <\r\n\r\nHow To Play\r\n\r\nSettings\r\n\r\nExit");
					} else if (txtrMenu.getText().contains("> Settings")) {
						txtrMenu.setText("Play\r\n\r\n> How To Play <\r\n\r\nSettings\r\n\r\nExit");
					} else if (txtrMenu.getText().contains("> Exit")) {
						txtrMenu.setText("Play\r\n\r\nHow To Play\r\n\r\n> Settings <\r\n\r\nExit");
					}
				}//end of if
				
				if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) { //Cycling through menu selections
					if (txtrMenu.getText().contains("> Play")) {
						txtrMenu.setText("Play\r\n\r\n> How To Play <\r\n\r\nSettings\r\n\r\nExit");
					} else if (txtrMenu.getText().contains("> How")) {
						txtrMenu.setText("Play\r\n\r\nHow To Play\r\n\r\n> Settings <\r\n\r\nExit");
					} else if (txtrMenu.getText().contains("> Settings")) {
						txtrMenu.setText("Play\r\n\r\nHow To Play\r\n\r\nSettings\r\n\r\n> Exit <");
					} else if (txtrMenu.getText().contains("> Exit")) {
						txtrMenu.setText("> Play <\r\n\r\nHow To Play\r\n\r\nSettings\r\n\r\nExit");
					}
				}//end of if

				if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) { //To select certain actions
					if (txtrMenu.getText().contains("> Play")) {
						Game.main(null); //Start main game
						menu.setVisible(false); //hide form
						menu.dispose(); //Close form to free memory
					} else if (txtrMenu.getText().contains("> How")) {
						HowToPlay.main(null); //Runs the "How To Play" menu
					} else if (txtrMenu.getText().contains("> Settings")) {
						Settings.main(null); //Run the settings window
					} else if (txtrMenu.getText().contains("> Exit")) {
						menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));//Close Frame and exit program | Simulates clicking the "close" button
					}
				}//end of if
			}//end of keyPressed
		});
		txtrMenu.setBackground(menu.getContentPane().getBackground());
		txtrMenu.setBounds(191, 227, 348, 264);
		menu.getContentPane().add(txtrMenu);
		
		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(MainMenu.class.getResource("/levels/Logo.png")));
		lblLogo.setBounds(125, 23, 304, 193);
		menu.getContentPane().add(lblLogo);

	}//end of Initialize
}//end of class
