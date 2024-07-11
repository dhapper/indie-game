package spells;

import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Enemy;
import entities.Player;
import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;

public class WaterRing extends Spell implements SpellMethods{
	
	private float sizeIncrease = 0 * Game.SCALE;
	private float sizeIncreaseRate = 3 * Game.SCALE;
	private float startSize = 8 * Game.SCALE;
	
	public WaterRing(Player player) {
		super(player);
		
		loadAnimations();
		
		init();
	}
	
	public void init() {
		bounds = new Ellipse2D.Float();
	}
	
	public void spellEffect() {
		for(Enemy enemy : player.getEnemyData()) {
			if(player.getWaterRing().getBounds().intersects(enemy.getHitbox())) {
				
				enemy.setAffectedDuringSpell(true);
				
				float x1 = player.getHitbox().x;
				float y1 = player.getHitbox().y;
				float x2 = enemy.getHitbox().x;
				float y2 = enemy.getHitbox().y;
				
				float[] vector = HelpMethods.GetVector(x1, y1, x2, y2);
				
				enemy.updatePos(-vector[0], -vector[1]);
				
			}
		}
	}
	
	public void initSpellUseVars() {
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
	
	public void loadAnimations() {
		this.sprites = new BufferedImage[2];
		sprites[0] = LoadSave.LoadImage("spells/WATER_RING.png").getSubimage(64 * 0, 0, 64, 64);
		sprites[1] = LoadSave.LoadImage("spells/WATER_RING.png").getSubimage(64 * 1, 0, 64, 64);
		
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		g.drawImage(sprites[1], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(int) (startSize + sizeIncrease), (int) (startSize + sizeIncrease), null);
		
		g.drawImage(sprites[0], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
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
