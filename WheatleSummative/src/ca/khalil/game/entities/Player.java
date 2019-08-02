/*
 * Functions in the class are from tutorial unless stated other wise
 * 
 * */

package ca.khalil.game.entities;

import java.util.ArrayList;
import java.util.List;

import ca.khalil.game.Game;
import ca.khalil.game.inputHandler;
import ca.khalil.game.items.Weapon;
import ca.khalil.graphics.Colours;
import ca.khalil.graphics.Screen;
import ca.khalil.level.Level;

//This class is heavily modified from the tutorial to suit my own needs.

public class Player extends Mob {
	
	public List<Weapon> weapons = new ArrayList<Weapon>(); //Making an array of weapons
	
	private inputHandler input;
	private int colour = Colours.get(000, 142, 554, -1); // Black, Dark gray, light Gray, white (-1 means invisible)
	private int scale = 1;
	private int attack = 0, attackingSpeed = 4;
	private int xa = 0, ya = 0;
	private long lastAttackTime = 0, invinTimer = 1000, lastHitTimer = 0;
	private boolean attackAnim = false;
	public boolean invincible = false;//Invincible is used with a timer to make it so that when zombies are clumped up they can't all hit you at once
		
	public String healthBar = "";
	
	private Weapon currWeapon;
	int weaponIndex = 0;

	private long lastWeapSwitch = 0;
	
	public Player(Level level, int x, int y, inputHandler input,int health) {// I added attackRange and Health
		super(level, "player", x, y, 1);
		this.input = input;
		this.health = health;
		for(int i = 0; i < this.health; i++) { //for all the health you have, add a "%" to the healthBar string.(i render % as hearts on the screen)
			this.healthBar += "%";
		}
		
		//Fills the array with dummy item's so that no matter the weapon id it goes in the right slot (without throwing indexOutOfBounds Error)
		//Makes it so that the ids don't need to be added in chronological order
		for(int i = 0; i < 100; i++) {
			weapons.add(new Weapon("dummyItem", -1, 0, 0, 0, 0, false));
		}
		
	}

	public void tick() { //Doing a lot of stuff
		xa = 0;
		ya = 0;
		/*************** Movement and Key Presses ***************/
		if (input.up.isPressed()) { //Self-explanatory
			if (!attackAnim) //I use attackAnim here (instead of "Attacking" boolean) to Sync animation with attack cooldown
				ya--; //Say we are going to move up
		}
		if (input.down.isPressed()) {
			if (!attackAnim)
				ya++; //Say we are going to move down
		}
		if (input.left.isPressed()) {
			if (!attackAnim)
				xa--; //Say we are going to move left
		}
		if (input.right.isPressed()) {
			if (!attackAnim)
				xa++; //Say we are going to move right
		}
		if (input.spacebar.isPressed()) { //My work | attack check
			Attack();
		} else {
			attackAnim = false;
			attack--;
			Attacking = false;
		}
		
		/*************** Weapon Switch ***************/
		
		if(input.q.isPressed()) {
			if (System.currentTimeMillis() - lastWeapSwitch  > 250) { //Switch weapon (1/4 second delay)
				do { //Move on to the next weapon
					weaponIndex++;
					if(weaponIndex > weapons.size() - 1) { //Prevents out of bounds index
						weaponIndex = 0;
					}
					//System.out.println(weaponIndex + " || " + weapons.get(weaponIndex).isUnlocked);
				} while(weapons.get(weaponIndex).isUnlocked == false);//If the next weapon isn't unlocked yet keep searching for the next weapon that is
				currWeapon = weapons.get(weaponIndex); //The the weapon to the current weapon
				lastWeapSwitch = System.currentTimeMillis();
			}
			
		}//end of q.isPressed()
		
		/*************** Setting Invincibility Period ***************/
				
		if(Game.hardmode == false) { //If hard mode is selected turn off invincibility timer
			if(invincible && System.currentTimeMillis() - lastHitTimer > invinTimer) { //Allows temporary invincibility after being hit
				invincible = false;
				lastHitTimer = System.currentTimeMillis();
			}
		} else {
			invincible = false;
		}
		
		/*************** Attacking Stuff ***************/
		
		for(Entity e : level.entities) {//For each entity in level.entities
			if(e.type.equalsIgnoreCase("Zombie")) { //if entity is a zombie
				e.dx = Math.abs(x - e.x); //Getting distance on the x axis
				e.dy = Math.abs(y - e.y); //Getting distance on the y axis
				
				//Pythagoream Theorem to get the total distance from enemy
				double distance = Math.sqrt((e.dx * e.dx) + (e.dy * e.dy)); 
								
				if(currWeapon.name.equalsIgnoreCase("sword")) { //If using sword
					//With the sword you can enemies 1 tile of to the side of where you're looking
					//the e.dy <= attackRange / 2 and e.dx <= attackRange / 2 allows for
					// a wider attack range on a 90 degree angle from where your looking
					if(distance <= currWeapon.range && Attacking) { //if entity distance is within attacking range and i'm attacking
						//if i'm looking in their direction and they're only a certain amount of pixels off in the perpendicular axis the hit them
						if(e.x >= x && movingDir == RIGHT && e.dy <= currWeapon.range / 2) {
							e.health-=currWeapon.damage;//Take Health away
						} else if (e.x <= x && movingDir == LEFT && e.dy <= currWeapon.range / 2) {
							e.health-=currWeapon.damage;
						}
						
						if(e.y >= y && movingDir == DOWN && e.dx <= currWeapon.range / 2) {
							e.health-=currWeapon.damage;
						}else if (e.y <= y && movingDir == UP && e.dx <= currWeapon.range / 2) {
							e.health-=currWeapon.damage;
						}
					}
				} //end of currWeapon
				
				if(currWeapon.name.equalsIgnoreCase("fist")) { //If using Fist
					if(distance <= currWeapon.range && Attacking) { //if entity distance is within attacking range and im attacking
						//if im looking in their direction and they're only a certain amount of pixels off in the perpendicular axis the hit them
						if(e.x >= x && movingDir == RIGHT && e.dy <= currWeapon.range / 4) { 
							e.health-=currWeapon.damage;
						} else if (e.x <= x && movingDir == LEFT && e.dy <= currWeapon.range / 4) {
							e.health-=currWeapon.damage;
						}
						
						if(e.y >= y && movingDir == DOWN && e.dx <= currWeapon.range / 4) {
							e.health-=currWeapon.damage;
						}else if (e.y <= y && movingDir == UP && e.dx <= currWeapon.range / 4) {
							e.health-=currWeapon.damage;
						}
					}
				}//end of currWeapon
				
				if(currWeapon.name.equalsIgnoreCase("staff")) { //If using the Staff
					//if they're within a certain radius of me (Weapon range) attack.
					if(distance <= currWeapon.range && currWeapon.canAttack) {
						e.health-=currWeapon.damage;
					}
				}//end of currWeapon
			}//end of e.type == "zombie"
		}//end of for
		
		
		/*************** Movement and Health ***************/
		
		if(health <= 0) { //If health <= 0 then tell everything im dead
			isAlive = false;
		}

		if (xa != 0 || ya != 0) { //Move
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

	}//end of tick
	
	public void checkHealth() {//My own function for checking health
		healthBar = "";
		for(int i = 0; i < health; i++) {
			healthBar += "%"; //Adds a % for the amount of health we have
		}
	}

	public void render(Screen screen) { //Heavily Modified by me: Rendering animations etc.
		int xTile = 0;
		int yTile = 25;
		int walkingSpeed = 4;
		//attackingSpeed = 3;
		int flipTop = (numSteps >> walkingSpeed) & 1; // Divides numSteps by 2^4 and the &'s to get between 0 and 1
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (!attackAnim) { // If not attacking normal animation
			if (movingDir == DOWN) { // Down
				xTile = 0;
			} else if (movingDir == UP) {// Up
				xTile = 8;
			} else {// Left or Right
				xTile = 4 + ((numSteps >> walkingSpeed) & 1) * 2;// Will either give 5 xTile + (0 or 2) so xTile will
																	// either be 4 or 6 (because they are 2 block wide)
				flipTop = (movingDir - 1) % 2;// Number between 0 and 1
				flipBottom = (movingDir - 1) % 2;
			}
		} else { // If attacking do attacking animation
			if (movingDir == DOWN) { // Down
				yTile = 21 + ((attack >> (attackingSpeed + 1)) & 1) * 2;
				xTile = 0;
			} else if (movingDir == UP) {// Up
				yTile = 23 - ((attack >> (attackingSpeed + 1)) & 1) * 2;
				xTile = 8;
			} else {// Left or Right
				yTile = 23;
				xTile = 4 + ((attack >> attackingSpeed) & 1) * 2;
				flipTop = (movingDir - 1) % 2;
				flipBottom = (movingDir - 1) % 2;
			}
		}

		int modifier = 8 * scale; // Handles the scale if the character is bigger than 8 * 8
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); // if not
																											// flipping
																											// flipTop =
																											// 0
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop,scale);
		// ^Upper Body vLower Body
		screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale);
		screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale);
	}//end of render()

	public boolean hasCollided(int xa, int ya) { //Check for collision
		int xMin = 0;
		int xMax = 7;//0 to 7 = 8 total number
		int yMin = 3;
		int yMax = 7;
		for(int x = xMin; x < xMax; x++) {
			if(isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for(int x = xMin; x < xMax; x++) {
			if(isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for(int y = yMin; y < yMax; y++) {
			if(isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for(int y = yMin; y < yMax; y++) {
			if(isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}
		return false;
	}//end of hasCollided()

	public void Attack() { //My function for attack time
		attack++;
		if (System.currentTimeMillis() - lastAttackTime > currWeapon.attackSpeed ) {//Checks if the last time you attacked was longer the the time set between attacks(attackSpeed)
			Attacking = true;
			attackAnim = true;
			lastAttackTime = System.currentTimeMillis();
		} else {
			Attacking = false;
		}

	}//end of attack

	@Override
	public void moveToPlayer() {//My function
		
	}
	
	public void addItem(Weapon weapon) { //My function for adding Items to my inventory
		this.weapons.add(weapon.id, weapon); //Inserts the item at the index of its id
		//i use this add function instead of the other one for organizations sack if i ever add more weapons
		//it makes sure all the items are in order of their id number
	}
	
	public void setupEquipment() {
		currWeapon = weapons.get(0); //Set the current weapon to the first item with the id of 0 (the fist)
	}
	
	public Weapon getCurrWeapon() {
		return currWeapon;
	}
	
	public int getCurrWeaponId() {
		return currWeapon.id;
	}

}
