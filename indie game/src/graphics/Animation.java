package graphics;

import java.awt.image.BufferedImage;
import java.util.Random;
import utilz.LoadSave;

public class Animation {
    private BufferedImage[] frames;
    private int aniTick, aniIndex, aniSpeed, numOfFrames;
    private String fileName;

    private boolean isAnimating;
    private int stopDuration;
    private int currentStopTick;
    private Random random;

    public Animation(String fileName, int rowIndex, int numOfFrames, int aniSpeed) {
        this.aniTick = 0;
        this.aniIndex = 0;
        this.aniSpeed = aniSpeed;
        this.numOfFrames = numOfFrames;
        this.fileName = fileName;
        this.frames = new BufferedImage[numOfFrames];

        BufferedImage spriteSheet = LoadSave.LoadImage("tilesets/" + fileName);

        for (int i = 0; i < numOfFrames; i++) {
            this.frames[i] = spriteSheet.getSubimage(i * 32, rowIndex * 32, 32, 32);
        }
        
        this.random = new Random();
        this.isAnimating = false;
        newRandomStopDuration();
        this.currentStopTick = 0;
    }

    public void update() {
        if (isAnimating) {
            aniTick++;
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= numOfFrames) {
                    aniIndex = 0;
                    isAnimating = false;
                    newRandomStopDuration();
                }
            }
        } else {
        	//System.out.println(stopDuration + " " +currentStopTick);
            currentStopTick++;
            if (currentStopTick >= stopDuration) {
                currentStopTick = 0;
                isAnimating = true;
            }
        }
    }
    
    public void newRandomStopDuration() {
    	this.stopDuration = random.nextInt(10000) + 1000;
    }

    public BufferedImage getCurrentFrame() {
        return frames[aniIndex];
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}
