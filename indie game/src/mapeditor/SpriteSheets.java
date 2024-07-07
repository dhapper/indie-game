package mapeditor;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import graphics.GraphicsHelp;
import utilz.LoadSave;

import static utilz.Constants.MapConstants.*;

public class SpriteSheets {
	
	public static SpriteSheetData GetSprites(String fileName) {
		
		String path = "tilesets/" + fileName;
		BufferedImage sheet = LoadSave.LoadImage(path);
		int width = sheet.getWidth() / TILE_SIZE;
		int height = sheet.getHeight() / TILE_SIZE;
		
		BufferedImage sprites[] = new BufferedImage[width * height];
		
		for(int i = 0; i < width * height; i++) {
			sprites[i] = sheet.getSubimage(i%width * TILE_SIZE,  i/width * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}
		
		return new SpriteSheetData(sprites, width, height);
	}
}
