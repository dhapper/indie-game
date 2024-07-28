package entities.enemy;

import static utilz.Constants.SlimeConstants.*; 

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.SpriteHelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Directions.*;

public class Slime extends Enemy{
	
	public Slime(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		init();
	}
	
	private void init() {
		// hitbox vars
		this.xDrawOffset = 5 * Game.SCALE;
		this.yDrawOffset = 11 * Game.SCALE;
		this.hitboxWidth = 20 * Game.SCALE;
		this.hitboxHeight = 15 * Game.SCALE;
		this.xCollisionBoxOffset = 0 * Game.SCALE;
		this.yCollisionBoxOffset = 8 * Game.SCALE;
		this.collisionBoxWidth = 20 * Game.SCALE;
		this.collisionBoxHeight = 7 * Game.SCALE;
		
		animations = SpriteHelpMethods.GetDefaultSizeSprites(LoadSave.LoadImage("npc/enemy/slime.png"), 6, 3);
		mirroredAnimations = SpriteHelpMethods.GetMirroredSprites(animations);
		
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
		currentSprite = facingRight ? animations[aniIndex][action] : mirroredAnimations[aniIndex][action];
		
		//currentSprite = DeathAnimation.DeathAnim(currentSprite);
		
		g.drawImage(currentSprite, (int) (hitbox.x - xDrawOffset) - xOffset, (int) (hitbox.y - yDrawOffset) - yOffset, width, height, null);
		
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
