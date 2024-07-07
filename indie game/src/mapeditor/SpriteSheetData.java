package mapeditor;

import java.awt.image.BufferedImage;

public class SpriteSheetData {

	private BufferedImage[] sprites;
	private int width, height;
	
	public SpriteSheetData(BufferedImage[] sprites, int width, int height) {
		this.sprites = sprites;
		this.width = width;
		this.height = height;
		
	}

	public BufferedImage[] getSprites() {
		return sprites;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
