package gamestates;

import java.awt.event.MouseEvent;


import main.Game;
import ui.Button;

public class State {
	
	protected Game game;
	public State(Game game) {
		this.game = game;
	}
	
	public boolean isIn(MouseEvent e, Button b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
	
	public Game getGame() {
		return game;
	}

}
