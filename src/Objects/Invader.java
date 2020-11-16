package Objects;

public class Invader extends CollidingObject {

	// Direction of enemies can be static since they move together
	private static boolean invaderRight;
	
	public Invader(int x, int y, int width, int height, int speed) {
		
		super(x, y, width, height, speed);
		
		setVisibility(true);
		invaderRight = true;
	}
	
	// Enemies only need to move left or right
	public void move() {
		if (!invaderRight) {
			setX(getX() - getSpeed());
			return;
		}
		
		setX(getX() + getSpeed());
	}
	
	public static void setDirectionRight(boolean right) {
		invaderRight = right;
	}
	
	// Determines if the enemy should fire a shot
	public void shouldAttack(Shot shot) {
		double shotrandom = Math.random();
		
		if(shotrandom < 0.001 && getVisibility() && !shot.getVisibility()) {
			shot.setX(getX() + 11);
			shot.setY(getY());
			shot.setVisibility(true);
		}
	}
	
}
