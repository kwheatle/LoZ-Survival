package ca.khalil.game.items;

//My own class: allow's for an inventory system to be added later on. Currently only used as base for weapon class

public abstract class Item {
	
	protected final int tileSize = 8;
	
	protected int unlockLevel;
	public boolean isUnlocked;
	public int id;
	public String type, name;
	
	public Item(String type) {
		this.type = type;
	}

	public abstract void tick();
	
	public abstract void unlock();
}
