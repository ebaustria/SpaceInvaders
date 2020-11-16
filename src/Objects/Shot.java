package Objects;

import Main.SpaceInvadersGame;

public class Shot extends CollidingObject {
	
	// Shot can be normal, explosive, or armor piercing
	private Ammo ammoType;
	
	private int quantity;

	public Shot(int x, int y, int width, int height, int speed) {
		super(x, y, width, height, speed);
		quantity = 0;
		ammoType = Ammo.NORMAL;
	}
	
	public void setAmmo(Ammo ammo) {
		ammoType = ammo;
	}
	
	public Ammo getAmmo() {
		return ammoType;
	}
	
	public void checkAmmo() {
		if (quantity < 0) {
			ammoType = Ammo.NORMAL;
		}
	}
	
	public void decrementAmmo() {
		quantity--;
	}
	
	// Give the player ammo if he/she gets a power-up
	public void reload() {
		quantity = 10;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	// Detect if a shot fired from the player hit an enemy
	public boolean detectUpwardCollision(int enemyX, int enemyY, boolean enemyVisibility) {
		
		if ((getX() + getWidth()) >= enemyX &&
			getX() <= enemyX + SpaceInvadersGame.PLAYER_WIDTH &&
			getY() <= enemyY + SpaceInvadersGame.PLAYER_WIDTH &&
			(getY() + getHeight()) >= enemyY &&
			enemyVisibility &&
			getVisibility()) {
			
			return true;
		} else {
			return false;
		}
	}
}
