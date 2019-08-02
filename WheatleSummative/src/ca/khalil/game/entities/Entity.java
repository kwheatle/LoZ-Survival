/*
 * 
 * Class from tutorial but heavily modified by me
 * 
 * */

package ca.khalil.game.entities;

import ca.khalil.graphics.Screen;
import ca.khalil.level.Level;

public abstract class Entity {
	
	public int x, y; // storing positions
	public int dist; // storing distances
	public int dx, dy; //For getting a distance from a certain object
	public int health; //health
	public boolean isAlive = true;
	protected Level level;
	public String type;
	
	public Entity(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);
	
	public abstract void moveToPlayer();
	
	public abstract void Attack();
}
