package entities;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Overworld;
import main.Game;
import spells.Flamethrower;
import spells.WaterRing;
import utilz.HelpMethods;

public class Player extends Entity{
	
	// hitbox vars
	private float xDrawOffset = 8.5f * Game.SCALE;
	private float yDrawOffset = 5 * Game.SCALE;
	private float hitboxWidth = 15 * Game.SCALE;
	private float hitboxHeight = 27 * Game.SCALE;
	private float xCollisionBoxOffset = 0 * Game.SCALE;
	private float yCollisionBoxOffset = 22 * Game.SCALE;
	private float collisionBoxWidth = 15 * Game.SCALE;
	private float collisionBoxHeight = 5 * Game.SCALE;
	
	// movement vars
	private boolean left, up, right, down;
	private float playerSpeed = 0.85f * Game.SCALE;
	
	// status
	private boolean usingSpell = false;
	private boolean attacking = false;
		
	// spells
	private WaterRing waterRing;
	private Flamethrower flamethrower;
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		this.name = "Dhaarshan";
		
		this.waterRing = new WaterRing(this);
		this.flamethrower = new Flamethrower(this);
		
		loadAnimations("player/redHood.png", 8, 9);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
	}
	
	public void update() {
		updateAnimationTick();
		updatePos();
		setAnimation();
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		if(waterRing.isCastingSpell())
			waterRing.draw(g, xOffset, yOffset);
		
		if(flamethrower.isCastingSpell())
			flamethrower.draw(g, xOffset, yOffset);

		BufferedImage sprite = facingRight ? animations[action][aniIndex] : mirroredAnimations[action][aniIndex];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	public void updatePlayerPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
		collisionBox.x = x + xCollisionBoxOffset;
		collisionBox.y = y + yCollisionBoxOffset;
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= GetSpriteDuration(action)[aniIndex]) {	// check if sprite exceeds frame duration
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(action)) {	// check if animation is complete
				aniIndex = 0;
				attacking = false;
			}
		}
		
		// seperate
		if(flamethrower.isCastingSpell())
			flamethrower.updateAnimationTick();
		
		if(waterRing.isCastingSpell())
			waterRing.updateAnimationTick();
	}
	
	private void setAnimation() {
		int startAni = action;
		
		if(moving)
			action = WALKING;
		else
			action = IDLE;
		
		if(attacking)
			action = ATTACKING;
		
		if(startAni !=  action)
			resetAniTick();
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		
		if(!left && !right && !up && !down)
			return;
		
		float xSpeed = 0, ySpeed = 0;
		
		if (left && !right)
			xSpeed = -playerSpeed;
		else if(right && !left)
			xSpeed = playerSpeed;
		
		if (up && !down)
			ySpeed = -playerSpeed;
		else if(down && !up)
			ySpeed = playerSpeed;
		
		// adjust speed for diagonal movement
		if(xSpeed != 0 && ySpeed != 0) {
	    	xSpeed *= Math.sqrt(0.5);
	    	ySpeed *= Math.sqrt(0.5);
	    }
		
		// horizontal movement
	    if (xSpeed != 0) {
	        if (HelpMethods.CanMoveHere(this, collisionBox.x + xSpeed, collisionBox.y, collisionBox.width, collisionBox.height, mapData)) {
	        	if(!HelpMethods.IsEntityThere(this, xSpeed, 0, characterData)) {
	        		hitbox.x += xSpeed;
		            collisionBox.x += xSpeed;
		            moving = true;
		            updateFacingDirection(xSpeed);
	        	}
	        }
	    }

	    // vertical movement
	    if (ySpeed != 0) {
	        if (HelpMethods.CanMoveHere(this, collisionBox.x, collisionBox.y + ySpeed, collisionBox.width, collisionBox.height, mapData)) {
	        	if(!HelpMethods.IsEntityThere(this, 0, ySpeed, characterData)) {
		            hitbox.y += ySpeed;
		            moving = true;
		            collisionBox.y += ySpeed;
	        	}
	        }
	    }
		
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	// getters and setters...
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUsingSpell() {
		return usingSpell;
	}

	public void setUsingSpell(boolean usingSpell) {
		this.usingSpell = usingSpell;
	}

	public WaterRing getWaterRing() {
		return waterRing;
	}

	public Flamethrower getFlamethrower() {
		return flamethrower;
	}

	public ArrayList<Entity> getNpcData(){
		return characterData;
	}
	
	public ArrayList<Enemy> getEnemyData(){
		return enemyData;
	}
	
}
