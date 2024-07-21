package attacks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import entities.Enemy;
import entities.player.Player;
import gamestates.Overworld;
import main.Game;
import utilz.ImageHelpMethods;

public class WaterRing extends Spell implements SpellMethods{
	
	private float sizeIncrease = 0 * Game.SCALE;
	private float sizeIncreaseRate = 3 * Game.SCALE;
	private float startSize = 8 * Game.SCALE;
	
	public WaterRing(Player player) {
		super(player);	
		init();
	}
	
	public void init() {
		sprites = ImageHelpMethods.GetSpecificSizeSprites("spells/WATER_RING.png", 3, 1, 64, 64);
		bounds = new Ellipse2D.Float();
	}
	
	public void spellEffect() {
		for(Enemy enemy : player.getEnemyData()) {
			if(player.getWaterRing().getBounds().intersects(enemy.getHitbox())) {
				enemy.setAffectedDuringSpell(true);
				AttackHelpMethods.KnockBack(enemy, 5F, player.getHitbox(), enemy.getHitbox());
			}
		}
	}
	
	public void initSpellUseVars() {
		player.changeManaAmount(-manaUsage);
		player.setUsingSpell(true);
		castingSpell = true;
	}

	public void updateAnimationTick() {
		if(player.isUsingSpell()) {
			aniTick++;
			sizeIncrease += sizeIncreaseRate;
			
			if(aniTick >= 120) {
				aniTick = 0;
				player.setUsingSpell(false);
				castingSpell = false;
				sizeIncrease = 0;
				resetEnemyVars();
			}else if(aniTick >= 40)
				sizeIncrease -= sizeIncreaseRate;
		}
		
		bounds = new Ellipse2D.Float((int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2),
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2), startSize + sizeIncrease, startSize + sizeIncrease);
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		g.drawImage(sprites[1][0], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(int) (startSize + sizeIncrease), (int) (startSize + sizeIncrease), null);
		
		g.drawImage(sprites[0][0], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(int) (startSize + sizeIncrease), (int) (startSize + sizeIncrease), null);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}

	public void drawHitbox(Graphics g, int xOffset, int yOffset) {
		Ellipse2D.Float hitbox = new Ellipse2D.Float((player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(startSize + sizeIncrease), (int) (startSize + sizeIncrease));
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.draw(hitbox);
	}
	
}
