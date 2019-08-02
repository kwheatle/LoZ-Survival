/*
 * 
 * Class from tutorial
 * 
 * */

package ca.khalil.level.tile;

public class BasicSolidTile extends BasicTile{

	public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, x, y, tileColour, levelColour);
		this.solid = true;
	}

}
