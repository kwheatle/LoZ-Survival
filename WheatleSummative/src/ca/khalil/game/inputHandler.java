package ca.khalil.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

//Class from tutorial: Slightly modified by me

public class inputHandler implements KeyListener{
	
	public inputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	public class Key { //Defining the Key class
		private int numTimesPressed = 0;
		private boolean pressed = false;
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
			if(pressed) {
				numTimesPressed++;
			}
		}
	}
	public List<Key> key = new ArrayList<Key>();
	
	/*************** Setting up keys *****************/
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	//Below are the keys added by me
	public Key spacebar = new Key();
	public Key t = new Key();
	public Key q = new Key();
	

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true); //If the keys is pressed set the value to true
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false); //If the keys is NOT pressed set the value to false
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	public void toggleKey(int keyCode, boolean isPressed) { 
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			up.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			down.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			left.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			right.toggle(isPressed);
		}
		//Below are the keys added by me
		if(keyCode == KeyEvent.VK_SPACE) {
			spacebar.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_T) {
			t.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_Q) {
			q.toggle(isPressed);
		}
	}

}
