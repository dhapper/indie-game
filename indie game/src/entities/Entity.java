package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;			// for damage
	protected Rectangle2D.Float collisionBox;	// for environment collision
	
	protected String name;
	protected BufferedImage spriteSheet;
	protected BufferedImage[][] animations;
	
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void drawHitbox(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.RED);
		g.drawRect((int) hitbox.x - xOffset, (int) hitbox.y - yOffset, (int) hitbox.width, (int) hitbox.height);
		g.setColor(Color.WHITE);
		g.drawRect((int) collisionBox.x - xOffset, (int) collisionBox.y - yOffset, (int) collisionBox.width, (int) collisionBox.height);
	}
	
	protected void initHitbox(float x, float y, float width, float height) {
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}
	
	protected void initCollisionBox(float x, float y, float width, float height) {
		collisionBox = new Rectangle2D.Float(x, y, width, height);
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public Rectangle2D.Float getCollisionBox() {
		return collisionBox;
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(BufferedImage spriteSheet) {
		this.spriteSheet = spriteSheet;
	}
	
	
}
