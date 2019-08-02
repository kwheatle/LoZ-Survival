package ca.khalil.game.items;

import ca.khalil.game.Game;

//Class made by me to handle weapons and such

public class Weapon extends Item {

	public int damage;
	public float range;
	public int attackSpeed;
	private int xpPerLevel = 10, level = 1;
	public int xp = 0;
	public boolean canAttack = false;
	private boolean isRanged;
	private long lastAttackTime = 0;

	public Weapon(String name, int unlockLevel, int attackSpeed, int damage, float range, int id, boolean isRanged) {
		super("weapon");
		this.name = name;
		this.id = id;
		this.unlockLevel = unlockLevel;
		if(unlockLevel == 1 || unlockLevel == 0) { //if unlock level is 1 or 0 then the weapon starts unlocked
			this.isUnlocked = true;
		} else {
			this.isUnlocked = false;
		}
		this.attackSpeed = attackSpeed;
		this.damage = damage;
		//tileSize is 8. converts the inputed range into tiles. I.e 1 -> 1 tile or 8, 2 -> 2 tiles or 16 etc.
		this.range = range * tileSize;
		this.isRanged = isRanged;
	}//end of Weapon()

	public void tick() {
		
		if (System.currentTimeMillis() - lastAttackTime >= attackSpeed) { //used for weapons that auto-attack
			canAttack = true;
			lastAttackTime = System.currentTimeMillis();
		} else {
			canAttack = false;
		}
		
		//Level up system: weapons gain 1 extra damage/range per level. Each level requires 5 more zombies than the last to level up. level system gets disabled with hardmode
		if(xp >= xpPerLevel + (5 * (level - 1) ) && !Game.hardmode) { 
			xp = 0;//Reset xp
			level++; //Add 1 to level
			if(isRanged) { //Ranged Weapons gain 1/2 tile range per level
				range+= (0.5 * tileSize);
			} else {
				damage+=1; //non-ranged Weapons gain 1 damage per level
			}
		}//end of if
	}//end of tick()

	public void unlock() {
		if (Game.wave == unlockLevel && unlockLevel != -1) { //The dummyItems use unlockLevel = -1 so that they are always locked so you can't use them.
			isUnlocked = true;
		}
	}
	
	public int getWeaponLevel() {
		return level;
	}
}
