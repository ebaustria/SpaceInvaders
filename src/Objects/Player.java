package Objects;

import Main.SpaceInvadersGame;

public class Player extends CollidingObject {
	
	private boolean directionRight;
	private boolean directionLeft;
	
	public Player(int x, int y, int width, int height, int speed) {
		
		super(x, y, width, height, speed);
		
		setVisibility(true);
	}
	
	public void setDirectionRight(boolean right) {
		directionRight = right;
	}
	
	public void setDirectionLeft(boolean left) {
		directionLeft = left;
	}
	
	public void shoot(Shot shot) {
		
		if (!shot.getVisibility()) {
			shot.setX(getX() + 11);
			shot.setY(getY());
		}
		
		shot.setVisibility(true);
		
		if(shot.getAmmo() == Ammo.ARMORPIERCING && shot.getY() == getY()) {
			shot.decrementAmmo();
		}
		
		if(shot.getAmmo() == Ammo.EXPLOSIVE && shot.getY() == getY()) {
			shot.decrementAmmo();
		}
	}
	
	// Player can move left, right, or not at all
	public void move() {
		if(directionLeft && getX() > 0) {
			setX(getX() - getSpeed());
		}
		if(directionRight && getX() < SpaceInvadersGame.BORDER) {
			setX(getX() + getSpeed());
		}
	}
}
