package entities;

import java.awt.Graphics;

import java.awt.image.BufferedImage;

import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;

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
	
	public Ghost(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		init();
		
		loadAnimations("npc/enemy/ghost.png", 6, 1);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
	}
	
	private void init() {
		speed = 2;
	}
	
	float targetX, targetY;
	boolean attacking;
	
	public void render(Graphics g, int xOffset, int yOffset) {
		BufferedImage sprite = facingRight ? animations[0][aniIndex] : mirroredAnimations[0][aniIndex];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		drawHealthBar(g, xOffset, yOffset);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	
	// temp vars
	int i = 0;
	float[] currentAttackVector;
	
	public void update() {
		updateAnimationTick(6, 40);
		
		
		if(!attacking) {
			if(checkLineOfSight(player)) {
				attacking = true;
				
				currentAttackVector = HelpMethods.GetVector(player.x + player.width/2, player.y + player.height/2, 
						hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2);
				
			}
		}else {
			if(i > 500) {
				//attacking = false;
				currentAttackVector = HelpMethods.GetVector(player.x + player.width/2, player.y + player.height/2, 
						hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2);
				i = 0;
			}else {
				
				speed = i < 250 ? i/20 : (500 - i)/20;
				
				moveTowardsPos(speed, currentAttackVector, TOWARDS);
				i++;
				System.out.println(i);
			}
		}
		// maybe refactor
		moving = false;
	}

}
