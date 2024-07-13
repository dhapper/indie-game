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

import static utilz.Constants.Attack.*;
import static utilz.Constants.Directions.*;

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
	
	// movement/direction vars
	private boolean left, up, right, down;
	private float playerSpeed = 0.85f * Game.SCALE;
	private int movingDirection;
	private int mouseX, mouseY;
	private int xLocationOffset, yLocationOffset;
	
	// status
	private boolean usingSpell = false;
	private boolean attacking = false;
		
	// spells
	private WaterRing waterRing;
	private Flamethrower flamethrower;
	
	private Attack attack1 = new Attack(this, BASIC, false);
	private Attack attack2 = new Attack(this, BASIC, true);
	private Attack finisher = new Attack(this, FINISHER, false);
	private Attack[] attacks = {attack1, attack2, finisher};
	private int currentAttackIndex = 0;
	private boolean inBufferFrames = false;
	private boolean nextAttackSelected = false;
	
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
		
		if(!attacking)
			updatePos();
		
		setAnimation();
		
		if(attacking)
			attacks[currentAttackIndex].update();
		
		if(nextAttackSelected) {
			if(!attacking) {	// wait until current attack ends
				//set next attack
				increaseAttackIndex();
				attacking = true;
				nextAttackSelected = false;
			}
		}
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		if(waterRing.isCastingSpell())
			waterRing.draw(g, xOffset, yOffset);
		
		if(flamethrower.isCastingSpell())
			flamethrower.draw(g, xOffset, yOffset);

		BufferedImage sprite = facingRight ? animations[action][aniIndex] : mirroredAnimations[action][aniIndex];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		if(attacking) {
			attacks[currentAttackIndex].loadDirection(movingDirection);
			attacks[currentAttackIndex].draw(g, xOffset, yOffset);
		}
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
		
	}
	
	public void updatePlayerPos(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
		collisionBox.x = x + xCollisionBoxOffset;
		collisionBox.y = y + yCollisionBoxOffset;
	}
	
	
	int bufferFrameTickCounter = 0;
	private void updateAnimationTick() {
		
		if(bufferFrameTickCounter < BUFFER_FRAMES * 2 && inBufferFrames) {
			bufferFrameTickCounter++;
		}else {
			inBufferFrames = false;
			bufferFrameTickCounter = 0;
		}
		
		aniTick++;
		if(aniTick >= GetSpriteDuration(action)[aniIndex]) {	// check if sprite exceeds frame duration
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(action)) {	// check if animation is complete
				aniIndex = 0;
				//attacking = false;
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
		
		// need to update this
//		if(attacking)
//			action = ATTACKING;
		
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
		            updateFacingDirection(xSpeed, TOWARDS);
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
		
	    
	    updateMovingDirection(xSpeed, ySpeed);
	}
	
	private void updateMovingDirection(float xSpeed, float ySpeed) {
		if(ySpeed < 0) {
			if(xSpeed > 0)
				movingDirection = UP_RIGHT;
			else if(xSpeed < 0)
				movingDirection = UP_LEFT;
			else
				movingDirection = UP;
		}else if(ySpeed > 0) {
			if(xSpeed > 0)
				movingDirection = DOWN_RIGHT;
			else if(xSpeed < 0)
				movingDirection = DOWN_LEFT;
			else
				movingDirection = DOWN;
		}else {
			if(xSpeed > 0)
				movingDirection = RIGHT;
			else if(xSpeed < 0)
				movingDirection = LEFT;
		}
		
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setMouseVars(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}
	
	public void setLocationOffset(int xLocationOffset, int yLocationOffset) {
		this.xLocationOffset = xLocationOffset;
		this.yLocationOffset = yLocationOffset;
	}
	
	public void increaseAttackIndex() {
		currentAttackIndex = (currentAttackIndex + 1) % 3;
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

	public int getxLocationOffset() {
		return xLocationOffset;
	}

	public int getyLocationOffset() {
		return yLocationOffset;
	}

	public void setCurrentAttackIndex(int currentAttackIndex) {
		this.currentAttackIndex = currentAttackIndex;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public boolean isInBufferFrames() {
		return inBufferFrames;
	}

	public void setInBufferFrames(boolean inBufferFrames) {
		this.inBufferFrames = inBufferFrames;
	}

	public boolean isNextAttackSelected() {
		return nextAttackSelected;
	}

	public void setNextAttackSelected(boolean nextAttackSelected) {
		this.nextAttackSelected = nextAttackSelected;
	}

	
}
