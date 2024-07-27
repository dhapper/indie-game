package utilz;

import java.awt.image.BufferedImage;

import graphics.GraphicsHelp;
import main.Game;

public class SpriteHelpMethods {

	public static BufferedImage[][] GetMirroredSprites(BufferedImage[][] sprites) {
	    int spritesWide = sprites.length;
	    int spritesLong = sprites[0].length;
	    BufferedImage[][] mirroredSprites = new BufferedImage[spritesWide][spritesLong];
	    for (int i = 0; i < spritesWide; i++)
	        for (int j = 0; j < spritesLong; j++)
	            mirroredSprites[i][j] = GraphicsHelp.MirrorImage(sprites[i][j]);
	    return mirroredSprites;
	}

	
	public static BufferedImage[][] GetDefaultSizeSprites(BufferedImage sheet, int spritesWide, int spritesLong){
		return GetSpecificSizeSprites(sheet, spritesWide, spritesLong, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
	}
	
	public static BufferedImage[][] GetSpecificSizeSprites(BufferedImage sheet, int spritesWide, int spritesLong, int width, int height) {
	    BufferedImage[][] sprites = new BufferedImage[spritesWide][spritesLong];
	    for(int i = 0; i < spritesWide; i++)
	        for(int j = 0; j < spritesLong; j++)
	            sprites[i][j] = sheet.getSubimage(i * width, j * height, width, height);
	    return sprites;
	}
	
}
