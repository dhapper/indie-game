package ui;

import java.awt.Graphics;

import gamestates.GameState;
import main.Game;

public class MenuButton extends Button{

	private GameState state;
	private int scale = 3;
	private int defaultWidth = 48;
	private int defaultHeight = 16;
	private int width = (int) (defaultWidth * Game.SCALE * scale);
	private int height = (int) (defaultHeight * Game.SCALE * scale);
	
	public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		
		loadImgs("buttons/menu buttons.png", defaultWidth, defaultHeight);
		initBounds(width, height);
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos, yPos, width, height, null);
	}
	
	public void applyGameState() {
		GameState.state = state;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
