package spells;

import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.GetSpriteDuration;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Player;
import utilz.LoadSave;

public class Flamethrower extends Spell implements SpellMethods{

	private BufferedImage[] growthSprites, matureSprites, endSprites;
	
	private BufferedImage[][] sprites = {growthSprites, matureSprites, endSprites};
	
	private int aniTick;
	private int aniIndex;
	private int[] durations;
	private int[] numOfFrames;
	
	private boolean growing = true, mature, end;
	
	public Flamethrower(Player player) {
		super(player);
		
		loadAnimations();
	}

	@Override
	public void draw(Graphics g, int xOffset, int yOffset) {
		if(growing)
			g.drawImage(growthSprites[aniIndex], (int) (player.getHitbox().x - xOffset), (int) (player.getHitbox().y - yOffset), 64, 128, null);
		else if(mature)
			g.drawImage(matureSprites[aniIndex], (int) (player.getHitbox().x - xOffset), (int) (player.getHitbox().y - yOffset), 64, 128, null);
		else if(end)
			g.drawImage(endSprites[aniIndex], (int) (player.getHitbox().x - xOffset), (int) (player.getHitbox().y - yOffset), 64, 128, null);
		
	}
	
	public void updateAnimationTick() {
		
		if(growing) {
			aniTick++;
			if(aniTick >= 20) {	// check if sprite exceeds frame duration
				aniTick = 0;
				aniIndex++;
				if(aniIndex >= 5) {	// check if animation is complete
					aniIndex = 0;
					
					growing = false;
					mature = true;
				}
			}	
		}
		
		if(mature) {
			aniTick++;
			if(aniTick >= 20) {	// check if sprite exceeds frame duration
				aniTick = 0;
				aniIndex++;
				if(aniIndex >= 5) {	// check if animation is complete
					aniIndex = 0;
					
					mature = false;
					end = true;
				}
			}	
		}
		
		if(end) {
			aniTick++;
			if(aniTick >= 20) {	// check if sprite exceeds frame duration
				aniTick = 0;
				aniIndex++;
				if(aniIndex >= 4) {	// check if animation is complete
					aniIndex = 0;
					
					end = false;
					
					endAnimation();
				}
			}	
		}
		
	}

	private void endAnimation() {
		player.setUsingSpell(false);
		castingSpell = false;
		aniTick = 0;
		aniIndex = 0;
		growing = true;
		
	}

	@Override
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
