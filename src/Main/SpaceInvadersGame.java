package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import Objects.Ammo;
import Objects.Explosion;
import Objects.Invader;
import Objects.Player;
import Objects.PowerUp;
import Objects.Shot;

public class SpaceInvadersGame extends JPanel implements ActionListener {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 700;
	public static final int PLAYER_WIDTH = 25;
	public static final int SHOT_WIDTH = 2;
	public static final int SHOT_HEIGHT = 20;
	
	private final int PLAYER_SPEED = 6;
	public static final int BORDER = WIDTH - PLAYER_WIDTH;
	
	private int level;
	private int invaderX;
	private int invaderY;
	
	private boolean pause;
	private boolean running;
	
	private Timer t;
	
	private CopyOnWriteArrayList<ArrayList<Invader>> enemyArray;
	private ArrayList<Invader> row1;
	private CopyOnWriteArrayList<Shot> enemyShotList;
	
	private Player player;
	
	private PowerUp armorPiercing;
	private PowerUp explosive;
	
	private Shot shot;
	
	private Explosion explosion;
	
	public SpaceInvadersGame() {
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		setBackground(Color.WHITE);
		
		running = true;
		level = 1;
		
		t = new Timer(50, this);
		t.start();
		
		invaderX = 50;
		invaderY = 20;
		
		pause = false;
		
		enemyArray = new CopyOnWriteArrayList<ArrayList<Invader>>();
		row1 = new ArrayList<Invader>();
		enemyShotList = new CopyOnWriteArrayList<Shot>();
		
		for (int i = 0; i < 10; i++) {
			row1.add(new Invader(invaderX, invaderY, PLAYER_WIDTH, PLAYER_WIDTH, 2));
			invaderX += 40;
		}
		
		resetInvaderX();
		enemyArray.add(row1);
		
		for(int i = 0; i < 5; i++) {
			enemyShotList.add(new Shot(0, 0, SHOT_WIDTH, SHOT_HEIGHT, 15));
		}
		
		player = new Player(WIDTH/2, HEIGHT - 55, PLAYER_WIDTH, PLAYER_WIDTH, PLAYER_SPEED);
		shot = new Shot(player.getX(), HEIGHT - 55, SHOT_WIDTH, SHOT_HEIGHT, 20);
		
		armorPiercing = new PowerUp(0, 0, PLAYER_WIDTH, PLAYER_WIDTH, PLAYER_SPEED);
		explosive = new PowerUp(0, 0, PLAYER_WIDTH, PLAYER_WIDTH, PLAYER_SPEED);
		
		explosion = new Explosion(-1, -1, 0, 0, 0);
		
		addKeyListener(new SpaceInvadersListener(player, this, shot));
	}
	
	private void resetInvaderX() {
		invaderX = 50;
	}
	
	private void resetInvaderY() {
		invaderY = 20;
	}

	public void setPause(boolean is_paused) {
		pause = is_paused;
	}
	
	public boolean getPause() {
		return pause;
	}
	
	private void update() {
		player.move();
		shot.move(false);
		
		for(Shot s : enemyShotList) {
			s.move(true);
		}
		
		handleCollisions();
		levelUp();
		moveInvaders();
		iterateEnemyShots();
		armorPiercing.move(true);
		explosive.move(true);
		shot.checkAmmo();
		
		if(!running) {
			t.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(running && !pause) {
			update();
		}
		repaint();
	}
	
	// Iterate over all shots in the list and check if they should be fired
	private void iterateEnemyShots() {
		for (Shot s : enemyShotList) {
			for (ArrayList<Invader> row : enemyArray) {
				for(Invader a : row) {
					a.shouldAttack(s);
				}
			}
		}
	}
	
	// Check if the player hit an enemy
	private void checkInvaderDeath(Invader invader) {
		
		if (shot.detectUpwardCollision(invader.getX(), invader.getY(), invader.getVisibility()) ||
			shot.detectSideCollision(invader.getX(), invader.getY(), invader.getVisibility())) {
			
			if (shot.getAmmo() != Ammo.ARMORPIERCING) {
				shot.setVisibility(false);
			}
			
			if (shot.getAmmo() == Ammo.EXPLOSIVE) {
				explosion.setX(shot.getX() - PLAYER_WIDTH);
				explosion.setY(shot.getY() - 40);
				explosion.setVisibility(true);
			}
			
			invader.setVisibility(false);
		}
	}
	
	// Check if an explosion has killed an enemy
	private void checkExplosionKill() {
		
		explosion.expand();
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				if (explosion.detectDownwardCollision(a.getX(), a.getY())) {
					a.setVisibility(false);
				}
			}
		}
	}
	
	// Check if the player has collected and ammo power-up and change the ammo type, if necessary
	private void collectPowerUp(PowerUp powerUp) {
		
		if (powerUp.detectDownwardCollision(player.getX(), player.getY()) ||
			powerUp.detectSideCollision(player.getX(), player.getY(), true)) {
			
			powerUp.setVisibility(false);
			
			if (powerUp.equals(explosive)) {
				shot.setAmmo(Ammo.EXPLOSIVE);
			} else {
				shot.setAmmo(Ammo.ARMORPIERCING);
			}
			
			shot.reload();
		}
	}
	
	// Handles most of the collisions in the game
	private void handleCollisions() {
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				checkInvaderDeath(a);
				checkPlayerDeath(a);
			}
		}
		
		for (Shot s : enemyShotList) {
			
			if (s.detectDownwardCollision(player.getX(), player.getY())) {
				running = false;
			}
		}
		
		collectPowerUp(armorPiercing);
		collectPowerUp(explosive);
	}
	
	// Check if an enemy has collided with the player
	private void checkPlayerDeath(Invader invader) {
		if (invader.detectDownwardCollision(player.getX(), player.getY())) {
			running = false;
		}
	}

	/*
	 * If all enemies are invisible (dead), reset enemy locations, add a new row of enemies,
	 * increase the level, and randomly determine if a power-up should spawn
	 */
	private void levelUp() {
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				if (a.getVisibility()) {
					return;
				}
			}
		}
		
		resetInvaders();
		addRow();
		
		enemyShotList.add(new Shot(0, 0, SHOT_WIDTH, SHOT_HEIGHT, 15));
		level++;
		
		int powerupx = (int) (Math.random()*(WIDTH-PLAYER_WIDTH));
		double poweruprandom = Math.random();
		
		if (poweruprandom < 0.25) {
			armorPiercing.reset(powerupx);
		}
		
		if (poweruprandom >= 0.25 && poweruprandom < 0.5) {
			explosive.reset(powerupx);
		}
	}
	
	// Reset the positions of all the enemies
	private void resetInvaders() {
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				a.setX(invaderX);
				a.setY(invaderY);
				a.setVisibility(true);
				invaderX += 40;
			}
			invaderY += 40;
			resetInvaderX();
		}
		resetInvaderY();
	}
	
	// Add a new row of enemies
	private void addRow() {
		
		ArrayList<Invader> lastRow = enemyArray.get(enemyArray.size() - 1);
		ArrayList<Invader> newRow = new ArrayList<Invader>();
			
		for (int i = 0; i < 10; i++) {
			newRow.add(new Invader(
					lastRow.get(i).getX(),
					lastRow.get(i).getY() + 40,
					PLAYER_WIDTH,
					PLAYER_WIDTH,
					2));
		}
		
		enemyArray.add(newRow);
	}
	
	// Move the enemies down by a set number of points when they hit a wall
	private void advanceInvaders() {
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader i : row) {
				i.setY(i.getY() + 4);
			}
		}
	}
	
	// Check if the left-most enemy has hit the left wall
	private void checkLeftWall(Invader invader) {
		
		if (invader.getX() <= 0) {
			Invader.setDirectionRight(false);
			
			advanceInvaders();
			
			Invader.setDirectionRight(true);
		}
	}
	
	// Check if the right-most enemy has hit the right wall
	private void checkRightWall(Invader invader) {
		
		if (invader.getX() >= BORDER) {
			Invader.setDirectionRight(false);
			
			advanceInvaders();
		}
	}

	// Handles the movement of all the enemies
	private void moveInvaders() {
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
			a.move();
			}
		}
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				if (a.getVisibility()) {
					
					checkLeftWall(a);
					checkRightWall(a);
					
					if ((a.getY() + PLAYER_WIDTH) >= HEIGHT) {
						running = false;
					}
				}
			}
		}
	}
	
	// Draw the array of enemies
	public void drawInvaders(Graphics graphics) {
		
		for (ArrayList<Invader> row : enemyArray) {
			for (Invader a : row) {
				a.draw(graphics, Color.RED);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (running) {
			player.draw(g, Color.BLUE);
			shot.draw(g, Color.BLACK);
			
			drawInvaders(g);
			
			for (Shot s : enemyShotList) {
				s.draw(g, Color.BLACK);
			}
			
			armorPiercing.draw(g, Color.MAGENTA);
			explosive.draw(g, Color.GREEN);
			
			/*
			 * Draw an explosion until it reaches a certain size while checking if it kills any
			 * enemies
			 */
			if (explosion.getVisibility()) {
				
				for (int i = 0; i < 60; i++) {
					explosion.draw(g, Color.ORANGE);
					checkExplosionKill();
				}
				
				explosion.setWidth(0);
				explosion.setHeight(0);
				explosion.setVisibility(false);
			}
			
			if (shot.getQuantity() > 0) {
				Font ammofont = new Font("Calibri", Font.PLAIN, 14);
				g.setColor(Color.BLACK);
				g.setFont(ammofont);
				g.drawString("Ammo: " + shot.getQuantity(), WIDTH - 75, 50);
			}
			
			Font scorefont = new Font("Calibri", Font.PLAIN, 14);
			g.setColor(Color.BLACK);
			g.setFont(scorefont);
			g.drawString("Wave: " + level, WIDTH - 75, 25);
			
			if (pause) {
				Font p = new Font("Calibri", Font.BOLD, 18);
				g.setColor(Color.BLACK);
				g.setFont(p);
				g.drawString("PAUSED", WIDTH/2 - 50, HEIGHT/2);
			}
		} else {
			Font f = new Font("Calibri", Font.BOLD, 14);
			g.setColor(Color.BLACK);
			g.setFont(f);
			g.drawString("Game Over. Wave: " + level, WIDTH/2 - 50, HEIGHT/2);
		}
	}
}
