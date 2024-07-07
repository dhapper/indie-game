package spells;

import java.awt.image.BufferedImage;

import entities.Player;

public abstract class Spell {

	
	protected Player player;
	
	protected String name;
	
	protected BufferedImage[] sprites;
	
	protected boolean castingSpell;
	
	
	public Spell(Player player) {
		this.player = player;
	}


	public boolean isCastingSpell() {
		return castingSpell;
	}


	public void setCastingSpell(boolean castingSpell) {
		this.castingSpell = castingSpell;
	}
	
	
	
}
