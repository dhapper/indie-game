package entities;

import java.awt.Graphics;

import main.Game;
import utilz.LoadSave;

public class Enemy extends Entity{

	// hitbox vars
	private float xDrawOffset = 7 * Game.SCALE;
	private float yDrawOffset = 5 * Game.SCALE;
	private float hitboxWidth = 15 * Game.SCALE;
	private float hitboxHeight = 27 * Game.SCALE;
	
	private float xCollisionBoxOffset = 0 * Game.SCALE;
	private float yCollisionBoxOffset = 22 * Game.SCALE;
	private float collisionBoxWidth = 15 * Game.SCALE;
	private float collisionBoxHeight = 5 * Game.SCALE;
	
	public Enemy(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		g.drawImage(LoadSave.LoadImage("player/redHood.png").getSubimage(0, 0, 32, 32), (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
	}

}
