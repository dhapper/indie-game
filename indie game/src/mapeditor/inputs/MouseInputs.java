package mapeditor.inputs;

import java.awt.event.MouseEvent;



import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gamestates.GameState;
import main.Game;
import main.GamePanel;
import mapeditor.*;
import mapeditor.panel.*;

import static mapeditor.EditorConstants.MapEditorConstants.*;

public class MouseInputs implements MouseListener, MouseMotionListener{

	private Panel panel;
	
	public MouseInputs(Panel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		int x = e.getX() / Game.TILES_DEFAULT_SIZE;
		int y = e.getY() / Game.TILES_DEFAULT_SIZE;
		
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
		
		int x = e.getX() / Game.TILES_DEFAULT_SIZE;
		int y = e.getY() / Game.TILES_DEFAULT_SIZE;
		
		Tile[][] tiles = MapEditor.TILE_LAYERS.get(MapEditor.LAYER);
		int spriteIndex = tiles[y][x].getSpriteIndex();
		
		// check if mouse in bounds
		if(e.getX() < 0 || e.getX() > MapEditor.WIDTH * Game.TILES_DEFAULT_SIZE)
			return;
		else if(e.getY() < 0 || e.getY() > MapEditor.HEIGHT * Game.TILES_DEFAULT_SIZE)
			return;
		
		if (panel instanceof MapPanel) {
			if(ToolPanel.SELECTED_TOOL == ToolPanel.PAINT_BRUSH)
				tiles[y][x].changeSprite(SpritePanel.SELECTED_SPRITE);
		}else if(panel instanceof SpritePanel) {
			SpriteSheetData spriteSheetData = MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER);
			int index = x + (y * spriteSheetData.getWidth());
			if(index < spriteSheetData.getWidth() * spriteSheetData.getHeight())
				SpritePanel.setSelectedSprite(index);
			
		}
		
		// clear tile on left click
		if (e.getButton() == MouseEvent.BUTTON3) {
			tiles[y][x].clearTile();
	        return;
	    }
		
		// save map
		if(ToolPanel.SELECTED_TOOL == ToolPanel.SAVE){
			System.out.println("save");
			new SaveMapWindow();
			panel.repaint();
			return;
		}
		
		// mirror selected
		if (panel instanceof MapPanel) {
			if(ToolPanel.SELECTED_TOOL == ToolPanel.MIRROR){
				tiles[x][y].setMirrored(!tiles[x][y].isMirrored());
				tiles[x][y].changeSprite(tiles[x][y].getSpriteIndex());
			}
		}
		
		// set background
		if(MapEditor.LAYER == 0) {
			if(ToolPanel.SELECTED_TOOL == ToolPanel.SET_BG) {
				for(int i = 0; i < tiles.length; i ++) {
					for(int j = 0; j < tiles[0].length; j ++) {
						tiles[i][j].changeSprite(SpritePanel.SELECTED_SPRITE); 
					}
				}
			}
		}
		
		// fill selected tile
		if(ToolPanel.SELECTED_TOOL == ToolPanel.FILL) {
			for(int i = 0; i < tiles.length; i ++) {
				for(int j = 0; j < tiles[0].length; j ++) {
					if(tiles[i][j].getSpriteIndex() == spriteIndex) {
						tiles[i][j].changeSprite(SpritePanel.SELECTED_SPRITE);
					}
				}
			}
	    }
		
//		for(int i = 0; i < tiles.length; i ++) {
//			for(int j = 0; j < tiles[0].length; j ++) {
//				System.out.print(tiles[i][j].getSpriteIndex() + " ");
//			}
//			System.out.println();
//		}
		
		panel.getMapEditor().getToolPanel().updateTool(ToolPanel.PAINT_BRUSH);
		
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
