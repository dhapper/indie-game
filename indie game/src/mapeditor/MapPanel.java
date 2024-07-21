package mapeditor;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import main.Game;

import static utilz.Constants.MapEditorConstants.*;

public class MapPanel extends Panel{

	public boolean test = true;
	
	public MapPanel() {
		
		setLayout(new GridLayout(MapEditor.HEIGHT, MapEditor.WIDTH));
        setPreferredSize(new Dimension(MapEditor.WIDTH * Game.TILES_DEFAULT_SIZE, MapEditor.HEIGHT * Game.TILES_DEFAULT_SIZE));
        
        //System.out.println(LAYER_ORDER);
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	 
	    
	    for(int layer : LAYER_ORDER) {
	    	for(int i = 0; i < MapEditor.TILE_LAYERS.get(layer).length; i++) {
		    	for(int j = 0; j < MapEditor.TILE_LAYERS.get(layer)[0].length; j++) {
		    		g.drawImage(MapEditor.TILE_LAYERS.get(layer)[i][j].getSprite(), j * Game.TILES_DEFAULT_SIZE, i * Game.TILES_DEFAULT_SIZE, null);
			    }
		    }
	    }
	    
	}
	
}
