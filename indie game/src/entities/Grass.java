package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.GraphicsHelp;
import utilz.LoadSave;

public class Grass extends Entity{

	BufferedImage sprites[];
	BufferedImage initialSprites[];
	int rotation = 0;
	
	public Grass(float x, float y, int width, int height) {
		super(x, y, width, height);
		
		init();
	}

	private void init() {
		initialSprites = new BufferedImage[2];
		initialSprites[0] = LoadSave.LoadImage("objects/grass.png").getSubimage(0, 0, 32, 32);
		initialSprites[1] = LoadSave.LoadImage("objects/grass.png").getSubimage(32, 0, 32, 32);
		
		
		sprites = new BufferedImage[2];
		sprites[0] = LoadSave.LoadImage("objects/grass.png").getSubimage(0, 0, 32, 32);
		sprites[1] = LoadSave.LoadImage("objects/grass.png").getSubimage(32, 0, 32, 32);
		
		//sprites[0] = GraphicsHelp.RotateImage(sprites[0], 0);
	}
	
	public void draw (Graphics g) {
		g.drawImage(sprites[0], (int) x, (int) y, width, height, null);
		g.drawImage(sprites[1], (int) x, (int) y, width, height, null);
	}
	
	
	int i = 0;
	public void update() {
		
		i++;
		if (i > 10) {
			rotation++;
			sprites[0] = GraphicsHelp.RotateImage(initialSprites[0], rotation);
			i = 0;
			System.out.println("hi");
		}
		
		
	}
	
	
}
