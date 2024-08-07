package mapeditor;

import java.awt.Color; 

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.GraphicsHelp;
import main.Game;
import utilz.Constants;
import utilz.LoadSave;

import static mapeditor.EditorConstants.MapEditorConstants.*;

public class Tile {

	private BufferedImage sprite;
	private int spriteIndex = -1;
	private boolean mirrored = false;
	
	
	public Tile() {
		clearTile();
	}

	public void changeSprite(int index) {
		this.spriteIndex = index;
		
		int rowIndex = index - index%(MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER).getWidth());
		
		if(MapEditor.LAYER == ANIMATED_SPRITES) {
			this.sprite = GraphicsHelp.DecreaseAlpha(MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER).getSprites()[rowIndex], 0.70f);
			this.spriteIndex = rowIndex;
		}else if(MapEditor.LAYER != 0)
			this.sprite = GraphicsHelp.DecreaseAlpha(MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER).getSprites()[index], 0.70f);
		else
			this.sprite = MapEditor.SPRITE_LAYERS.get(MapEditor.LAYER).getSprites()[index];

	}
	
	public void clearTile() {
		this.spriteIndex = -1;
		
		this.sprite = new BufferedImage(Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawRect(0, 0, sprite.getWidth(), sprite.getHeight());
        g2d.setColor(new Color(255, 255, 255, 10));
        g2d.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
        g2d.dispose();
		
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public int getSpriteIndex() {
		return spriteIndex;
	}

	public boolean isMirrored() {
		return mirrored;
	}

	public void setMirrored(boolean mirrored) {
		this.mirrored = mirrored;
	}
	
}
