package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.Game;
import utilz.LoadSave;

public class Menu extends State implements Statemethods{
	
	public Menu(Game game) {
		super(game);
	}


	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(100, 100, 100, 100);
		g.drawString("Menu, press enter", 300, 300);
		
		g.drawImage(LoadSave.LoadImage("player/test.png").getSubimage(0, 32, 32, 32), 300, 300, Game.TILES_SIZE, Game.TILES_SIZE, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			GameState.state = GameState.OVERWORLD;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
