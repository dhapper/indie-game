package attacks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.player.Player;
import gamestates.Overworld;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;

public class Flamethrower extends Spell implements SpellMethods{

	private BufferedImage[] growthSprites, matureSprites, endSprites;
	private int[][] durations;
	private int[] numOfFrames;
	private static final int GROWTH = 0, MATURE = 1, ENDING = 2;
	private int state = GROWTH;
	private int mouseX, mouseY;
	private Rectangle2D originalRect;
	
	private int xOffset, yOffset;
	
	public Flamethrower(Player player) {
		super(player);
		
		loadAnimations();
		
		init();
	}
	
	public void init() {
		originalRect = new Rectangle2D.Float();
		bounds = new Rectangle2D.Float();
		
		int standardDuration = 10;
		
		durations = new int[3][5];
		durations[GROWTH][0] = standardDuration;
		durations[GROWTH][1] = standardDuration;
		durations[GROWTH][2] = standardDuration;
		durations[GROWTH][3] = standardDuration;
		durations[GROWTH][4] = standardDuration;
		
		durations[MATURE][0] = standardDuration;
		durations[MATURE][1] = standardDuration;
		durations[MATURE][2] = standardDuration;
		durations[MATURE][3] = standardDuration;
		durations[MATURE][4] = standardDuration;
		
		durations[ENDING][0] = standardDuration;
		durations[ENDING][1] = standardDuration;
		durations[ENDING][2] = standardDuration;
		durations[ENDING][3] = standardDuration;
		
		numOfFrames = new int[3];
		numOfFrames[GROWTH] = 5;
		numOfFrames[MATURE] = 5;
		numOfFrames[ENDING] = 5;
	}
	
	public void spellEffect() {
		
		Rectangle2D.Float offsetHitbox = null;
		
		for(Enemy enemy : player.getEnemyData()) {
			offsetHitbox = new Rectangle2D.Float(enemy.getHitbox().x - xOffset, enemy.getHitbox().y - yOffset,
					enemy.getHitbox().width, enemy.getHitbox().height);
			
			if(player.getFlamethrower().getBounds().intersects(offsetHitbox)) {
				if(!enemy.isAffectedDuringSpell()) {
					enemy.damageEnemy(1);
					enemy.setAffectedDuringSpell(true);
				}
			}
		}
		
	}
	
	public void initSpellUseVars(int mouseX, int mouseY, int xOffset, int yOffset) {
		player.changeManaAmount(-manaUsage);
		player.setUsingSpell(true);
		player.getFlamethrower().setCastingSpell(true);
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		this.mouseX = (int) (mouseX + xOffset - player.getHitbox().x);
		this.mouseY = (int) (mouseY + yOffset - player.getHitbox().y);
	}

	@Override
	public void draw(Graphics g, int xOffset, int yOffset) {
	    Graphics2D g2d = (Graphics2D) g;
	    
	    BufferedImage img = null;
	    switch (state) {
        case GROWTH:
        	img = growthSprites[aniIndex];
            break;
        case MATURE:
        	img = matureSprites[aniIndex];
            break;
        case ENDING:
        	img = endSprites[aniIndex];
        	break;
	    }
	    
	    if(img == null)
	    	img = growthSprites[0];	// to avoid null pointer exception

	    float playerCenterX = player.getHitbox().x + player.getHitbox().width / 2 - xOffset;
	    float playerCenterY = player.getHitbox().y + player.getHitbox().height / 2 - yOffset;

	    float mouseXRelativeToPlayer = mouseX + player.getHitbox().x - xOffset;
	    float mouseYRelativeToPlayer = mouseY + player.getHitbox().y - yOffset;

	    float[] vector = HelpMethods.GetVector(playerCenterX, playerCenterY, mouseXRelativeToPlayer, mouseYRelativeToPlayer);
	    float angle = (float) Math.atan2(vector[1], vector[0]);
	    angle += Math.PI;

	    // Normalize the vector
	    float length = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
	    vector[0] /= length;
	    vector[1] /= length;

	    // move flame relative to center
	    float adjustmentFactor = 20.0f;
	    vector[0] *= adjustmentFactor * Game.SCALE;
	    vector[1] *= adjustmentFactor * Game.SCALE;

	    AffineTransform transform = new AffineTransform();
	    originalRect = new Rectangle2D.Float(-img.getWidth() / 2, -img.getHeight(), img.getWidth(), img.getHeight());

	    transform.translate(playerCenterX + vector[0], playerCenterY + vector[1]);
	    transform.rotate(angle + Math.PI / 2);
	    transform.translate(0, -img.getHeight() * Game.SCALE / 2);	// adjust based on image
	    transform.scale(Game.SCALE, Game.SCALE);	// scale

	    AffineTransform backup = g2d.getTransform();	// save state
	    g2d.setTransform(transform);

	    g2d.drawImage(img, -img.getWidth() / 2, -img.getHeight(), null);	// draw image centered at the bottom-center
	    bounds = transform.createTransformedShape(originalRect);

	    g2d.setTransform(backup);	// restore state
	    
	    if(Overworld.SHOW_HITBOXES)
			drawHitbox(g, xOffset, yOffset);
	}
	
	public void drawHitbox(Graphics g, int xOffset, int yOffset) {
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.RED);
			g2d.draw(bounds);
	}

	public void updateAnimationTick() {
		aniTick++;
	    if (aniTick >= durations[state][aniIndex]) { // check if sprite exceeds frame duration
	        aniTick = 0;
	        aniIndex++;
	        if (aniIndex >= numOfFrames[state]) { // check if animation is complete
	        	aniIndex = 0;
	        	
	            switch (state) {
	                case GROWTH:
	                    state = MATURE;
	                    break;
	                case MATURE:
	                    state = ENDING;
	                    break;
	                case ENDING:
	                    state = GROWTH;
	                    endAnimation();
	                    break;
	            }
	        }
	    }
	}

	private void endAnimation() {
		player.setUsingSpell(false);
		castingSpell = false;
		aniTick = 0;
		aniIndex = 0;
		
		// reset enemy isDamagedDuringSpell
		resetEnemyVars();
	}


	public void loadAnimations() {
		growthSprites = new BufferedImage[5];
		matureSprites = new BufferedImage[5];
		endSprites = new BufferedImage[5];
		
		for(int i = 0; i < 5; i++) {
			growthSprites[i] = LoadSave.LoadImage("spells/FLAMETHROWER.png").getSubimage(i * 32, 0, 32, 64);
			matureSprites[i] = LoadSave.LoadImage("spells/FLAMETHROWER.png").getSubimage(i * 32, 64, 32, 64);
			if(i != 4)
				endSprites[i] = LoadSave.LoadImage("spells/FLAMETHROWER.png").getSubimage(i * 32, 2 * 64, 32, 64);
		}
		
	}
	

}
