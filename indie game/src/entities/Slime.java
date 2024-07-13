package entities;

import static utilz.Constants.SlimeConstants.*; 

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
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
		
		loadAnimations("npc/enemy/slime.png", 6, 3);
		
		initHitbox(x, y, hitboxWidth, hitboxHeight);
		initCollisionBox(x + xCollisionBoxOffset, y + yCollisionBoxOffset, collisionBoxWidth, collisionBoxHeight);
	}
	
	private void init() {
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
		BufferedImage sprite = facingRight ? animations[action][aniIndex] : mirroredAnimations[action][aniIndex];
		g.drawImage(sprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
		drawHealthBar(g, xOffset, yOffset);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	
	
	public void update() {
		updateAnimationTick(GetSpriteAmount(action), GetSpriteDuration(action));
		
		if(checkLineOfSight(player))
			moveTowardsPos(speed, HelpMethods.GetVector(player.x + player.width/2, player.y + player.height/2, hitbox.x + hitbox.width/2, hitbox.y + hitbox.height/2), TOWARDS);
		
		setAnimation();
		
		// maybe refactor
		moving = false;
	}
	
}
