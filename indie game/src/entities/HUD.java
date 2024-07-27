package entities;

import java.awt.Graphics; 
import java.awt.image.BufferedImage;

import entities.player.Player;
import main.Game;
import utilz.LoadSave;
import utilz.SpriteHelpMethods;

public class HUD {

	private Player player;
	
	
	// hearts
	private int numOfHearts;
	private boolean drawPartialHearts;
	private BufferedImage playerHearts[][];
	
	// mana
	private BufferedImage manaStars[][];
	private int cooldownSprites = 21;
	//private int glimmerSprites = 28;
	private int cooldownState = 0;
	
	int manaAniTick = 0;
	
	public HUD(Player player) {
		this.player = player;
		
		manaStars = SpriteHelpMethods.GetDefaultSizeSprites(LoadSave.LoadImage("player/mana stars.png"), 29, 2);
		playerHearts = SpriteHelpMethods.GetDefaultSizeSprites(LoadSave.LoadImage("player/player hearts.png"), 4, 1);
		
		updateHearts();
	}
	
	public void draw(Graphics g) {
		drawPlayerHearts(g);
		drawManaStars(g);
	}
	
	public void drawPlayerHearts(Graphics g) {
		for(int i = 0; i < numOfHearts; i++) {
			if(i == numOfHearts - 1) {
				if(drawPartialHearts)
					g.drawImage(playerHearts[3-player.getHealth()%3][0], i * Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
				else
					g.drawImage(playerHearts[0][0], i * Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}else
				g.drawImage(playerHearts[0][0], i * Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
		}
	}
	
	public void drawManaStars(Graphics g) {
		for(int i = 0; i < player.getMana(); i++)
			g.drawImage(manaStars[cooldownSprites][0], (int) (Game.TILES_SIZE) * i, (int) (Game.TILES_SIZE), (int) (Game.TILES_SIZE), (int) (Game.TILES_SIZE), null);
		
		if(player.getMana() < player.getMaxMana())
			g.drawImage(manaStars[cooldownState][0], (int) (Game.TILES_SIZE) * (player.getMana()), (int) (Game.TILES_SIZE), (int) (Game.TILES_SIZE), (int) (Game.TILES_SIZE), null);
	}
	
	public void update() {
		if(player.getMana() < player.getMaxMana()) {
			manaAniTick++;
			if(manaAniTick >= 30) {
				cooldownState++;
				manaAniTick = 0;
				if(cooldownState > cooldownSprites) {
					cooldownState = 0;
					player.changeManaAmount(1);
				}
			}
		}
	}
	
	public void updateHearts() {
		int fullHearts = player.getHealth()/3;
		numOfHearts = fullHearts;
		if(fullHearts * 3 == player.getHealth()) {
			drawPartialHearts = false;
		}else {
			drawPartialHearts = true;
			numOfHearts++;
		}
	}
	
}
