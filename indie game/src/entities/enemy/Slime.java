package entities.enemy;

import static utilz.Constants.SlimeConstants.*; 

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Enemy;
import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.ImageHelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Directions.*;

public class Slime extends Enemy{

	// hitbox vars
	private float xDrawOffset = 5 * Game.SCALE;
	private float yDrawOffset = 11 * Game.SCALE;
	private float hitboxWidth = 20 * Game.SCALE;
	private float hitboxHeight = 15 * Game.SCALE;
	private float xCollisionBoxOffset = 0 * Game.SCALE;
	private float yCollisionBoxOffset = 8 * Game.SCALE;
	private float collisionBoxWidth = 20 * Game.SCALE;
	private float collisionBoxHeight = 7 * Game.SCALE;
	
	public Slime(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		init();
	}
	
	private void init() {
		animations = ImageHelpMethods.GetDefaultSizeSprites("npc/enemy/slime.png", 6, 3);
		mirroredAnimations = ImageHelpMethods.GetMirroredSprites(animations);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
		
		speed = 2;
	}
	
	private void setAnimation() {
		int startAni = action;
		
		if(moving)
			action = RUNNING;
		else
			action = IDLE;
		
		if(startAni !=  action)
			resetAniTick();
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		BufferedImage sprite = facingRight ? animations[aniIndex][action] : mirroredAnimations[aniIndex][action];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		drawHealthBar(g, xOffset, yOffset);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	
	
	public void update() {
		updateAnimationTick(GetSpriteAmount(action), GetSpriteDuration(action));
		
		if(checkLineOfSight(playerHitbox))
			moveTowardsPos(speed, HelpMethods.GetVector(playerHitbox.x + playerHitbox.width/2, playerHitbox.y + playerHitbox.height/2, hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2), TOWARDS);
		
		damagePlayer();
		
		setAnimation();
		
		// maybe refactor
		moving = false;
	}
	
}
