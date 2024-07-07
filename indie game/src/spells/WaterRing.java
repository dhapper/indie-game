package spells;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import entities.Player;
import main.Game;
import utilz.LoadSave;

public class WaterRing extends Spell{
	
	private int spellAniTick;
//	private int spellAniIndex;
	private float sizeIncrease = 0 * Game.SCALE;
	private float sizeIncreaseRate = 3 * Game.SCALE;
	private float startSize = 8 * Game.SCALE;
	private Ellipse2D.Float boundary;
	
	public WaterRing(Player player) {
		super(player);
		
		loadAnimations();
		
		
		init();
	}
	
	private void init() {
		boundary = new Ellipse2D.Float();
		
	}

	public void updateAnimationTick() {
		if(player.isUsingSpell()) {
			spellAniTick++;
			sizeIncrease += sizeIncreaseRate;
			
			if(spellAniTick >= 120) {
				spellAniTick = 0;
				player.setUsingSpell(false);
				castingSpell = false;
				sizeIncrease = 0;
			}else if(spellAniTick >= 40)
				sizeIncrease -= sizeIncreaseRate;
		}
		
		boundary = new Ellipse2D.Float((int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2),
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2), startSize + sizeIncrease, startSize + sizeIncrease);
	}
	
	private void loadAnimations() {
		this.sprites = new BufferedImage[2];
		sprites[0] = LoadSave.LoadImage("spells/WATER_RING.png").getSubimage(64 * 0, 0, 64, 64);
		sprites[1] = LoadSave.LoadImage("spells/WATER_RING.png").getSubimage(64 * 1, 0, 64, 64);
		
	}

	public void drawBeforePlayer(Graphics g, int xOffset, int yOffset) {
		g.drawImage(sprites[1], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(int) (startSize + sizeIncrease), (int) (startSize + sizeIncrease), null);
	}
	
	public void drawAfterPlayer(Graphics g, int xOffset, int yOffset) {
		g.drawImage(sprites[0], (int) (player.getHitbox().x + player.getHitbox().width/2 - (startSize + sizeIncrease)/2) - xOffset,
				(int) (player.getHitbox().y + player.getHitbox().height/2 - (startSize + sizeIncrease)/2) - yOffset,
				(int) (startSize + sizeIncrease), (int) (startSize + sizeIncrease), null);
	}
	
	
	public Ellipse2D.Float getBoundary() {
		return boundary;
	}

	public void setBoundary(Ellipse2D.Float waterRing) {
		this.boundary = waterRing;
	}
}
