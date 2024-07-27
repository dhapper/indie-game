package entities.player;

import java.awt.Graphics;

import entities.Entity;
import graphics.GraphicsHelp;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;
import utilz.SpriteHelpMethods;

public class Book extends Entity{

	private Player player;
	private int playerX, playerY;
	
	private float bookX, bookY;
	private int state = 1;
	private int glowing = 1;
	private float xDrawOffset;
	private float yDrawOffset;
	private float speed = 6;
	
	public Book(Player player, float x, float y, int width, int height) {
		super(x, y, width, height);
		this.player = player;
		
		animations = SpriteHelpMethods.GetSpecificSizeSprites(LoadSave.LoadImage("player/book.png"), 2, 2, 16, 16);
	}

	public void update() {
		float[] speeds = moveTowardsPos(speed, bookX, bookY, playerX + xDrawOffset, playerY + yDrawOffset);
		
		bookX += speeds[0];
		bookY += speeds[1];
		
		updateDrawOffsets();
		updateBookState();
	}
	
	private void updateBookState() {
		state = player.isUsingSpell() ? 0 : 1;
	}

	public void updateDrawOffsets() {
		xDrawOffset = player.isFacingRight() ? -12 * Game.SCALE : 28 * Game.SCALE;
		yDrawOffset = player.isFacingForward() ? 6 * Game.SCALE :  -6 * Game.SCALE;
	}
	
	public void draw(Graphics g){
		if(bookX + 16/2 * Game.SCALE > playerX + 32/2 * Game.SCALE)
			g.drawImage(GraphicsHelp.MirrorImage(animations[glowing][state]), (int) bookX, (int) bookY, (int) (16 * Game.SCALE), (int) (16 * Game.SCALE), null);
		else
			g.drawImage(animations[glowing][state], (int) bookX, (int) bookY, (int) (16 * Game.SCALE), (int) (16 * Game.SCALE), null);
	}
	
	public float[] moveTowardsPos(float speed, float x, float y, float desiredX, float desiredY){
		float vector[] = HelpMethods.GetVector(x, y, desiredX, desiredY);
		float xSpeed = -speed *  vector[0]/10;
		float ySpeed = -speed * vector[1]/10;
		
		if(Math.abs(bookX - (playerX + xDrawOffset)) < 1 * Game.SCALE)
			xSpeed = 0;
		
		if(Math.abs(bookY - (playerY + yDrawOffset)) < 1 * Game.SCALE)
			ySpeed = 0;
		
		return new float[] {xSpeed, ySpeed};
	}
	
	public void updatePlayerPos(int x, int y) {
		playerX = x;
		playerY = y;
	}
	
	public void setBookPos(int x, int y) {
		bookX = x;
		bookY = y;
	}
	
	public float getBookY() {
		return bookY;
	}
	
}
