package attacks;

import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import entities.Enemy;
import entities.enemy.Entity;
import entities.player.Player;
import gamestates.Overworld;
import graphics.GraphicsHelp;
import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.ImageHelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Attack.*;
import static utilz.Constants.Directions.*;

public class Attack {
	
	private Player player;
	private int direction;
	private BufferedImage[][] sprites;
	private String filePath;
	
	private int aniTick, aniIndex;
	private int startBufferFrames;
	private int totalAniTick;
	
	private Shape hitbox;
	private boolean mirrored;
	private int damage;
	
	// draw vars
	private int width, height;
	private int xPos, yPos;
	private int xDrawOffset, yDrawOffset, drawWidth, drawHeight;
	private int addedDistance = 5;
	
	// flipped when LEFT || RIGHT
	private int defaultDrawX;
	private int defaultDrawY;
	private int defaultDrawWidth;
	private int defaultDrawHeight;
	private int defaultDrawDiagonalX;
	private int defaultDrawDiagonalY;
	private int defaultDrawDiagonalWidth;
	private int defaultDrawDiagonalHeight;
	
	public Attack(Player player, boolean mirrored) {
		this.player = player;
		this.mirrored = mirrored;
		this.width = Game.TILES_SIZE * 2;	// ?
		this.height = Game.TILES_SIZE * 2;
		
		init();	
	}
	
	private void init() {
		initBufferFrames();
		initAttackSpecificVars();
		hitbox = new Rectangle2D.Float();
		
		sprites = ImageHelpMethods.GetDefaultSizeSprites(filePath, 3, 2);
		if(mirrored)
		    for (int i = 0; i < sprites.length; i++)
		        for (int j = 0; j < sprites[i].length; j++)
		            sprites[i][j] = GraphicsHelp.MirrorImage(sprites[i][j]);
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
	    // Centerize drawing - can store and improve efficiency
	    xPos = (int) (player.getHitbox().getCenterX() - xOffset - width / 2);
	    yPos = (int) (player.getHitbox().getCenterY() - yOffset - height / 2);

	    int xShift = (int) (player.getHitbox().width / 2 + addedDistance * Game.SCALE);
	    int yShift = (int) (player.getHitbox().height / 2 + addedDistance * Game.SCALE);

	    // Calculate the aspect ratio
	    double aspectRatio = 1;//(double) player.getHitbox().width / player.getHitbox().height;

	    // Adjust diagonal shifts to mimic an oval shape
	    int xDiagonalShift = (int) (xShift * Math.sqrt(1 - Math.pow(yShift / (double) yShift, 2) / aspectRatio) + addedDistance * 2 * Game.SCALE);
	    int yDiagonalShift = (int) (yShift * Math.sqrt(1 - Math.pow(xShift / (double) xShift, 2) * aspectRatio) + addedDistance * 2 * Game.SCALE);

	    switch (direction) {
	        case UP:
	           drawAttack(g, 0, -yShift, 0);
	            break;
	        case DOWN:
	            drawAttack(g, 0, yShift, 180);
	            break;
	        case LEFT:
	            drawAttack(g, -xShift, 0, 270);
	            break;
	        case RIGHT:
	            drawAttack(g, xShift, 0, 90);
	            break;
	        case UP_LEFT:
	            drawAttack(g, -xDiagonalShift, -yDiagonalShift, 270);
	            break;
	        case UP_RIGHT:
	            drawAttack(g, xDiagonalShift, -yDiagonalShift, 0);
	            break;
	        case DOWN_LEFT:
	            drawAttack(g, -xDiagonalShift, yDiagonalShift, 180);
	            break;
	        case DOWN_RIGHT:
	            drawAttack(g, xDiagonalShift, yDiagonalShift, 90);
	            break;
	    }

	    // seems wrong to set hitbox here
	    calculateOffsetsAndDimensions();
	 	hitbox = new Ellipse2D.Float(xPos + xDrawOffset, yPos + yDrawOffset, drawWidth, drawHeight);
	 		
	    if (Overworld.SHOW_HITBOXES)
	        drawHitbox(g);
	}

	
	private void drawAttack(Graphics g, int xShift, int yShift, int rotation) {
		int index = -1;
		int[] cardinal = {UP, DOWN, LEFT, RIGHT};
		int[] diagonal = {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
		BufferedImage currentFrame = null;;
		
		if(Arrays.stream(cardinal).anyMatch(i -> i == direction))
			index = 0;
		else if(Arrays.stream(diagonal).anyMatch(i -> i == direction)) {
			index = 1;
			if(mirrored)
				currentFrame = GraphicsHelp.RotateImage(sprites[aniIndex][index], 90);
		}
		
		xPos += xShift;
		yPos += yShift;
		if(currentFrame == null)
			currentFrame = sprites[aniIndex][index];
		currentFrame = GraphicsHelp.RotateImage(currentFrame, rotation);
		//g.drawImage(currentFrame, (int) (xPos + 16 * Game.SCALE), (int) (yPos + 16 * Game.SCALE), Game.TILES_SIZE, Game.TILES_SIZE, null);
	}
	
	private void drawHitbox(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.fill(hitbox);
	}
	
	// for managing seperate attacks, use switch
	public void initAttackSpecificVars() {
		defaultDrawWidth = 32;
		defaultDrawHeight = 16;
		defaultDrawX = 32 - defaultDrawWidth/2;
		defaultDrawY = 38 - defaultDrawHeight;
		
		defaultDrawDiagonalWidth = 32;
		defaultDrawDiagonalHeight = 12;
		defaultDrawDiagonalX = 44 - defaultDrawDiagonalWidth;
		defaultDrawDiagonalY = defaultDrawDiagonalHeight;
		
		damage = 1;
		
		filePath = "player/updated ae.png";
	}
	
	private void calculateOffsetsAndDimensions() {
	    switch (direction) {
	        case UP:
	            drawWidth = (int) (defaultDrawWidth * Game.SCALE);
	            drawHeight = (int) (defaultDrawHeight * Game.SCALE);
	            xDrawOffset = (int) (defaultDrawX * Game.SCALE);
	            yDrawOffset = (int) (defaultDrawY * Game.SCALE);
	            break;
	        case DOWN:
	            drawWidth = (int) (defaultDrawWidth * Game.SCALE);
	            drawHeight = (int) (defaultDrawHeight * Game.SCALE);
	            xDrawOffset = (int) (defaultDrawX * Game.SCALE);
	            yDrawOffset = (int) (height - defaultDrawY * Game.SCALE - drawHeight);
	            break;
	        case LEFT:
	            drawWidth = (int) (defaultDrawHeight * Game.SCALE);
	            drawHeight = (int) (defaultDrawWidth * Game.SCALE);
	            xDrawOffset = (int) (defaultDrawY * Game.SCALE);
	            yDrawOffset = (int) (defaultDrawX * Game.SCALE);
	            break;
	        case RIGHT:
	            drawWidth = (int) (defaultDrawHeight * Game.SCALE);
	            drawHeight = (int) (defaultDrawWidth * Game.SCALE);
	            xDrawOffset = (int) (width - defaultDrawY * Game.SCALE - drawWidth);
	            yDrawOffset = (int) (defaultDrawX * Game.SCALE);
	            break;
	        case UP_LEFT:
	        case UP_RIGHT:
	        case DOWN_LEFT:
	        case DOWN_RIGHT:
	            calculateDiagonalOffsetsAndDimensions();
	            break;
	    }
	}

	private void calculateDiagonalOffsetsAndDimensions() {
	    drawWidth = (int) (defaultDrawDiagonalWidth * Game.SCALE);
	    drawHeight = (int) (defaultDrawDiagonalHeight * Game.SCALE);

	    switch (direction) {
	        case UP_LEFT:
	            xDrawOffset = (int) (defaultDrawDiagonalX * Game.SCALE);
	            yDrawOffset = (int) (defaultDrawDiagonalY * Game.SCALE);
	            break;
	        case UP_RIGHT:
	            xDrawOffset = (int) (width - defaultDrawDiagonalX * Game.SCALE - drawWidth);
	            yDrawOffset = (int) (defaultDrawDiagonalY * Game.SCALE);
	            break;
	        case DOWN_LEFT:
	            xDrawOffset = (int) (defaultDrawDiagonalX * Game.SCALE);
	            yDrawOffset = (int) (height - defaultDrawDiagonalY * Game.SCALE - drawHeight);
	            break;
	        case DOWN_RIGHT:
	            xDrawOffset = (int) (width - defaultDrawDiagonalX * Game.SCALE - drawWidth);
	            yDrawOffset = (int) (height - defaultDrawDiagonalY * Game.SCALE - drawHeight);
	            break;
	    }
	}


	
	public void update() {
		attackEffect(player.getxLocationOffset(), player.getyLocationOffset());
		bufferFrameTickManager();
		updateAnimationTick();
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= getSpriteDurations()[aniIndex]) {	// check if sprite exceeds frame duration
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= getSpriteAmount()) {	// check if animation is complete
				endAnimation();
			}
		}
	}
	
	private void bufferFrameTickManager() {
		totalAniTick++;
		if(totalAniTick > startBufferFrames)
			player.setInBufferFrames(true);
	}
	
	private void endAnimation() {
		player.setAttacking(false);
		aniTick = 0;
		aniIndex = 0;
		totalAniTick = 0;
		
		resetEnemyVars();
	}
	
	public void attackEffect(int xOffset, int yOffset) {
		Rectangle2D.Float offsetHitbox = null;
		for(Enemy enemy : player.getEnemyData()) {
			offsetHitbox = new Rectangle2D.Float(enemy.getHitbox().x - xOffset, enemy.getHitbox().y - yOffset,
					enemy.getHitbox().width, enemy.getHitbox().height);
			
			if(hitbox.intersects(offsetHitbox)) {
				if(!enemy.isAffectedDuringAttack()) {
					enemy.damageEnemy(damage);
					enemy.setAffectedDuringAttack(true);
				}
				AttackHelpMethods.KnockBack(enemy, 3, player.getHitbox(), enemy.getHitbox());
			}
		}
	}
	
	public void initBufferFrames() {
		int[] durations = getSpriteDurations();
		startBufferFrames = 0;
		for(int duration : durations)
			startBufferFrames += duration;
		startBufferFrames -= BUFFER_FRAMES;
	}
	
	public int[] getSpriteDurations() {
		int durPerFrame = 29;
		return new int[] {durPerFrame, durPerFrame, durPerFrame};
	}
	
	public int getSpriteAmount() {
		return 3;
	}
	
	public void resetEnemyVars() {
		for(Entity e : player.getNpcData())
			if(e instanceof Enemy)
				((Enemy) e).setAffectedDuringAttack(false);	
	}
	
	public void loadDirection(int direction) {
		this.direction = direction;
	}
	
}
