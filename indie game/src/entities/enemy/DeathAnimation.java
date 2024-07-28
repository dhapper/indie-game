package entities.enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class DeathAnimation {

	ArrayList<BufferedImage> deathAnimations = new ArrayList<BufferedImage>();
	ArrayList<float[]> deathAnimationStats = new ArrayList<float[]>();
	Random random = new Random();
	
	public void update() {
		for(BufferedImage sprite : deathAnimations) {
			ArrayList<int[]> indices = getOpaquePixelIndices(sprite);
			if(indices.size() > 0)
				sprite = DeathAnim(sprite, indices);
			else if(indices.size() == 0) {
				sprite = DeathAnim2(sprite, getBlackPixelIndices(sprite));
			}
		}
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		for(int i = 0; i < getDeathAnimations().size(); i++) {
			float[] stats = getDeathAnimationStats().get(i);
			g.drawImage(getDeathAnimations().get(i), (int) (stats[0] - stats[4] - xOffset), (int) (stats[1] - stats[5] - yOffset), (int) stats[2], (int) stats[3], null);
		}
	}
	
	public BufferedImage DeathAnim(BufferedImage sprite, ArrayList<int[]> indices) {
        int index = random.nextInt(indices.size());
        int[] coordinates = indices.get(index);
        int x = coordinates[0];
        int y = coordinates[1];
        sprite.setRGB(x, y, 0xFF000000);
	    return sprite;
	}
	
	public BufferedImage DeathAnim2(BufferedImage sprite, ArrayList<int[]> indices) {
	    if (indices.isEmpty())
	        return sprite;
	    int index = random.nextInt(indices.size());
	    int[] coordinates = indices.get(index);
	    int x = coordinates[0];
	    int y = coordinates[1];
	    sprite.setRGB(x, y, 0x00000000);
	    return sprite;
	}
	
	public ArrayList<int[]> getOpaquePixelIndices(BufferedImage sprite) {
		ArrayList<int[]> indices = new ArrayList<>();
		int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    for(int j = 0; j < height; j++) {
	        for(int i = 0; i < width; i++) {
	            int pixel = sprite.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xff;
	            if(alpha != 0 && pixel != 0xFF000000)
	            	indices.add(new int[]{i, j});
	        }
	    }
	    return indices;
	}
	
	public ArrayList<int[]> getBlackPixelIndices(BufferedImage sprite) {
		ArrayList<int[]> indices = new ArrayList<>();
		int width = sprite.getWidth();
	    int height = sprite.getHeight();
	    for(int j = 0; j < height; j++) {
	        for(int i = 0; i < width; i++) {
	            int pixel = sprite.getRGB(i, j);
	            if(pixel == 0xFF000000)
	            	indices.add(new int[]{i, j});
	        }
	    }
	    return indices;
	}

	// getters and setters
	
	public ArrayList<BufferedImage> getDeathAnimations() {
		return deathAnimations;
	}

	public void setDeathAnimations(ArrayList<BufferedImage> deathAnimations) {
		this.deathAnimations = deathAnimations;
	}

	public ArrayList<float[]> getDeathAnimationStats() {
		return deathAnimationStats;
	}

	public void setDeathAnimationStats(ArrayList<float[]> deathAnimationStats) {
		this.deathAnimationStats = deathAnimationStats;
	}

	
}
