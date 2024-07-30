package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods{
	
	private MenuButton[] buttons = new MenuButton[2];
	
	public Menu(Game game) {
		super(game);
		
		buttons[0] = new MenuButton(Game.GAME_WIDTH/2, Game.GAME_HEIGHT/3, 0, GameState.OVERWORLD);	// to generate values, maybe make into constants
		
		
		buttons[0] = new MenuButton(Game.GAME_WIDTH/2 - buttons[0].getWidth()/2, Game.GAME_HEIGHT*2/5 - buttons[0].getHeight()/2, 0, GameState.OVERWORLD);
		buttons[1] = new MenuButton(Game.GAME_WIDTH/2 - buttons[0].getWidth()/2, Game.GAME_HEIGHT*3/5 - buttons[0].getHeight()/2, 1, GameState.OVERWORLD);
	}


	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawImage(LoadSave.LoadImage("menu bg.png"), 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
		int width = 500;
		int height = 250;
		g.drawImage(LoadSave.LoadImage("title.png"), Game.GAME_WIDTH/2 - width/2, Game.GAME_HEIGHT/5 - height/2, width, height, null);
		
		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if(isIn(e, mb)) {	
				mb.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGameState();
				break;
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for(MenuButton mb : buttons)
			mb.resetBools();
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton mb : buttons)
			mb.setMouseOver(false);
		
		for(MenuButton mb : buttons)
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
			break;
			}
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
