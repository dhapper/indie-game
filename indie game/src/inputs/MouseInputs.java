package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener{

	private GamePanel gamePanel;
	
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case OVERWORLD:
			gamePanel.getGame().getOverworld().mouseMoved(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseClicked(e);
			break;
		case OVERWORLD:
			gamePanel.getGame().getOverworld().mouseClicked(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case OVERWORLD:
			gamePanel.getGame().getOverworld().mousePressed(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case OVERWORLD:
			gamePanel.getGame().getOverworld().mouseReleased(e);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
