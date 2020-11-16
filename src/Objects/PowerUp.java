package Objects;

public class PowerUp extends CollidingObject {

	public PowerUp(int x, int y, int width, int height, int speed) {
		super(x, y, width, height, speed);
	}
	
	// Drops a power-up from the top of the play area
	public void reset(int randomX) {
		setY(0);
		setX(randomX);
		setVisibility(true);
	}
}
