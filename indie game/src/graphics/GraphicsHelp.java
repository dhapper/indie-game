package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GraphicsHelp {

	public static BufferedImage BuildBufferedImage(BufferedImage[] imageLayers) {
		int width = imageLayers[0].getWidth();
		int height = imageLayers[0].getHeight();
		BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = finalImage.createGraphics();
		
		for(BufferedImage image : imageLayers) {
			g2d.drawImage(image, 0, 0, width, height, null);	
		}
		
		g2d.dispose();
		return finalImage;
	}
	
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

	public static BufferedImage RotateImage(BufferedImage img, double degrees) {
	    if (img != null) {
	        int width = img.getWidth();
	        int height = img.getHeight();
	        
	        // Calculate the new dimensions of the rotated image
	        double radians = Math.toRadians(degrees);
	        double sin = Math.abs(Math.sin(radians));
	        double cos = Math.abs(Math.cos(radians));
	        int newWidth = (int) Math.floor(width * cos + height * sin);
	        int newHeight = (int) Math.floor(height * cos + width * sin);

	        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = rotatedImage.createGraphics();

	        // Set rendering hints to avoid interpolation
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

	        // Translate and rotate the image
	        AffineTransform transform = new AffineTransform();
	        transform.translate(newWidth / 2.0, newHeight / 2.0);
	        transform.rotate(radians);
	        transform.translate(-width / 2.0, -height / 2.0);

	        g2d.setTransform(transform);
	        g2d.drawImage(img, 0, 0, null);
	        g2d.dispose();

	        return rotatedImage;
	    }
	    return null;
	}




	
//	public static BufferedImage RotateImage(BufferedImage img, double degrees) {
//        if (img != null) {
//            int width = img.getWidth();
//            int height = img.getHeight();
//            BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g = rotatedImage.createGraphics();
//            
//            AffineTransform transform = new AffineTransform();
//            transform.rotate(Math.toRadians(degrees), width / 2.0, height / 2.0);
//            g.setTransform(transform);
//            g.drawImage(img, 0, 0, null);
//            g.dispose();
//            
//            return rotatedImage;
//        }
//        return null;
//    }

}
