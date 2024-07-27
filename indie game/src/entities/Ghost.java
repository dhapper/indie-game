package entities;

import java.awt.Graphics;


import java.awt.image.BufferedImage;

import entities.Enemy;
import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;
import utilz.SpriteHelpMethods;

import static utilz.Constants.Directions.*;

public class Ghost extends Enemy{

	// hitbox vars
	private float xDrawOffset = 5 * Game.SCALE;
	private float yDrawOffset = 0 * Game.SCALE;
	private float hitboxWidth = 20 * Game.SCALE;
	private float hitboxHeight = 32 * Game.SCALE;
	private float xCollisionBoxOffset = 0 * Game.SCALE;
	private float yCollisionBoxOffset = 20 * Game.SCALE;
	private float collisionBoxWidth = 20 * Game.SCALE;
	private float collisionBoxHeight = 12 * Game.SCALE;
	
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
