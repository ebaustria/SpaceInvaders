package Objects;

public class Explosion extends CollidingObject {

	public Explosion(int x, int y, int width, int height, int speed) {
		super(x, y, width, height, speed);
	}
	
	// Increase the size of the explosion
	public void expand() {
		setWidth(getWidth() + 1);
		setHeight(getHeight() + 1);
	}
}
