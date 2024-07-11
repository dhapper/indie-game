package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHelp {

	// alpha value from 0.0 - 1.0
	public static BufferedImage DecreaseAlpha(BufferedImage image, float alphaDecreaseFactor) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int argb = image.getRGB(x, y);
	            Color color = new Color(argb, true);
	            
	            int alpha = color.getAlpha();
	            int newAlpha = Math.min((int) (alpha * alphaDecreaseFactor), 255); // Ensure newAlpha is in range 0-255

	            Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
	            newImage.setRGB(x, y, newColor.getRGB());
	        }
	    }
	    return newImage;
	}
	

	public static BufferedImage MirrorImage(BufferedImage img) {
	    if (img != null) {
	        int width = img.getWidth();
	        int height = img.getHeight();
	        BufferedImage mirroredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
	        transform.translate(-width, 0);
	        Graphics2D g = mirroredImage.createGraphics();
	        g.drawImage(img, transform, null);
	        g.dispose();
	        return mirroredImage;
	    }
	    return null;
	}

}
