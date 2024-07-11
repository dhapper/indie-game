package mapeditor;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gamestates.GameState;
import main.GamePanel;

import static utilz.Constants.MapEditorConstants.*;

public class MouseInputs implements MouseListener, MouseMotionListener{

	private Panel panel;
	
	public MouseInputs(Panel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		int x = e.getX() / TILE_SIZE;
		int y = e.getY() / TILE_SIZE;
		
		Tile[][] tiles = MapEditor.TILE_LAYERS.get(MapEditor.LAYER);
		
		if (panel instanceof MapPanel) {
			
			if(x > MapEditor.WIDTH - 1 || x < 0)
				return;
			
			if(y > MapEditor.HEIGHT - 1 || y < 0)
				return;
			
			if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) { // Check if the right mouse button is being dragged
				tiles[y][x].clearTile();
				panel.repaint();
	            return;
	        }
			
            tiles[y][x].changeSprite(SpritePanel.SELECTED_SPRITE);
            
            panel.repaint();
        }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		int x = e.getX() / TILE_SIZE;
		int y = e.getY() / TILE_SIZE;
		
		Tile[][] tiles = MapEditor.TILE_LAYERS.get(MapEditor.LAYER);
		int spriteIndex = tiles[y][x].getSpriteIndex();
		
		// check if mouse in bounds
		if(e.getX() < 0 || e.getX() > MapEditor.WIDTH * TILE_SIZE)
			return;
		else if(e.getY() < 0 || e.getY() > MapEditor.HEIGHT * TILE_SIZE)
			return;
		
		// switch layer
		if(KeyboardInputs.keyHeld) {
			if (KeyboardInputs.key == '`' || KeyboardInputs.key == '0') {
				MapEditor.LAYER = 0;
			}else if(KeyboardInputs.key == '1') {
				MapEditor.LAYER = 1;
			}else if(KeyboardInputs.key == '2') {
				MapEditor.LAYER = 2;
			}else if(KeyboardInputs.key == '3') {
				MapEditor.LAYER = 3;
			}
			panel.repaint();
		}
		
		// switch selected sprite / draw tile on initial click
		if (panel instanceof MapPanel) {
			tiles[y][x].changeSprite(SpritePanel.SELECTED_SPRITE);
		}else if(panel instanceof SpritePanel) {
	    	//switch(MapEditor.LAYER) {
	    	//case GROUND_SPRITES:
			//case COLLISION_SPRITES:
				SpriteSheetData spriteSheetData = MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER);
				int index = x + (y * spriteSheetData.getWidth());
				if(index < spriteSheetData.getWidth() * spriteSheetData.getHeight())
					SpritePanel.setSelectedSprite(index);
				//break;
	    	//}
		}
		
		// clear tile on left click
		if (e.getButton() == MouseEvent.BUTTON3) {
			tiles[y][x].clearTile();
	        return;
	    }
		
		// save map
		if(KeyboardInputs.keyHeld) {
			if (KeyboardInputs.key == 's' || KeyboardInputs.key == 'S') {
				new SaveMapWindow();
			return;	
			}
		}
		
		// fill background
		if(KeyboardInputs.keyHeld) {
			if (KeyboardInputs.key == 'b' || KeyboardInputs.key == 'B') {
				for(int i = 0; i < tiles.length; i ++) {
					for(int j = 0; j < tiles[0].length; j ++) {
						tiles[i][j].changeSprite(SpritePanel.SELECTED_SPRITE); 
					}
				}
		    }
		}
		
		// fill selected tile
		if(KeyboardInputs.keyHeld) {
			if (KeyboardInputs.key == 'c' || KeyboardInputs.key == 'C') {
				for(int i = 0; i < tiles.length; i ++) {
					for(int j = 0; j < tiles[0].length; j ++) {
						if(tiles[i][j].getSpriteIndex() == spriteIndex)
							tiles[i][j].changeSprite(SpritePanel.SELECTED_SPRITE); 
					}
				}
		    }
		}
		
		panel.repaint();
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
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
