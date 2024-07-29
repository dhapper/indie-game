package entities;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.enemy.Enemy;
import graphics.GraphicsHelp;
import utilz.HelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Directions.*;

public abstract class Entity {

	protected String name;
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;			// for entity hitbox
	protected Rectangle2D.Float collisionBox;	// for environment collision
	
	// hitbox vars
	protected float xDrawOffset;
	protected float yDrawOffset;
	protected float hitboxWidth;
	protected float hitboxHeight;
	protected float xCollisionBoxOffset;
	protected float yCollisionBoxOffset;
	protected float collisionBoxWidth;
	protected float collisionBoxHeight;
	
	// animation vars
	protected BufferedImage spriteSheet;
	protected BufferedImage[][] animations;
	protected BufferedImage[][] mirroredAnimations;
	protected int aniTick, aniIndex;
	protected BufferedImage currentSprite;
	
	// location data
	protected ArrayList<int[][]> mapData;
	protected ArrayList<Entity> characterData;
	protected ArrayList<Enemy> enemyData;
	
	// status
	protected boolean moving = false;
	protected boolean alive = true;
	protected int action;
	protected boolean facingRight = true;
	
	protected float xSpeed, ySpeed;
	
	public void moveTowardsPos(float speed, float[] vector, int mode) {
		xSpeed = speed * vector[0]/10;
		ySpeed = speed * vector[1]/10;
		updatePos(xSpeed, ySpeed, mode);
	}
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void updatePos(float xSpeed, float ySpeed, int mode) {
		
		moving = false;
		
		// horizontal movement
	    if (xSpeed != 0) {
	        if (HelpMethods.CanMoveHere(this, collisionBox.x + xSpeed, collisionBox.y, collisionBox.width, collisionBox.height, mapData)) {
	        	if(!HelpMethods.IsEntityThere(this, xSpeed, 0, characterData)) {
		            hitbox.x += xSpeed;
		            collisionBox.x += xSpeed;
		            moving = true;
		            updateFacingDirectionX(xSpeed, mode);
	        	}
	        }
	    }

	    // vertical movement
	    if (ySpeed != 0) {
	        if (HelpMethods.CanMoveHere(this, collisionBox.x, collisionBox.y + ySpeed, collisionBox.width, collisionBox.height, mapData)) {
	        	if(!HelpMethods.IsEntityThere(this, 0, ySpeed, characterData)) {
		            hitbox.y += ySpeed;
		            collisionBox.y += ySpeed;
		            moving = true;
	        	}
	        }
	    }
		
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
	
//	protected void loadAnimations(String filePath, int spritesWide, int spritesLong) {
//		spriteSheet = LoadSave.LoadImage(filePath);
//		animations = new BufferedImage[spritesLong][spritesWide];
//		mirroredAnimations = new BufferedImage[spritesLong][spritesWide];
//		
//		for(int j = 0; j < animations.length; j++) {
//			for(int i = 0; i < animations[j].length; i++) {
//				animations[j][i] = spriteSheet.getSubimage(i*32, j*32, 32, 32);
//				mirroredAnimations[j][i] = GraphicsHelp.MirrorImage(animations[j][i]);
//			}
//		}
//	}
	
//	protected void loadAnimations16(String filePath, int spritesWide, int spritesLong) {
//		spriteSheet = LoadSave.LoadImage(filePath);
//		animations = new BufferedImage[spritesLong][spritesWide];
//		mirroredAnimations = new BufferedImage[spritesLong][spritesWide];
//		
//		for(int j = 0; j < animations.length; j++) {
//			for(int i = 0; i < animations[j].length; i++) {
//				animations[j][i] = spriteSheet.getSubimage(i*16, j*16, 16, 16);
//				mirroredAnimations[j][i] = GraphicsHelp.MirrorImage(animations[j][i]);
//			}
//		}
//	}
//	
//	protected void loadAnimations32(String filePath, int spritesWide, int spritesLong) {
//		spriteSheet = LoadSave.LoadImage(filePath);
//		animations = new BufferedImage[spritesLong][spritesWide];
//		mirroredAnimations = new BufferedImage[spritesLong][spritesWide];
//		
//		for(int j = 0; j < animations.length; j++) {
//			for(int i = 0; i < animations[j].length; i++) {
//				animations[j][i] = spriteSheet.getSubimage(i*32, j*32, 32, 32);
//				mirroredAnimations[j][i] = GraphicsHelp.MirrorImage(animations[j][i]);
//			}
//		}
//	}
	
	public void loadMapData(ArrayList<int[][]> mapData, ArrayList<Entity> characterData, ArrayList<Enemy> enemyData) {
		this.mapData = mapData;
		this.characterData = characterData;
		this.enemyData = enemyData;
	}
	
	protected void updateFacingDirectionX(float xSpeed, int mode) {
		if(mode == TOWARDS)
			facingRight = xSpeed > 0 ? true : false;
		else if(mode == AWAY)
			facingRight = xSpeed > 0 ? false : true;
	}
	
	// getters and setters...
	
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
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public BufferedImage getCurrentSprite() {
		return currentSprite;
	}

	public float getxDrawOffset() {
		return xDrawOffset;
	}

	public float getyDrawOffset() {
		return yDrawOffset;
	}
	
}
