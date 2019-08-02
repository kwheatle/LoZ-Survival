/*This class was created by me for my Zombie mobs
 * 
 * 
 * 
 * */

package ca.khalil.game.entities;

import ca.khalil.game.inputHandler;
import ca.khalil.graphics.Colours;
import ca.khalil.graphics.Screen;
import ca.khalil.level.Level;

public class Zombie extends Mob{
	
	private Player player;
	private inputHandler input;
	private int colour = Colours.get(-1, 000, 030, 142); // Black, Dark gray, light Gray, white (-1 means invisible)
	private int scale = 1;
	private int attack = 0;
	private long lastAttackTime = 0, attackSpeed = 300;
	
	public Zombie(Level level, String type, int x, int y, Player player, int health, int attackSpeed) {
		super(level, type, x, y, 1); // <- Level, type, x, y, speed
		this.player = player;
		this.health = health;
		this.isAlive = true;
		this.attackSpeed = attackSpeed;
	}

	@Override
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;//0 - 7 = 8
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
	}//end of has collided

	@Override
	public void Attack() { //Attack function for the zombies
		int dx = Math.abs(x - player.x);
		int dy = Math.abs(y - player.y);
		if (System.currentTimeMillis() - lastAttackTime > attackSpeed) {//if the time since last ran is greater than attackSpeed. (a kind of timer)
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			
			if(distance <= 8 && !player.invincible) {
				player.invincible = true;
				player.health--;
				player.healthBar = ""; //Setting health to nothing
				for(int i = 0; i < player.health; i++) {
					player.healthBar += "%"; //Adding health back
				}
			}	
			lastAttackTime = System.currentTimeMillis();
		}
	}//end of attack
	
	@Override
	public void tick() {// Updates logic for the Zombie			
		if(health <= 0 && isAlive) {
			player.getCurrWeapon().xp++; //If zombie get killed add 1 xp to weapon used to kill it.
			isAlive = false;
			return; //Stops this from being run more than once
		}
	}
	
	public void moveToPlayer() {//My Own function: makes the mob always go to the player
		int xa = 0, ya = 0;
		
		if( x > player.x) {
			xa--;
		}
		else if( x < player.x) {
			xa++;
		}
		if(y > player.y) {
			ya--;
		}
		else if(y < player.y) {
			ya++;
		}
		
		if(xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
	}//end of moveToPlayer

	@Override
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 2;
		//int attackingSpeed = 3;
		int flipTop = (numSteps >> walkingSpeed) & 1; // Divides numSteps by 2^4 and the &'s to get between 0 and 1
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (!Attacking) { // If not attacking normal animation
			if (movingDir == 1) { // Down
				xTile = 0;
			}  else if(movingDir == 0) {
				xTile = 2;
			}
			else {// Left or Right
				xTile = 4 + ((numSteps >> walkingSpeed) & 1) * 2;// Will either give 5 xTile + (0 or 2) so xTile will
																	// either be 4 or 6 (because they are 2 block wide)
				flipTop = (movingDir - 1) % 2;// Num between 0 and 1
				flipBottom = (movingDir - 1) % 2;
			}
		}//end of if
		
		int modifier = 8 * scale; // Handles the scale if the character is bigger than 8 * 8
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); // if not
																											// flipping
																											// flipTop =
																											// 0
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop,
				scale);
		// ^Upper Body vLower Body
		screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour,
				flipBottom, scale);
		screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32,
				colour, flipBottom, scale);
	}//end of render()
	
	

}
