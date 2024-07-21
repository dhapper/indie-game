package mapeditor;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import graphics.GraphicsHelp;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.MapEditorConstants.*;

public class SpriteSheets {
	
	public static SpriteSheetData GetSprites(String fileName) {
		
		String path = "tilesets/" + fileName;
		BufferedImage sheet = LoadSave.LoadImage(path);
		int width = sheet.getWidth() / Game.TILES_DEFAULT_SIZE;
		int height = sheet.getHeight() / Game.TILES_DEFAULT_SIZE;
		
		BufferedImage sprites[] = new BufferedImage[width * height];
		
		for(int i = 0; i < width * height; i++) {
			sprites[i] = sheet.getSubimage(i%width * Game.TILES_DEFAULT_SIZE,  i/width * Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
		}
		
		return new SpriteSheetData(sprites, width, height);
	}
}
