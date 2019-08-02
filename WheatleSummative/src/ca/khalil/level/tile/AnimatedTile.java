/*
 * 
 * Class from tutorial
 * 
 * */

package ca.khalil.level.tile;

public class AnimatedTile extends BasicTile {
	
	private int[][] animationTileCoords;
	private int currAnimIndex;
	private long lastItterationTime; //in millis
	private int animSwitchDelay;

	public AnimatedTile(int id, int[][] animationCoords, int tileColour, int levelColour, int animSwitchDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour, levelColour);
		this.animationTileCoords = animationCoords;
		this.currAnimIndex = 0;
		this.lastItterationTime = System.currentTimeMillis();
		this.animSwitchDelay = animSwitchDelay;
	}
	
	public void tick() {
		if((System.currentTimeMillis() - lastItterationTime) >= (animSwitchDelay)) {
			lastItterationTime = System.currentTimeMillis();
			currAnimIndex = (currAnimIndex + 1) % animationTileCoords.length; //Don't do++ or else we'll go out of bounds
			this.tileId = (animationTileCoords[currAnimIndex][0] + (animationTileCoords[currAnimIndex][1] * 32)); //Updates the tileId
		}
	}
	
}
