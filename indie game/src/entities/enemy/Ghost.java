package entities.enemy;

import java.awt.Graphics;

import java.awt.image.BufferedImage;

import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;
import utilz.SpriteHelpMethods;

import static utilz.Constants.Directions.*;

public class Ghost extends Enemy{
	
	private int aniTick = 0;	// only resets after each attack
	private int totalAttackTime = 500;
	private float[] currentAttackVector;
	private int speedDivisor = 20;
	private boolean attacking;
	
	public Ghost(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		init();
	}
	
	private void init() {
		// hitbox vars
		this.xDrawOffset = 5 * Game.SCALE;
		this.yDrawOffset = 0 * Game.SCALE;
		this.hitboxWidth = 20 * Game.SCALE;
		this.hitboxHeight = 32 * Game.SCALE;
		this.xCollisionBoxOffset = 0 * Game.SCALE;
		this.yCollisionBoxOffset = 20 * Game.SCALE;
		this.collisionBoxWidth = 20 * Game.SCALE;
		this.collisionBoxHeight = 12 * Game.SCALE;
		
		animations = SpriteHelpMethods.GetDefaultSizeSprites(LoadSave.LoadImage("npc/enemy/ghost.png"), 6, 1);
		mirroredAnimations = SpriteHelpMethods.GetMirroredSprites(animations);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
		
		speed = 2;
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		BufferedImage sprite = facingRight ? animations[aniIndex][0] : mirroredAnimations[aniIndex][0];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		drawHealthBar(g, xOffset, yOffset);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	public void updateAttack() {
		if(!attacking) {
			if(checkLineOfSight(playerHitbox)) {
				attacking = true;
				currentAttackVector = HelpMethods.GetVector(playerHitbox.x + playerHitbox.width/2, playerHitbox.y + playerHitbox.height/2, 
						hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2);
			}
		}else {
			if(aniTick > totalAttackTime) {
				//attacking = false;
				currentAttackVector = HelpMethods.GetVector(playerHitbox.x + playerHitbox.width/2, playerHitbox.y + playerHitbox.height/2, 
						hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2);
				aniTick = 0;
			}else {
				speed = aniTick < totalAttackTime/2 ? aniTick/speedDivisor : (totalAttackTime - aniTick)/speedDivisor;
				moveTowardsPos(speed, currentAttackVector, TOWARDS);
				aniTick++;
			}
		}
	}
	
	public void update() {
		updateAnimationTick(6, 40);
		updateAttack();
		damagePlayer();
		
		moving = false;
	}

}
