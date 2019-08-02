package ca.khalil.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import ca.khalil.game.entities.Entity;
import ca.khalil.game.entities.Player;
import ca.khalil.game.entities.Zombie;
import ca.khalil.game.items.Weapon;
import ca.khalil.graphics.Colours;
import ca.khalil.graphics.Font;
import ca.khalil.graphics.Screen;
import ca.khalil.graphics.SpriteSheet;
import ca.khalil.level.Level;
//Read the comments above and at the top of each class

//SM - Slightly Modified by me
//HM - Heavily Modified by me
//Main class - from tutorial (HM)

//My classes: Zombie, Player(HM from tutorial), Entity(HM), MainMenu, Settings, HowToPlay, Item, Weapon
//Classes from tutorial (unless stated otherwise): Level(SM), Tile(SM), BasicTile, BasicSolidTile, AnitmatedTile, Font, SpriteSheet, Screen, inputHandler(SM), Colours.

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L; //I don't know what it does, but it gives warnings without it.

	public static final int WIDTH = 320;//Window Width
	public static final int HEIGHT = WIDTH / 12 * 9; // Makes it a 12 by 9 aspect ratio window
	public static final int SCALE = 3;	//To scale up the size by 3 while keeping the aspect ratio
	public static int currWeapon = 2;
	public static String TITLE = "Khalil's Game";//Frame Title
	
	private List<Entity> dead = new ArrayList<Entity>(); //List of dead entities
	
	Random rand = new Random();

	public boolean Running = false;//program running
	public int tickCount = 0;
	
	public long lastWeapSwitch = 0;//Last weapon switch time
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);//Image
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); //Pixels in the image
	private int[] colours = new int[6 * 6 * 6]; // r = 6 bits g = 6 bits g = 6 bits | = 6 * 6 * 6
	
	public static int wave = 0; //wave number
	int numZombiesLeft; //zombies left
	int zombiesKilled = 0;//zombies killed (in total)
	
	private Screen screen;
	public inputHandler input;
	public Level level;
	public Player player;
	public Zombie zom;
	public Weapon sword, fist, staff;

	JFrame frame = new JFrame(TITLE); //Setting the title and initializing the frame
	
	private String PlayerDead;
	private String waveNum;
	private String zombiesLeft;
	private String currWeaponLevel;
	
	//// Values got from the Settings Class || static because other classes are referencing them
	public static int zombiesPerWave;// = Settings.ZombieAmount.getValue(); //
	public static boolean hardmode;// = Settings.rdbtnHardMode.isSelected(); //Hard Mode
	

	public Game() {
		frame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); // These three lines make sure the canvas stays the same size

		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		
		frame.setResizable(true); //I set it to true because it doesn't make a difference to the gameplay
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.add(this, BorderLayout.CENTER);
		frame.setVisible(true); //^ frame stuff
		
	}
	
	
	
	ActionListener zombies = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {

			for(Entity E : level.entities) { //Loops through every Entity in the List level.entities
				if(E.isAlive && E.type.equalsIgnoreCase("zombie")) { //Checking if entity type is a zombie and it is alive
					E.moveToPlayer(); //Makes the entity move to player
					E.Attack(); //Makes the entity attack the player
				}
				
				if(!E.isAlive) { //Checking if entity isn't  alive
					zombiesKilled++;
					dead.add(E); //Mark Entities as dead for later removal
				}
			}//end of for
			level.entities.removeAll(dead);//Remove all dead entities from level.entitites(stops rendering them)
			numZombiesLeft = ((wave * zombiesPerWave) - dead.size());
			if(level.entities.size() == 1 && !dead.contains(player)) { //if only the player is left
				nextWave(); //Start the next wave
				dead.clear(); //Clear the amount of dead
			}//end of if
		}//end Actionperformed
		
	};
	
	Timer zombieMove = new Timer(50, zombies); //Creating timer to move Zombies (and possibly other mobs)

	

	public synchronized void start() { //For calling as an applet
		Running = true;
		new Thread(this).start();
	}

	private void nextWave() { //increases wave #
		wave++; //increase wave number
		for(Weapon w : player.weapons) {
			if(w.isUnlocked == false) {
				w.unlock();
			}
		}//end of for
		if(player.isAlive) {
			if(!hardmode) {
				player.health = 10;
				player.checkHealth();
			}
			for(int i = 0; i < zombiesPerWave * wave; i++) { //Spawning zombies | amount increases by amount of zombies per wave (in settings) every wave
				int x = rand.nextInt((level.width * 8) - 32) + 32;
				int y = rand.nextInt((level.height * 8) - 32) + 32;					
				if(!hardmode) { 			 //Level, type    , x, y, Player, health , attackSpeed
					level.addEntity(new Zombie(level, "Zombie", x, y, player, 5 + wave, 350)); 
				} else {
					level.addEntity(new Zombie(level, "Zombie", x, y, player, 5 + (wave * 2), 200)); 
				}
			} // end of for
		}//end of if
	} //end of nextWave()

	public synchronized void stop() {
		Running = false;
	}

	public void run() {
		long lastTime = System.nanoTime(); //Get the current Time as accurate as possible
		double nanoSecPerTick = 1_000_000_000d / 60d;// setting to 1 billion as a double (setting the nanoseconds per tick)

		int ticks = 0;
		int frames = 0;
		int fps = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while (Running) {
			long now = System.nanoTime(); //Current time
			delta += (now - lastTime) / nanoSecPerTick;
			lastTime = now;
			
			while(delta >= 1) { // Making it update 60 times per second so it runs the same speed on all systems
				ticks++;
				update();
				delta-=1;
			}
			
			frames++;
			render();
		
			
			if(System.currentTimeMillis() - lastTimer >= 1000) { // 1 second is 1000 milliseconds | checking if its been a second since statement last ran
				lastTimer+=1000;
				//System.out.println(frames + " frames, " + ticks + " ticks");
				fps = frames;
				frames = 0;
				ticks = 0;
			}//end of if
			//Displays fps(Frames per Second), Weapon's range, weapon, and xp, in title bar
			frame.setTitle("Khalil's Game | " + fps + " fps | Range: " + (player.getCurrWeapon().range / 8) + " tiles | Damage: " + player.getCurrWeapon().damage + " hp | xp: " + player.getCurrWeapon().xp); 
		}//end of while(running)
	}//end of run

	private void init() {
		
		int index = 0;
		for (int r = 0; r < 6; r++) { // Not sure what this does :(
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);

					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		
		
		frame.requestFocus(); //Give's focus to the window
		
		/************************* DECLARING VARIABLES *************************/
		
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet2.png")); //Setting up the screen variable
		input = new inputHandler(this); //Setting up the input handler
		level = new Level("/levels/level.png"); //Setting the picture for the level to draw
		player = new Player(level, 16, 16, input, 10); //Level, x, y, inputHandler, health
		
						 //String name, int unlockLevel, int attackSpeed, int damage, int range, int id, boolean isRanged
		fist = new Weapon("Fist", 0, 300, 1, 2f, 0, false); //Declaring fist attributes
		sword = new Weapon("Sword", 2, 530, 3, 3f, 1, false); //Declaring sword attributes
		staff = new Weapon("Staff", 5, 100, 2, 1.2f, 5, true); //Declaring staff attributes (it auto attacks)
		
		player.addItem(fist);
		player.addItem(sword);
		player.addItem(staff); //<- ^ Adding weapons to inventory

		level.addEntity(player); //Adds player to level
		
		player.setupEquipment(); // Sets the player's current weapon to their fist
		zombieMove.start(); // Runs the nextWave() - Starts the Zombie wave
	}//end of init
	
	public void update() { //Game logic
		if(!player.isAlive) { //if the player is dead stop updating the game
			zombieMove.stop(); //Stop zombieMove timer
			return;
		}
		waveNum = "Wave: " + wave;
		PlayerDead = "You Lose with " + zombiesKilled + " zombies killed!";
		zombiesLeft = "Zombies Left: " + numZombiesLeft; //Setting string values
		currWeaponLevel = "Weapon Level: " + player.getCurrWeapon().getWeaponLevel();
		
		tickCount++;
		level.tick(); //Calls the tick() function in every entity added to level
		
		for(Weapon w : player.weapons) { //Calls tick() for every unlocked weapon in the player inventory
			if(w.isUnlocked) w.tick();
		}
	}//end of update()

	public void render() { //Drawing stuff
		BufferStrategy bs = getBufferStrategy(); //Organizes how we but the image on the screen
		if (bs == null) {
			createBufferStrategy(3); //Triple Buffering
			return;
		}
		
		int xOffset = player.x - (screen.width / 2); 
		int yOffset = player.y - (screen.height / 2);//<- ^ Setting offsets for scrolling screen 
		
		level.renderTiles(screen, xOffset, yOffset);
		level.renderEntities(screen); // <- ^ render(draw) entities and level
		
		renderUI(); //renders ui
		
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int ColourCode = screen.pixels[x + y * screen.width];
				if (ColourCode < 255) {
					pixels[x + y * WIDTH] = colours[ColourCode];

				}
			}
		} //Setting the pixels on the screen
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); //Draw everything to the screen
			
		g.dispose();

		bs.show(); //Show the contents of buffer
	}//end of render

	public static void main(String[] args) {
		new Game().start(); //Start
	}
	
	private void drawCurrWeapon() {
		switch(player.getCurrWeapon().name) {//Draw item on left side of screen depending on current weapon
		case "Fist": //Fist
			screen.render(screen.xOffset + screen.width - 16,  screen.yOffset + (screen.height / 2 ), 0 + 17 * 32 , Colours.get(000, 142, 543, -1), 0x00, 2);
			break;
		case "Sword": //Sword
			screen.render(screen.xOffset + screen.width - 32,  screen.yOffset + (screen.height / 2 ), 0 + 16 * 32 , Colours.get(000, 321, 222, -1), 0x00, 2);
			screen.render(screen.xOffset + screen.width - 16,  screen.yOffset + (screen.height / 2 ), 1 + 16 * 32 , Colours.get(000, 321, 222, -1), 0x00, 2); 
			//I've done this twice because the picture is 2 tile long
			break;
		case "Staff": //Staff
			screen.render(screen.xOffset + screen.width - 32,  screen.yOffset + (screen.height / 2 ), 0 + 18 * 32 , Colours.get(000, 540, 035, -1), 0x00, 2);
			screen.render(screen.xOffset + screen.width - 16,  screen.yOffset + (screen.height / 2 ), 1 + 18 * 32 , Colours.get(000, 540, 035, -1), 0x00, 2);
			//the same as the sword: the picture is 2 tiles long
			break;
		}
	}
	
	private void renderUI() { //I use a function to do this because it makes things look neater
		if(player.isAlive) {
		/// Rendering the Items on the left side of screen ///	
		drawCurrWeapon();
		
		/// Rendering wave counter
		Font.render(waveNum, screen, screen.xOffset + screen.width - (waveNum.length() * 8), screen.yOffset, Colours.get(-1, -1, -1, 111));
		
		/// Zombies Left
		Font.render(zombiesLeft, screen, screen.xOffset + screen.width - (zombiesLeft.length() * 8), screen.yOffset + 16, Colours.get(-1, -1, -1, 111));
		
		/// Draw the level of the current weapon, if you're not playing hard mode (Because weapon level is disabled in hardmode)
		if(!hardmode) {
			Font.render(currWeaponLevel, screen, screen.xOffset + screen.width - (currWeaponLevel.length() * 8), screen.yOffset + 32, Colours.get(-1, -1, -1, 111));
		}
		
		//% are the hearts on the spritesheet
		//Drawing the hearts on the screen using %'s
		Font.render(player.healthBar, screen, screen.xOffset ,screen.yOffset,Colours.get(-1, 000, 500, 500));
		
		
		//If the player is dead draw string on the screen
		}else {
			Font.render(PlayerDead, screen, screen.xOffset + 40 ,screen.yOffset + (screen.height / 2), Colours.get(-1, -1, -1, 500));
		}
	}//end of renderUI()

}//end of class
