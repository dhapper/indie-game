package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.Game;

public class Shaders {
	
	
	// old  shaders
	public static void draw(Graphics g, float playerX, float playerY) {
		for(int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
			for(int i = 0; i < Game.TILES_IN_WIDTH; i++) {
				
				int alphaX = (int) (Math.abs((int) playerX - i * Game.TILES_SIZE)/3.5);
				int alphaY = (int) (Math.abs((int) playerY - j * Game.TILES_SIZE)/3.5);
				int alpha = alphaX + alphaY;
				alpha = Math.min(255, alpha);
				
				Color caveColour = new Color(0, 0, 0, alpha);
				g.setColor(caveColour);
				
				g.fillRect(i * Game.TILES_SIZE, j * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
			}
		}
	}
	
	public static void caveShaders(Graphics g, float playerX, float playerY, int clearAreaDiameter, int additionalTransparentAreaDiameter) {
		
		float clearShapeScale = clearAreaDiameter * Game.SCALE;
		float transparentShapeScale = clearShapeScale + additionalTransparentAreaDiameter * Game.SCALE;
		
		Ellipse2D.Float clearShape = new Ellipse2D.Float(playerX - clearShapeScale/2, playerY - clearShapeScale/2, clearShapeScale, clearShapeScale);
		Ellipse2D.Float transparentShape = new Ellipse2D.Float(playerX - transparentShapeScale/2, playerY - transparentShapeScale/2, transparentShapeScale, transparentShapeScale); 
		Rectangle2D.Float darkShape = new Rectangle2D.Float(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		Graphics2D g2d = (Graphics2D) g;
		
		// Create Area objects from the shapes
	    Area darkArea = new Area(darkShape);
	    Area transparentArea = new Area(transparentShape);
	    Area clearArea = new Area(clearShape);
	    
	    // Subtract ellipse a from rectangle b
	    darkArea.subtract(transparentArea);
	    transparentArea.subtract(clearArea);
	    
	    // Fill the resulting area
	    g.setColor(new Color(0, 0, 0, 240));
	    g2d.fill(darkArea);
	    g.setColor(new Color(0, 0, 0, 100));
	    g2d.fill(transparentArea);
		
	}
	
}
