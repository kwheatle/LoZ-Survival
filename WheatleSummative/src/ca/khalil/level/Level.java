/*
 * 
 * Functions from tutorial unless stated otherwise
 * 
 * */

package ca.khalil.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.imageio.ImageIO;

import ca.khalil.game.entities.Entity;
import ca.khalil.graphics.Screen;
import ca.khalil.level.tile.Tile;

public class Level {

	private byte[] tiles; // Array of id's for the tiles in that coordinate
	public int width, height;
	public List<Entity> entities = new ArrayList<Entity>();
	
	private String imagePath;
	private BufferedImage image;

	public Level(String imagePath) {
		if(imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else { //if image path isn't specified the set default width and height and generate default level
			this.width = 64;
			this.height = 64;
			tiles = new byte[width * height];
			this.generateLevel();
		}
	}
	
	private void loadLevelFromFile() {
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));//Getting image to read
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTiles() {
		int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width); //Turns the data into an int[]. The data in its relative position
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tileCheck: for (Tile t : Tile.tiles) {//Loops through every tile in the tiles variable| for loops is name tileCheck(???? since when!?!!?!?!)
					if(t != null && t.getLevelColour() == tileColours[x + y * width]) {
						this.tiles[x + y * width] = t.getId();
						break tileCheck; //only break out of "tileCheck"
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile())); //Gets image and puts it into the file
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void alterTile(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x * y % 10 < 8) {
					tiles[x + y * width] = Tile.AnimGRASS.getId(); // [x + y * width] is how me get our current tile
				} else {
					tiles[x + y * width] = Tile.STONE.getId();
				}
			}
		}
	}
	
	public void tick() {
		for(Entity e : entities) {//For loop to iterate through ListArray
			e.tick();
		}
		
		for(Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}
	}

	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0) {
			xOffset = 0;
		}
		if (xOffset > ((width << 3) - screen.width)) {
			xOffset = ((width << 3) - screen.width);
		}

		if (yOffset < 0) {
			yOffset = 0;
		}
		if (yOffset > ((height << 3) - screen.height)) {
			yOffset = ((height << 3) - screen.height);
		}
		
		if(screen.width > width * 8) {
			xOffset = (screen.width - (width * 8)) / 2 * -1;
		}
		
		if(screen.height > height * 8) {
			yOffset = (screen.height - (height * 8)) / 2 * -1;
		}

		screen.setOffset(xOffset, yOffset);

		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3 ) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3); // << 3 means "* 2^3"
			}
		}
	}
	
	public void renderEntities(Screen screen) { //I modified this
		
		/*for(Entity e : entities) {//For loop to iterate through ListArray
			try{
				if(e.isAlive) { //If entity is alive render it if not remove it from List
					e.render(screen);
				}
				//Sometime's I'll get a Concurrent Modification Exception because the entity the loop is currently on dies
				//So this'll stop the game from crashing (Hopefully)
			} catch (ConcurrentModificationException C) {
				System.out.println("Zombie number " + entities.indexOf(e) + "couldn't be modified");
			}
		}*/// Old Method (I got a lot of ConcurrentModificationException's using it)
		
		for(int i = 0; i < entities.size(); i++) { //Cycles through all the entities in this.entities
			if(entities.get(i).isAlive) {
				entities.get(i).render(screen);
			}
		}
	}
	

	public Tile getTile(int x, int y) { //If tile has x or y bigger or smaller than the level, render it as a void tile
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void addEntity(Entity ent) {//Add entity to list
		this.entities.add(ent);
	}
	
	public void removeEntity(Entity ent) { //My Function (removes ent's from List once dead)
		this.entities.remove(ent);
	}
}
