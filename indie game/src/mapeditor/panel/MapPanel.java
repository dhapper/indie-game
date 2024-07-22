package mapeditor.panel;

import java.awt.Color;
import java.awt.Dimension;


import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import main.Game;
import mapeditor.MapEditor;

import static mapeditor.EditorConstants.MapEditorConstants.*;

public class MapPanel extends Panel{

	public boolean test = true;
	
	public MapPanel(MapEditor mapEditor) {
		super(mapEditor);
		
		setLayout(new GridLayout(MapEditor.HEIGHT, MapEditor.WIDTH));
        setPreferredSize(new Dimension(MapEditor.WIDTH * Game.TILES_DEFAULT_SIZE, MapEditor.HEIGHT * Game.TILES_DEFAULT_SIZE));
        
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	 
	    g.setColor(Color.LIGHT_GRAY);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    for(int layer : LAYER_ORDER) {
	    	for(int i = 0; i < MapEditor.TILE_LAYERS.get(layer).length; i++) {
		    	for(int j = 0; j < MapEditor.TILE_LAYERS.get(layer)[0].length; j++) {
		    		g.drawImage(MapEditor.TILE_LAYERS.get(layer)[i][j].getSprite(), j * Game.TILES_DEFAULT_SIZE, i * Game.TILES_DEFAULT_SIZE, null);
			    }
		    }
	    }
	    
	    g.setColor(Color.GRAY);
	    g.drawRect(0, 0, MapEditor.TILE_LAYERS.get(0)[0].length * Game.TILES_DEFAULT_SIZE, MapEditor.TILE_LAYERS.get(0).length * Game.TILES_DEFAULT_SIZE);
	    
	}
	
}
