package mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import main.Game;

import static utilz.Constants.MapEditorConstants.*;

public class SpritePanel extends Panel{

	public static int SELECTED_SPRITE = 0;
	
	public SpritePanel() {
		
		//addMouseListener(new MouseInputs((Panel) this));
		
		setLayout(new GridLayout(5, 10));
        setPreferredSize(new Dimension(Game.TILES_DEFAULT_SIZE * 6, 1000));
        
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	 
	    int index = 0;
	    SpriteSheetData spriteSheetData = MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER);
	    for(BufferedImage sprite : spriteSheetData.getSprites()) {
	    	g.drawImage(sprite, index % spriteSheetData.getWidth() * Game.TILES_DEFAULT_SIZE, index / spriteSheetData.getWidth() * Game.TILES_DEFAULT_SIZE, null);
	    	index++;
	    }
	    
	    g.setColor(Color.BLACK);
	    g.drawRect(Game.TILES_DEFAULT_SIZE * (SELECTED_SPRITE % spriteSheetData.getWidth()), Game.TILES_DEFAULT_SIZE * (SELECTED_SPRITE / spriteSheetData.getWidth()), Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
	    g.setColor(Color.WHITE);
	    g.drawRect(Game.TILES_DEFAULT_SIZE * (SELECTED_SPRITE % spriteSheetData.getWidth()) + 1, Game.TILES_DEFAULT_SIZE * (SELECTED_SPRITE / spriteSheetData.getWidth()) + 1, Game.TILES_DEFAULT_SIZE - 2, Game.TILES_DEFAULT_SIZE - 2);
	    
	    g.setColor(Color.BLACK);
	    g.drawString("Layer: " + MapEditor.LAYER, 10, 700);
	}
	
	public static void setSelectedSprite(int spriteChoice) {
		SpritePanel.SELECTED_SPRITE = spriteChoice;
	}
}
