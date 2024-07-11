package spells;

import java.awt.Graphics;

public interface SpellMethods {

	public void draw(Graphics g, int xOffset, int yOffset);
	
	public void drawHitbox(Graphics g, int xOffset, int yOffset);
	
	public void loadAnimations();
	
	public void spellEffect();
	
	public void init();
	
	public void updateAnimationTick();
}
