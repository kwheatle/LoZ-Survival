/*
 * 
 * Class from tutorial
 * 
 * */

package ca.khalil.level.tile;

import ca.khalil.graphics.Colours;
import ca.khalil.graphics.Screen;
import ca.khalil.level.Level;

public abstract class Tile {// Abstract means its a blueprint
	
	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(000, -1, -1, -1), 0xFF000000);//AARRGGBB - Alpha channel is first | black
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(111, 222, 333, 444), 0xff555555); //Gray
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colours.get(-1, 131, 141, -1), 0xff00ff00); // Green
	public static final Tile AnimGRASS = new AnimatedTile(3, new int[][] { {0, 5}, {1, 5 }, {2, 5}, {1, 5} }, Colours.get(-1, 004, 115, -1), 0xFF0000FF, 1000 );
	
	protected byte id; //Protected means only subclasses can see these values
	protected boolean solid;//Can't walk through
	protected boolean emitter; //Light
	private int levelColour;
	
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
		this.id = (byte) id;
		if(tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = levelColour;
		tiles[id] = this;
	}
	
	public byte getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isEmitter() {
		return emitter;
	}
	
	public int getLevelColour() {
		return levelColour;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen, Level level, int x, int y);
}
