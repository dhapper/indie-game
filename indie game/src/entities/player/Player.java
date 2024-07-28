package entities.player;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import attacks.Attack;
import attacks.Flamethrower;
import attacks.WaterRing;
import entities.Entity;
import entities.enemy.Enemy;
import gamestates.Overworld;
import graphics.GraphicsHelp;
import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.SpriteHelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Attack.*;
import static utilz.Constants.Directions.*;

public class Player extends Entity{
	
	// hitbox vars
	private float xDrawOffset = 24 * Game.SCALE;
	private float yDrawOffset = 18 * Game.SCALE;
	private float hitboxWidth = 16 * Game.SCALE;
	private float hitboxHeight = 29 * Game.SCALE;
	private float collisionBoxWidth = hitboxWidth;
	private float collisionBoxHeight = 5 * Game.SCALE;
	private float xCollisionBoxOffset = 0 * Game.SCALE;
	private float yCollisionBoxOffset = hitboxHeight - collisionBoxHeight;
	
	// movement/direction vars
	private boolean left, up, right, down;
	private float playerSpeed = 0.85f * Game.SCALE;
	private int movingDirection;
	private int xLocationOffset, yLocationOffset;
	private boolean facingForward = true;
	private boolean rolling = false;
	
	// status
	private boolean usingSpell = false;
	private boolean attacking = false;
		
	// spells
	private WaterRing waterRing;
	private Flamethrower flamethrower;
	private boolean castingSpellAnimation;
	
	// attacks
	private Attack updatedAttack = new Attack(this);
	private Attack[] attacks = {updatedAttack};
	private int currentAttackIndex = 0;
	private boolean inBufferFrames = false;
	private boolean nextAttackSelected = false;
	private int bufferFrameTickCounter = 0;
	
	// stats
	private int health = 5;
	private int maxHealth = health;
	private int mana = 7;
	private int maxMana = mana;
	
	// invincibility frames
	private boolean invincible;
	private int invincibilityFrames;
	private int totalInvincibilityFrames = 300;
	
	// objects
	private Book book;
	private HUD hud;
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		this.name = "Dhaarshan";
		
		init();
	}
	
	public void init() {
		
		BufferedImage layers[] = new BufferedImage[3];
		layers[0] = LoadSave.LoadImage("player/player.png");
		layers[1] = LoadSave.LoadImage("player/SLASH_EFFECT.png");
		layers[2] = LoadSave.LoadImage("player/WEAPON_1.png");
		
		BufferedImage spriteSheet = GraphicsHelp.BuildBufferedImage(layers);
		
		
		
		animations = SpriteHelpMethods.GetSpecificSizeSprites(spriteSheet, 11, 11, 64, 64);
		mirroredAnimations = SpriteHelpMethods.GetMirroredSprites(animations);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
		
		this.waterRing = new WaterRing(this);
		this.flamethrower = new Flamethrower(this);
		this.book = new Book(this, 0, 0, 10, 10);
		this.hud = new HUD(this);
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
		
		book.update();
		hud.update();
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		if(waterRing.isCastingSpell())
			waterRing.draw(g, xOffset, yOffset);
		
		if(flamethrower.isCastingSpell())
			flamethrower.draw(g, xOffset, yOffset);

		// book behind
		if(book.getBookY() <= hitbox.y - yOffset)
			book.draw(g);
		
		BufferedImage sprite = facingRight ? animations[aniIndex][action] : mirroredAnimations[aniIndex][action];
		if(invincible)
			sprite = GraphicsHelp.DecreaseAlpha(sprite, 1.0f*invincibilityFrames/totalInvincibilityFrames);

		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);	
		
		if(attacking) {
			attacks[currentAttackIndex].loadDirection(movingDirection);
			attacks[currentAttackIndex].draw(g, xOffset, yOffset);
		}
		
		// book infront
		if(book.getBookY() > hitbox.y - yOffset)
			book.draw(g);
		
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
		
		if(invincible) {
			invincibilityFrames++;
			if(invincibilityFrames > totalInvincibilityFrames) {
				invincible = false;
				invincibilityFrames = 0;
			}
		}
		
		if(bufferFrameTickCounter < BUFFER_FRAMES * 2 && inBufferFrames) {
			bufferFrameTickCounter++;
		}else {
			inBufferFrames = false;
			bufferFrameTickCounter = 0;
		}
		
		aniTick++;
		if(aniTick >= GetSpriteDuration(action)[aniIndex]) {	// check if sprite exceeds frame duration
			if(rolling)
				System.out.println(GetSpriteDuration(action)[aniIndex]);
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(action)) {	// check if animation is complete
				aniIndex = 0;
				//attacking = false;
				castingSpellAnimation = false;
				rolling = false;
				resetRollVars();
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
		
		
		if(moving) {
			//action = facingForward ? WALKING : WALKING_DOWN;
			if(ySpeed > 0)
				action = WALKING_DOWN;
			else if(ySpeed < 0)
				action = WALKING_UP;
			else
				action =  facingForward ? WALKING : WALKING_UP;
		}else
			action = facingForward ? IDLE : IDLE_B;
		
		
		if(castingSpellAnimation)
			action = CASTING_SPELL;
			//action = facingForward ? CASTING_SPELL : CASTING_SPELL_B;;
		
		// need to update this
		if(attacking) {
			if(lookingUp)
				action = ATTACKING_UP;
			else if(lookingDown)
				action = ATTACKING_DOWN;
			else {
				action = ATTACKING;
				facingForward = true;
			}
		}
		//action = facingForward ? CASTING_SPELL : CASTING_SPELL_B;
		
		if(rolling)
			action = facingForward ? ROLL : ROLL_B;
		
		if(startAni !=  action)
			resetAniTick();
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}
	
	boolean rollRight = false, rollLeft = false, rollUp = false, rollDown = false;
	int rollSpeed = 5;
	public void initRollVars() {
		
		if(left)
			rollLeft = true;
		
		if(right)
			rollRight = true;
		
		if(up)
			rollUp = true;
		
		if(down)
			rollDown = true;
	}
	
	public void resetRollVars() {
		rollLeft = false;
		rollRight = false;
		rollUp = false;
		rollDown = false;
	}

	public void roll() {
		if(rollLeft)
			xSpeed = -rollSpeed;
		
		if(rollRight)
			xSpeed = rollSpeed;
		
		if(rollUp)
			ySpeed = -rollSpeed;
		
		if(rollDown)
			ySpeed = rollSpeed;
		
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
		            updateFacingDirectionX(xSpeed, TOWARDS);
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
		            updateFacingDirectionY(ySpeed, TOWARDS);
	        	}
	        }
	    }
		
	    
	    updateMovingDirection(xSpeed, ySpeed);
	}
	
	private void updatePos() {
		
		if(rolling) {
			roll();
			return;
		}
		
		
		moving = false;
		
		if(action == CASTING_SPELL)
			return;
		
		if(!left && !right && !up && !down)
			return;
		
		xSpeed = 0;
		ySpeed = 0;
		
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
		            updateFacingDirectionX(xSpeed, TOWARDS);
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
		            updateFacingDirectionY(ySpeed, TOWARDS);
	        	}
	        }
	    }
		
	    
	    updateMovingDirection(xSpeed, ySpeed);
	}
	
	public void updateFacingDirectionY(float ySpeed, int mode) {
		if(mode == TOWARDS)
			facingForward = ySpeed > 0 ? true : false;
		else if(mode == AWAY)
			facingForward = ySpeed > 0 ? false : true;
	}
	
	private void updateMovingDirection(float xSpeed, float ySpeed) {
		
		if(ySpeed < 0) {
			if(xSpeed > 0)
				movingDirection = UP_RIGHT;
			else if(xSpeed < 0)
				movingDirection = UP_LEFT;
			else
				movingDirection = facingRight ? UP_RIGHT : UP_LEFT;
		}else if(ySpeed > 0) {
			if(xSpeed > 0)
				movingDirection = DOWN_RIGHT;
			else if(xSpeed < 0)
				movingDirection = DOWN_LEFT;
			else
				movingDirection = facingRight ? DOWN_RIGHT : DOWN_LEFT;
		}else {
			if(xSpeed > 0)
				movingDirection = RIGHT;
			else if(xSpeed < 0)
				movingDirection = LEFT;
		}
		
		
		lookingDown = false;
		lookingUp = false;
		if(ySpeed > 0) {
			lookingDown = true;
			lookingUp = false;
		}else if(ySpeed < 0) {
			lookingDown = false;
			lookingUp = true;
		}
		
	}
	boolean lookingDown = false, lookingUp = false;

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setLocationOffset(int xLocationOffset, int yLocationOffset) {
		this.xLocationOffset = xLocationOffset;
		this.yLocationOffset = yLocationOffset;
	}
	
	public void increaseAttackIndex() {
		currentAttackIndex = (currentAttackIndex + 1) % attacks.length;
	}
	
	public void decreaseHealth(int damage) {
		health -= damage;
		health = Math.max(health, 0);
		hud.updateHearts();
	}
	
	public void changeManaAmount(int manaDelta) {
		mana += manaDelta;
		mana = Math.max(mana, 0);
		mana = Math.min(mana, maxMana);
	}
	
	// getters and setters...
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
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
		
		if(usingSpell)
			castingSpellAnimation = true;
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

	public Book getBook() {
		return book;
	}
	
	public boolean isFacingForward() {
		return facingForward;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getMana() {
		return mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public HUD getHud() {
		return hud;
	}

	public boolean isRolling() {
		return rolling;
	}

	public void setRolling(boolean rolling) {
		this.rolling = rolling;
	}

}
