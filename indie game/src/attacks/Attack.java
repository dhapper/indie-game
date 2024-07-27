package attacks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.enemy.Enemy;
import entities.player.Player;
import gamestates.Overworld;
import main.Game;

import static utilz.Constants.Attack.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

public class Attack {

    private Player player;
    private int direction;

    private int aniTick, aniIndex;
    private int startBufferFrames;
    private int totalAniTick;

    private Area hitbox, updatedHitbox;
    private Shape initialEllipseHitbox, transformedRect;
    private int damage;

    // draw vars
    private int xPos, yPos;
    private int xDrawOffset, yDrawOffset, drawWidth, drawHeight;

    public Attack(Player player) {
        this.player = player;

        init();
    }

    private void init() {
        initBufferFrames();
        initialEllipseHitbox = new Rectangle2D.Float();
        hitbox = new Area();
        
        damage = 1;
    }

    public void draw(Graphics g, int xOffset, int yOffset) {
        // Centerize drawing - can store and improve efficiency
        xPos = (int) (player.getHitbox().getCenterX() - xOffset - Game.TILES_SIZE);	// not sure why - TILES_SIZE
        yPos = (int) (player.getHitbox().getCenterY() - yOffset - Game.TILES_SIZE);

        // seems wrong to set hitbox here
        calculateOffsetsAndDimensions();
        
        initialEllipseHitbox = new Ellipse2D.Float(xPos + xDrawOffset, yPos + yDrawOffset, drawWidth, drawHeight);
        updatedHitbox = new Area(initialEllipseHitbox);
        transformedRect = null;
        
        switch (direction) {
        case RIGHT:
        	subtractArea(xPos + xDrawOffset, yPos + yDrawOffset, 45, 10, 45, 13, false);
        	break;
        case LEFT:
        	subtractArea(xPos + xDrawOffset + initialEllipseHitbox.getBounds().width - 45 * Game.SCALE, yPos + yDrawOffset, 45, 10, 45, 13, true);
        	break;
        case UP_RIGHT:
        	subtractArea(xPos + xDrawOffset + initialEllipseHitbox.getBounds().width - 45 * Game.SCALE, yPos + yDrawOffset, 45, 10, 45, 14, true);
        	break;
        case UP_LEFT:
        	subtractArea(xPos + xDrawOffset, yPos + yDrawOffset, 45, 10, 45, 14, false);
        	break;
        case DOWN_RIGHT:
        	subtractArea(xPos + xDrawOffset + initialEllipseHitbox.getBounds().width - 45 * Game.SCALE, yPos + yDrawOffset, 45, 15, 35, -18, true);
        	break;
        case DOWN_LEFT:
        	subtractArea(xPos + xDrawOffset, yPos + yDrawOffset, 45, 15, 35, -18, false);
        	break;
        }
     
        hitbox = updatedHitbox;
        
        if(Overworld.SHOW_HITBOXES)
        	drawHitbox(g);
    }
    
    public void subtractArea(float x, float y, int width, int height, int rotation, int yShift, boolean mirrored) {
    	Rectangle2D.Float rect = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);
    	rotation = !mirrored ? rotation : rotation * -1;
        double theta = Math.toRadians(rotation);
        AffineTransform transform = new AffineTransform();
        float pivotX = !mirrored ? xPos + xDrawOffset : xPos + xDrawOffset + initialEllipseHitbox.getBounds().width;
        float pivotY = yPos + yDrawOffset;
        transform.rotate(theta, pivotX, pivotY);
        transform.translate(0, yShift * Game.SCALE);
        
        transformedRect = transform.createTransformedShape(rect);
        updatedHitbox.subtract(new Area(transformedRect));
    }

    private void drawHitbox(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.draw(hitbox);
    }

    private void calculateOffsetsAndDimensions() {
    	
    	
        switch (direction) {
            case LEFT:
            	drawWidth = (int) (30 * Game.SCALE);
                drawHeight = (int) (40 * Game.SCALE);
                xDrawOffset = (int) (0 * Game.SCALE);
                yDrawOffset = (int) (16 * Game.SCALE);
                break;
            case RIGHT:
                drawWidth = (int) (30 * Game.SCALE);
                drawHeight = (int) (40 * Game.SCALE);
                xDrawOffset = (int) (33 * Game.SCALE);
                yDrawOffset = (int) (16 * Game.SCALE);
                break;
            case UP_LEFT:
            	drawWidth = (int) (40 * Game.SCALE);
                drawHeight = (int) (33 * Game.SCALE);
                xDrawOffset = (int) (9 * Game.SCALE);
                yDrawOffset = (int) (-1 * Game.SCALE);
                break;
            case UP_RIGHT:
            	drawWidth = (int) (40 * Game.SCALE);
                drawHeight = (int) (33 * Game.SCALE);
                xDrawOffset = (int) (15 * Game.SCALE);
                yDrawOffset = (int) (-1 * Game.SCALE);
                break;
            case DOWN_LEFT:
            	drawWidth = (int) (35 * Game.SCALE);
                drawHeight = (int) (31 * Game.SCALE);
                xDrawOffset = (int) (9 * Game.SCALE);
                yDrawOffset = (int) (33 * Game.SCALE);
                break;
            case DOWN_RIGHT:
            	drawWidth = (int) (35 * Game.SCALE);
                drawHeight = (int) (31 * Game.SCALE);
                xDrawOffset = (int) (20 * Game.SCALE);
                yDrawOffset = (int) (33 * Game.SCALE);
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
        if (aniTick >= getSpriteDurations()[aniIndex]) { // check if sprite exceeds frame duration
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) { // check if animation is complete
                endAnimation();
            }
        }
    }

    private void bufferFrameTickManager() {
        totalAniTick++;
        if (totalAniTick > startBufferFrames)
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
        for (Enemy enemy : player.getEnemyData()) {
            offsetHitbox = new Rectangle2D.Float(enemy.getHitbox().x - xOffset, enemy.getHitbox().y - yOffset,
                    enemy.getHitbox().width, enemy.getHitbox().height);

            if (hitbox.intersects(offsetHitbox)) {
                if (!enemy.isAffectedDuringAttack()) {
                    enemy.damageEnemy(damage);
                    enemy.setAffectedDuringAttack(true);
                    AttackHelpMethods.KnockBack(enemy, 3, player.getHitbox(), enemy.getHitbox());
                }
                AttackHelpMethods.KnockBack(enemy, 3, player.getHitbox(), enemy.getHitbox());
            }
        }
    }

    public void initBufferFrames() {
        int[] durations = getSpriteDurations();
        startBufferFrames = 0;
        for (int duration : durations)
            startBufferFrames += duration;
        startBufferFrames -= BUFFER_FRAMES;
    }

    public int[] getSpriteDurations() {
        return GetSpriteDuration(ATTACKING);
    }

    public int getSpriteAmount() {
        return GetSpriteAmount(ATTACKING);
    }

    public void resetEnemyVars() {
        for (Entity e : player.getNpcData())
            if (e instanceof Enemy)
                ((Enemy) e).setAffectedDuringAttack(false);
    }

    public void loadDirection(int direction) {
        this.direction = direction;
    }
}
