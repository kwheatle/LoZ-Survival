/*
 * 
 * Class from tutorial
 * 
 * */

package ca.khalil.graphics;

public class Font {

	public static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
			+ "0123456789.,:;'\"!?$%()-=+/      ";

	public static void render(String msg, Screen screen, int x, int y,
			int colour) {
		msg = msg.toUpperCase();

		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			if (charIndex >= 0) {
				screen.render(x + i * 8, y, charIndex + 30 * 32, colour, 0x00, 1);
			}
		}
	}
}
