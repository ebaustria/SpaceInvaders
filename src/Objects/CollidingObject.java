package Objects;

import java.awt.Color;
import java.awt.Graphics;

import Main.SpaceInvadersGame;

// Superclass of Player, Invader, PowerUp, Shot, and Explosion classes
public class CollidingObject {
	
	private int x;
	private int y;
	private int speed;
	private int width;
	private int height;
	
	private boolean isVisible;

	public CollidingObject(int x, int y, int width, int height, int speed) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
	}
	
	public void setVisibility(boolean visibility) {
		isVisible = visibility;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean getVisibility() {
		return isVisible;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	// Move an object up if it is not falling. Otherwise move it down
	public void move(boolean falling) {
		
		if (!falling) {
			if (getVisibility()) {
				setY(getY() - getSpeed());
			}
			
			if(getY() < 0 || !getVisibility()) {
				setVisibility(false);
			}
		}
		else {
			if(getVisibility()) {
				setY(getY() + getSpeed());
			}
			if(getY() >= SpaceInvadersGame.HEIGHT) {
				setVisibility(false);
			}
		}
		
	}

	public boolean detectSideCollision(int characterX, int characterY, boolean characterVisibility) {
		
		if ((y >= characterY && y <= (characterY + SpaceInvadersGame.PLAYER_WIDTH)) ||
			((y + height) >= characterY && (y + height) <= (characterY + SpaceInvadersGame.PLAYER_WIDTH))) {
			
			if ((x + width) >= characterX &&
				x <= (characterX + SpaceInvadersGame.PLAYER_WIDTH) &&
				characterVisibility &&
				isVisible) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectDownwardCollision(int characterX, int characterY) {
		
		if ((x + width) >= characterX &&
			x <= characterX + SpaceInvadersGame.PLAYER_WIDTH &&
			(y + height) >= characterY &&
			y <= characterY + SpaceInvadersGame.PLAYER_WIDTH &&
			isVisible) {
			
			return true;
		} else {
			
			return false;
		}
	}
	
	public void draw(Graphics graphics, Color color) {
		
		if (isVisible) {
			graphics.setColor(color);
			graphics.fillRect(x, y, width, height);
		}
	}
}
