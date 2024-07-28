package entities.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class DeathAnimation {

	public static BufferedImage DeathAnim(BufferedImage sprite) {
		    
	    ArrayList<int[]> nonTransparentPixelIndices = new ArrayList<>();
		    
	    int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    
	    for(int j = 0; j < height; j++) {
	        for(int i = 0; i < width; i++) {
	            
	            int pixel = sprite.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xff; // Extract the alpha component
	            
	            if(alpha != 0 && pixel != 0xFF000000) { // If alpha is not zero, the pixel is not transparent
	                nonTransparentPixelIndices.add(new int[]{i, j});
	            }
	        }
	    }
	    
	    Random random = new Random();
        
        int index = random.nextInt(nonTransparentPixelIndices.size());
        
        int[] coordinates = nonTransparentPixelIndices.get(index);
        int x = coordinates[0];
        int y = coordinates[1];
        
        // Set the pixel at (x, y) to black
        sprite.setRGB(x, y, 0xFF000000); // 0xFF000000 is the color black with full opacityRandom random = new Random();
	    
	    return sprite;
	}
	
	public static BufferedImage DeathAnim2(BufferedImage sprite) {
	    ArrayList<int[]> nonTransparentPixelIndices = new ArrayList<>();
	    
	    int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    
	    for (int j = 0; j < height; j++) {
	        for (int i = 0; i < width; i++) {
	            int pixel = sprite.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xff; // Extract the alpha component
	            
	            if (alpha != 0) { // If alpha is not zero, the pixel is not transparent
	                nonTransparentPixelIndices.add(new int[]{i, j});
	            }
	        }
	    }
	    
	    if (nonTransparentPixelIndices.isEmpty()) {
	        return sprite; // If there are no non-transparent pixels, return the sprite as is
	    }
	    
	    Random random = new Random();
	    int index = random.nextInt(nonTransparentPixelIndices.size());
	    
	    int[] coordinates = nonTransparentPixelIndices.get(index);
	    int x = coordinates[0];
	    int y = coordinates[1];
	    
	    // Set the pixel at (x, y) to fully transparent
	    sprite.setRGB(x, y, 0x00000000); // 0x00000000 is the color black with full transparency
	    
	    return sprite;
	}
	
	public static ArrayList<int[]> getOpaquePixelIndices(BufferedImage sprite) {
		
		ArrayList<int[]> indices = new ArrayList<>();
		
		int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    
	    for(int j = 0; j < height; j++) {
	        for(int i = 0; i < width; i++) {
	            
	            int pixel = sprite.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xff; // Extract the alpha component
	            
	            if(alpha != 0 && pixel != 0xFF000000) { // If alpha is not zero, the pixel is not transparent
	                indices.add(new int[]{i, j});
	            }
	        }
	    }
	    
	    return indices;
	}
	
	public static ArrayList<int[]> getBlackPixelIndices(BufferedImage sprite) {
		
		ArrayList<int[]> indices = new ArrayList<>();
		
		int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    
	    for(int j = 0; j < height; j++) {
	        for(int i = 0; i < width; i++) {
	            
	            int pixel = sprite.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xff; // Extract the alpha component
	            
	            if(alpha != 0 && pixel == 0xFF000000) { // If black opaque pixel
	                indices.add(new int[]{i, j});
	            }
	        }
	    }
	    
	    return indices;
	}

	
}
