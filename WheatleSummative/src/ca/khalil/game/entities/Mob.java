/*
 * 
 * Class from tutorial. Slightly modified by me: added some more variables etc
 * 
 * */

package ca.khalil.game.entities;

import ca.khalil.level.Level;
import ca.khalil.level.tile.Tile;

public abstract class Mob extends Entity{
	
	//protected String type;//Type of mob (Zombie, dog, etc.)
	protected int speed, numSteps = 0, movingDir = 1, scale = 1;
	protected float attackRange; // 0 is up 1 is down 2 is left 3 is right
	//public int health;
	protected final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3; //Directions to be looking
	protected boolean isMoving;
	public boolean Attacking;

	public Mob(Level level, String type, int x, int y, int speed) {
		super(level);
		this.type = type;
		this.speed = speed;
		this.x = x;
		this.y = y;
	}
	

	public void move(int xa, int ya) {
		if(xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if(!hasCollided(xa, ya)) { //if you wont be colliding with anything
			if(ya < 0) {
				movingDir = UP;
			}
			if(ya > 0) {
				movingDir = DOWN;
			}
			if(xa < 0) {
				movingDir = LEFT;
			}
			if(xa > 0) {
				movingDir = RIGHT;
			}
			
			x += xa * speed;
			y += ya * speed;
		}
	}
	
	public abstract boolean hasCollided(int xa, int ya);
	
	protected boolean isSolidTile(int xa, int ya, int x, int y) { //Checks if the tile infront of you is solid
		if(level == null) {return false;}
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
		if(!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;		
	}
	
	public abstract void Attack();
	
	public String getName() {
		return type;
	}
}
