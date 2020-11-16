package Main;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {

	public SpaceInvaders() {
		add(new SpaceInvadersGame());
		
		setResizable(false);
		pack();
		
		setTitle("Space Invaders");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		
		@Override
		public void run() {
			JFrame spaceinvaders = new SpaceInvaders();
			spaceinvaders.setVisible(true);
		}
		});
	}
}
