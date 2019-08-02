package ca.khalil.graphics;

/*
 * 
 * not my class 
 * A lot of the stuff in here is really confusing so i'm not sure exactly what's going on
 * this stuff is the math behind draw sprites to the screen
 * 
 * */


//i dont really understand what's happening in this class: its alot of math to draw to screen
public class Screen {

	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;

	public int[] pixels;

	public int xOffset = 0;
	public int yOffset = 0;

	public int width;
	public int height;

	public SpriteSheet sheet;

	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;

		pixels = new int[width * height];

	}
	
	public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
	
	public void drawRect(int xp, int yp, int width, int height, int colour, boolean fixed) {
		if(fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		
		for(int x = xp; x < xp + width; x++) {
			if(x < 0 | x >= this.width || yp >= this.height) continue;
			if(yp > 0) pixels[x + yp * this.width] = colour;
			if(yp + height >= this.height) continue;
			if(yp + height > 0) pixels[x + (yp + height) * this.width] = colour;
		}
		for(int y = yp; y < yp + height; y++) {
			if(xp >= this.width || y < 0 || y >= this.height) continue;
			if(xp > 0) pixels[xp + y * this.width] = colour;
			if(xp + width >= this.width) continue;
			if(xp + width > 0) pixels[(xp + width) + y * this.width] = colour;
		}
	}

	public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0; // & bit 'and' opperator  1  & 0 = 0, 1 & 1 = 1, 0 & 1 = 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0; // if > 0 is the same as if == 1

		int scaleMap = scale - 1;
		int xTile = tile % 32; 
		int yTile = tile / 32;//<- ^ getting tile co-ordinates
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width; //Getting offset per tile

		for (int y = 0; y < 8; y++) {
			int ySheet = y;
			if(mirrorY) 
				ySheet = 7 -y;
			
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3)/2);
					
			for (int x = 0; x < 8; x++) {
				
				int xSheet = x;
				if(mirrorX) xSheet = 7 -x;	
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3)/2);
				
				int col = (colour >> ( sheet.pixels[xSheet + ySheet
						* sheet.width + tileOffset] * 8)) & 255; // Getting the colour to populate the squares with
				if (col < 255) {
					for(int yScale = 0; yScale < scale; yScale++) {
						if (yPixel + yScale < 0 || yPixel + yScale >= height) {
							continue;
						}
						for(int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0 || xPixel + xScale >= width) {
								continue;
							}
							pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col; //Render colour everything inside the [] are for getting the x, y or the tile
						}					
					}
				}
			}
		}
	}
}
	
