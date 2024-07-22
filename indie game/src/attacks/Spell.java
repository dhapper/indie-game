package attacks;

import java.awt.Shape;
import java.awt.image.BufferedImage;

import entities.Enemy;
import entities.enemy.Entity;
import entities.player.Player;

public abstract class Spell {

	protected Player player;
	protected String name;
	protected BufferedImage[][] sprites;
	protected boolean castingSpell;
	protected Shape bounds;
	protected int manaUsage = 1;
	protected int aniTick, aniIndex;
	
	public Spell(Player player) {
		this.player = player;
	}

	public void resetEnemyVars() {
		for(Entity e : player.getNpcData())
			if(e instanceof Enemy)
				((Enemy) e).setAffectedDuringSpell(false);	
	}

	public boolean isCastingSpell() {
		return castingSpell;
	}

	public void setCastingSpell(boolean castingSpell) {
		this.castingSpell = castingSpell;
	}
	
	public Shape getBounds() {
		return bounds;
	}
	
	public int getManaUsage() {
		return manaUsage;
	}
	
}