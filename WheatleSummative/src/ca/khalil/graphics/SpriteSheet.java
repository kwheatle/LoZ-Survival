package ca.khalil.graphics;

//Class from tutorial

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public String path;
	public int width;
	public int height;
	
	public int[] pixels;
	
	public SpriteSheet(String path) { //Reads spritesheet from URL/Filepath
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path)); //Actual read line
		} catch (IOException e) { //Catch error
			e.printStackTrace();
		}
		
		if(image == null) {
			return;
		}
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for(int i = 0; i < pixels.length; i++) {//Setting the pixel data
			pixels[i] = (pixels[i]& 0xff) / 64; //Removing alpha channel. dividing by 64 because we need 4 colours
		}
	}
}
