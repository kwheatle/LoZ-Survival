package ca.khalil.game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;

public class Settings {

	//Game game = new Game();

	private JFrame frame;
	public JLabel lblZperWave;
	public static JSlider ZombieAmount;
	public static JRadioButton rdbtnHardMode;
	final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings window = new Settings();
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
	public Settings() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Only close the window not Stop the program
		frame.getContentPane().setLayout(null);
		
		JRadioButton rdbtnNormalMode = new JRadioButton("Normal Mode");
		rdbtnNormalMode.setForeground(Color.ORANGE);
		rdbtnNormalMode.setBackground(Color.BLACK);
		rdbtnNormalMode.setSelected(true);
		buttonGroup.add(rdbtnNormalMode);
		rdbtnNormalMode.setBounds(6, 87, 109, 23);
		frame.getContentPane().add(rdbtnNormalMode);
		
		rdbtnHardMode = new JRadioButton("Hard Mode");
		rdbtnHardMode.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Game.hardmode = rdbtnHardMode.isSelected();
			}
		});
		rdbtnHardMode.setForeground(Color.ORANGE);
		rdbtnHardMode.setBackground(Color.BLACK);
		buttonGroup.add(rdbtnHardMode);
		rdbtnHardMode.setBounds(6, 129, 109, 23);
		frame.getContentPane().add(rdbtnHardMode);
		
		JLabel lblDifficulty = new JLabel("Difficulty");
		lblDifficulty.setForeground(Color.ORANGE);
		lblDifficulty.setFont(new Font("Power Clear", Font.BOLD, 16));
		lblDifficulty.setBounds(10, 27, 80, 30);
		frame.getContentPane().add(lblDifficulty);
		
		JLabel lblZombiesPerWave = new JLabel("Zombies Per Wave");
		lblZombiesPerWave.setForeground(Color.ORANGE);
		lblZombiesPerWave.setFont(new Font("Power Clear", Font.BOLD, 16));
		lblZombiesPerWave.setBounds(192, 27, 160, 30);
		frame.getContentPane().add(lblZombiesPerWave);
		
		ZombieAmount = new JSlider();
		ZombieAmount.setBackground(Color.BLACK);
		ZombieAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(ZombieAmount.getValueIsAdjusting()) {//If you're changing the slider value
					Game.zombiesPerWave = ZombieAmount.getValue(); //Sets the value to whatever the slider is
					lblZperWave.setText("" + ZombieAmount.getValue());//Shows the value of the slider in a Label
				}
			}
		});
		ZombieAmount.setValue(5);
		ZombieAmount.setMaximum(20);
		ZombieAmount.setMinimum(1);
		ZombieAmount.setBounds(176, 84, 200, 26);
		frame.getContentPane().add(ZombieAmount);
		
		lblZperWave = new JLabel("" + ZombieAmount.getValue());
		lblZperWave.setForeground(Color.ORANGE);
		lblZperWave.setBounds(247, 133, 81, 14);
		frame.getContentPane().add(lblZperWave);
	}
}
