package Main;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Objects.Player;
import Objects.Shot;

public class SpaceInvadersListener extends KeyAdapter {
	
	private SpaceInvadersGame game;
	private Player player;
	private Shot shot;
	
	public SpaceInvadersListener(Player player, SpaceInvadersGame game, Shot shot) {
		this.player = player;
		this.game = game;
		this.shot = shot;
	}

	// Left arrow to move left, right arrow to move right, space bar to shoot, p to pause the game
	@Override
	public void keyPressed(KeyEvent e) {
		int key_id = e.getKeyCode();
		
		if(key_id == KeyEvent.VK_LEFT) {
			player.setDirectionLeft(true);
			player.setDirectionRight(false);
		}
		
		if(key_id == KeyEvent.VK_RIGHT) {
			player.setDirectionLeft(false);
			player.setDirectionRight(true);
		}
		
		if(key_id == KeyEvent.VK_SPACE) {
			player.shoot(shot);
		}
		
		if(key_id == KeyEvent.VK_P) {
			if(!game.getPause()) {
				game.setPause(true);
			} else {
				game.setPause(false);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key_id = e.getKeyCode();
		if(key_id == KeyEvent.VK_LEFT) {
			player.setDirectionLeft(false);
			player.setDirectionRight(false);
		}
		if(key_id == KeyEvent.VK_RIGHT) {
			player.setDirectionLeft(false);
			player.setDirectionRight(false);
		}
	}
}
