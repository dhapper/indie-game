package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Overworld;
import main.Game;
import utilz.LoadSave;

public class Tree extends Entity {

	private int type;
	private BufferedImage sprite;
	
	float hitboxOffsetX = 20 * Game.SCALE;
	float hitboxOffsetY = 52 * Game.SCALE;
	float hitboxOffsetWidth = hitboxOffsetX * 2;
	float hitboxOffsetHeight = 56 * Game.SCALE;
	
	public Tree(int type, float x, float y, int width, int height) {
		super(x, y, width, height);
		
		this.type = type;
		
		init();
		initHitbox(x + hitboxOffsetX, y + hitboxOffsetY, width - hitboxOffsetWidth, height - hitboxOffsetHeight);
		initCollisionBox(x + hitboxOffsetX, y + hitboxOffsetY, width - hitboxOffsetWidth, height - hitboxOffsetHeight);
	}

	private void init() {
		
		sprite = LoadSave.LoadImage("tilesets/OBJECT_SPRITES.png").getSubimage(0, 64 * type, 64, 64);
		
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		g.drawImage(sprite, (int) x - xOffset, (int) y - yOffset, width, height, null);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	

}
