package entities;

import java.awt.Color; 
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.enemy.Entity;
import entities.player.Player;
import main.Game;
import utilz.HelpMethods;

import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{

	// hitbox vars
	protected float xDrawOffset;
	protected float yDrawOffset;
	protected float hitboxWidth;
	protected float hitboxHeight;
	protected float xCollisionBoxOffset;
	protected float yCollisionBoxOffset;
	protected float collisionBoxWidth;
	protected float collisionBoxHeight;
	
	// stats
	protected int maxHealth = 5;
	protected int health = maxHealth;
	protected float speed;
	protected int damage = 1;
	
	// status
	protected boolean affectedDuringAttack = false;
	protected boolean affectedDuringSpell = false;
	protected boolean completedDeathAnimation = false;
	protected float xSpeed, ySpeed;
	
	// player reference
	protected Player player;
	protected Rectangle2D.Float playerHitbox;
	
	public Enemy(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		
	}
	
	public void update() {
		
	}
	
	public void damagePlayer() {
		if(HelpMethods.IsSpecificHitboxThere(this, playerHitbox, xSpeed, ySpeed)) {
			if(!player.isInvincible()) {
				player.decreaseHealth(damage);
				if(player.isAlive())
					player.setInvincible(true);
			}
		}
	}
	
	public void drawHealthBar(Graphics g, int xOffset, int yOffset) {
		
		float xPos = hitbox.x - xDrawOffset - xOffset;
		float yPos = hitbox.y - yDrawOffset - yOffset;
		
		float width = 16 * Game.SCALE;
		float height = 3 * Game.SCALE;
		float heightAboveEntity = 6 * Game.SCALE;
		float healthBarX = xPos + hitbox.width/2 - width/2; 
		float healthBarY = yPos - heightAboveEntity;
		
		health = Math.max(health, 0);
		
		g.setColor(Color.RED);
		g.fillRect((int) healthBarX, (int) healthBarY, (int) ((1.0 * health / maxHealth) * width), (int) height);
		g.setColor(Color.BLACK);
		g.drawRect((int) healthBarX, (int) healthBarY, (int) width, (int) height);
		
	}
	
	public boolean checkLineOfSight(Rectangle2D.Float player) {
		float playerCenterX = player.x + player.width/2;
		float playerCenterY = player.y + player.height/2;
		float enemyCenterX = hitbox.x + hitbox.width/2;
		float enemyCenterY = hitbox.y + hitbox.height/2;
		
		float distance = (float) Math.sqrt((Math.pow((playerCenterX - enemyCenterX), 2) + Math.pow((playerCenterY - enemyCenterY), 2)));
		
		if(distance < Game.TILES_SIZE * 4 && distance > Game.TILES_SIZE * 0.25)
			if(!affectedDuringSpell)
				return true;
		return false;
	}
	
	protected void updateAnimationTick(int spriteAmount, int duration) {
		aniTick++;
		if(aniTick >= duration) {	// check if sprite exceeds frame duration
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= spriteAmount)// check if animation is complete
				aniIndex = 0;
		}
	}
	
	public void moveTowardsPos(float speed, float[] vector, int mode) {
		xSpeed = speed * vector[0]/10;
		ySpeed = speed * vector[1]/10;
		updatePos(xSpeed, ySpeed, mode);
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
	
	public void checkIfAlive() {
		if(health <= 0)
			alive = false;
	}
	
	protected void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void loadPlayerHitbox(Rectangle2D.Float playerHitbox) {
		this.playerHitbox = playerHitbox;
	}
	
	public void loadPlayer(Player player) {
		this.player = player;
	}
	
	// getters and setters
	
	public void damageEnemy(int damage) {
		this.health -= damage;
	}

	public boolean isAffectedDuringSpell() {
		return affectedDuringSpell;
	}

	public void setAffectedDuringSpell(boolean damagedDuringSpell) {
		this.affectedDuringSpell = damagedDuringSpell;
	}

	public boolean isAffectedDuringAttack() {
		return affectedDuringAttack;
	}

	public void setAffectedDuringAttack(boolean affectedDuringAttack) {
		this.affectedDuringAttack = affectedDuringAttack;
	}
	
	

}
