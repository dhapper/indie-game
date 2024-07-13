package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Overworld;
import graphics.GraphicsHelp;
import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.LoadSave;

import static utilz.Constants.Attack.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.MapEditorConstants.TILE_SIZE;

public class Attack {
	
	private Player player;
	private int attackType, direction;
	
	BufferedImage[] sprites;
	
	private int aniTick, aniIndex;
	
	private Rectangle2D.Float hitbox;
	
	private boolean mirrored;
	
	private int startBufferFrames;
	
	private int damage;
	
	public Attack(Player player, int attackType, boolean mirrored) {
		this.player = player;
		this.attackType = attackType;
		this.mirrored = mirrored;
		
		init();
		
		loadAnimations(attackType);
	}
	
	private void init() {
		hitbox = new Rectangle2D.Float();
		initBufferFrames();
		initDamage();
	}

	private void loadAnimations(int attackType) {
		
		String filePath = null;
		switch(attackType) {
		case BASIC:
			filePath = "player/regular attack.png";
			break;
		case FINISHER:
			filePath = "player/combo attack.png";
			break;
		}
		
		BufferedImage sheet = LoadSave.LoadImage(filePath);
		
		int frames = sheet.getWidth()/TILE_SIZE;
		
		sprites = new BufferedImage[frames];
		
		for(int i = 0; i < frames; i ++) {
			sprites[i] = sheet.getSubimage(TILE_SIZE * i, 0, TILE_SIZE, TILE_SIZE);
			if(mirrored)
				sprites[i] = GraphicsHelp.MirrorImage(sprites[i]);
		}
		
		
	}

	public void draw(Graphics g, int xOffset, int yOffset) {
		
		int width = Game.TILES_SIZE * 2;
		int height = Game.TILES_SIZE * 2;
		
		// centerize drawing - can store and improve eff
		int xPos = (int) (player.getHitbox().getCenterX() - xOffset - width/2);
		int yPos = (int) (player.getHitbox().getCenterY() - yOffset - height/2);
		
		BufferedImage currentFrame = sprites[aniIndex];
		
		int xShift = (int) (player.getHitbox().width/2 + 2 * Game.SCALE);
		int yShift = (int) (player.getHitbox().height/2 + 2 * Game.SCALE);
		
		// Calculate the aspect ratio
		double aspectRatio = (double) player.getHitbox().width / player.getHitbox().height;

		// Adjust diagonal shifts to mimic an oval shape
		int xDiagonalShift = (int) (xShift * Math.sqrt(1 - Math.pow(yShift / (double) yShift, 2) / aspectRatio));
		int yDiagonalShift = (int) (yShift * Math.sqrt(1 - Math.pow(xShift / (double) xShift, 2) * aspectRatio));
		
		switch(direction) {
			case UP:
				yPos -= yShift;	// move up
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case DOWN:
				yPos += yShift;	// move down
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 180);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case LEFT:
				xPos -= xShift;	// move left
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 270);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case RIGHT:
				xPos += xShift;	// move right
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 90);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
				
			case UP_LEFT:
				xPos -= xDiagonalShift;	// move left
				yPos -= yDiagonalShift;	// move up
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 315);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case UP_RIGHT:
				xPos += xDiagonalShift;	// move right
				yPos -= yDiagonalShift;	// move up
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 45);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case DOWN_LEFT:
				xPos -= xDiagonalShift;	// move left
				yPos += yDiagonalShift;	// move down
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 225);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
			case DOWN_RIGHT:
				xPos += xDiagonalShift;	// move right
				yPos += yDiagonalShift;	// move down
				currentFrame = GraphicsHelp.RotateImage(currentFrame, 135);
				g.drawImage(currentFrame, xPos, yPos, width, height, null);
				break;
		}
		
		// seems wrong to set hitbox here
		hitbox = new Rectangle2D.Float(xPos, yPos, width, height);
		
		if(Overworld.SHOW_HITBOXES)
			drawHitbox(g);
	}
	
	private void drawHitbox(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.draw(hitbox);
	}
	
	
	// seperte this method
	int totalAniTick;
	public void update() {
		
		attackEffect(player.getxLocationOffset(), player.getyLocationOffset());
		
		totalAniTick++;
		if(totalAniTick > startBufferFrames)
			player.setInBufferFrames(true);
		
		aniTick++;
		if(aniTick >= getSpriteDurations(attackType)[aniIndex]) {	// check if sprite exceeds frame duration
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= getSpriteAmount(attackType)) {	// check if animation is complete
				endAnimation();
			}
		}
		
			
	}
	
	private void endAnimation() {
		player.setAttacking(false);
		aniTick = 0;
		aniIndex = 0;
		
		totalAniTick = 0;
		
//		if(player.getCurrentAttackIndex() == 2)
//			player.setCurrentAttackIndex(0);
//		else
//			player.setCurrentAttackIndex(player.getCurrentAttackIndex() + 1);
		
		//player.setInBufferFrames(false);
		
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
				
				float x1 = (float) player.getHitbox().getCenterX();
				float y1 = (float) player.getHitbox().getCenterY();
				float x2 = (float) enemy.getHitbox().getCenterX();
				float y2 = (float) enemy.getHitbox().getCenterY();
				
				float[] vector = HelpMethods.GetVector(x1, y1, x2, y2);
				
				float[] adjustedVector = {-1 * vector[0], -1 * vector[1]};
				
				enemy.moveTowardsPos(2f * Game.SCALE, adjustedVector, Constants.Directions.AWAY);
			}
		}
		
//		for(Enemy enemy : player.getEnemyData()) {
//			if(player.getWaterRing().getBounds().intersects(enemy.getHitbox())) {
//				
//				enemy.setAffectedDuringSpell(true);
//				
//				float x1 = player.getHitbox().x;
//				float y1 = player.getHitbox().y;
//				float x2 = enemy.getHitbox().x;
//				float y2 = enemy.getHitbox().y;
//				
//				float[] vector = HelpMethods.GetVector(x1, y1, x2, y2);
//				
//				float[] adjustedVector = {-1 * vector[0], -1 * vector[1]}; 
//				
//				enemy.moveTowardsPos(5f * Game.SCALE, adjustedVector, Constants.Directions.AWAY);
//				
//			}
//		}
	}
	
	public void initBufferFrames() {
		int[] durations = getSpriteDurations(attackType);
		startBufferFrames = 0;
		for(int duration : durations)
			startBufferFrames += duration;
		startBufferFrames -= BUFFER_FRAMES;
	}
	
	private void initDamage() {
		switch(attackType) {
		case BASIC:
			this.damage = 1;
			break;
		case FINISHER:
			this.damage = 3;
			break;
		}
		
	}
	
	public int[] getSpriteDurations(int attackType) {
		
		int draw = 3;
		int swing = 5;
		int finDur = 5;	// finisher duration
		
		switch(attackType) {
		case BASIC:
			return new int[] {draw, draw, draw, draw, draw, draw, swing, swing, swing, swing, swing, swing, swing, swing, swing, swing, swing, swing};
		case FINISHER:
			return new int[] {finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur, finDur};
		}
		
		return null;
	}
	
	public int getSpriteAmount(int attackType) {
		switch(attackType) {
		case BASIC:
			return 17;
		case FINISHER:
			return 15;
		}
		
		return 0;
	}
	
	public void loadDirection(int direction) {
		this.direction = direction;
	}
	
	public static void printUnexpectedNumberErrorLocation(Class<?> clazz, String methodName) {
		// how to call
		//YourClass.printUnexpectedNumberErrorLocation(this.getClass(), "getSpriteDuration");
		
        System.out.println("Error @ " + clazz.getName() + " | " + methodName);
    }
	
	public void resetEnemyVars() {
		for(Entity e : player.getNpcData())
			if(e instanceof Enemy)
				((Enemy) e).setAffectedDuringAttack(false);	
	}
}
