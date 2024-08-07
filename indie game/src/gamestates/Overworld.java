package gamestates;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import entities.Entity;
import entities.Tree;
import entities.enemy.DeathAnimation;
import entities.enemy.Enemy;
import entities.enemy.Ghost;
import entities.enemy.Slime;
import entities.player.Player;
import graphics.Shaders;
import location.ExitZone;
import location.LocationManager;
import main.Game;

public class Overworld extends State implements Statemethods{
	
	private Player player;
	private LocationManager locationManager;
	
	public static boolean SHOW_HITBOXES = false;
	
	// camera offset vars
	private int xLocationOffset, yLocationOffset;
	private int leftBorder = (int) (0.4 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
	private int upperBorder = (int) (0.4 * Game.GAME_HEIGHT);
	private int lowerBorder = (int) (0.6 * Game.GAME_HEIGHT);
	private int locationTilesWide;
	private int locationTilesHigh;
	private int maxTilesOffsetX;
	private int maxTilesOffsetY;
	private int maxLocationOffsetX;
	private int maxLocationOffsetY;
	
	// entity lists
	private ArrayList<Entity> characters;
	private ArrayList<Enemy> enemies;
	private ArrayList<Entity> objects;
	
	// loading location vars
	private boolean transitioning = true;
	private boolean locationLoaded = false;
	private boolean firstLoad = true;
	private int loadingSpeed = 8;
	private int nextLocationIndex;
	private int transitionTick = 0;
	private int spawnX, spawnY;
	
	private DeathAnimation deathAnim = new DeathAnimation();
	
	public void createNewDeathAnimation(BufferedImage sprite, float x, float y, float width, float height, float xDrawOffset, float yDrawOffset) {
		deathAnim.getDeathAnimations().add(sprite);
		deathAnim.getDeathAnimationStats().add(new float[] {x, y, width, height, xDrawOffset, yDrawOffset});
	}
	
	public Overworld(Game game) {
		super(game);
		
		init();
	}
	
	private void init() {
		locationManager = new LocationManager(game);
		
		spawnX = 2 * Game.TILES_SIZE;
		spawnY = 2 * Game.TILES_SIZE;
		player = new Player(spawnX, spawnY, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2);
		
		loadLocation(0);	// first map loaded
	}
	
	public static ArrayList<Entity> sortEntitiesByY(ArrayList<Entity> entities) {
        ArrayList<Entity> sortedList = new ArrayList<>(entities);
        Collections.sort(sortedList, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                return Float.compare(e1.getHitbox().y + e1.getHitbox().height, e2.getHitbox().y + e2.getHitbox().height);
            }
        });
        return sortedList;
	}
	
	@Override
	public void draw(Graphics g) {
		
        locationManager.draw(g, xLocationOffset, yLocationOffset);;
	
		deathAnim.draw(g, xLocationOffset, yLocationOffset);
        
		ArrayList<Entity> orderedEntityList = sortEntitiesByY(characters);
		for(Entity e : orderedEntityList) {
			
			if(!e.isAlive())
				continue;
			
			if(e instanceof Player)
				player.render(g, xLocationOffset, yLocationOffset);
			
			if(e instanceof Enemy)
				((Enemy) e).render(g, xLocationOffset, yLocationOffset);
			
			if(e instanceof Tree)
				((Tree) e).draw(g, xLocationOffset, yLocationOffset);
		}
		
		
		if(locationManager.getCurrentLocation().getShader() ==  1)
			Shaders.caveShaders(g, (float) (player.getHitbox().getCenterX() - xLocationOffset), (float) (player.getHitbox().getCenterY() - yLocationOffset), 100, 100);
		
		player.getHud().draw(g);
		
		
		if(transitioning) {
			g.setColor(Color.BLACK);
			g.fillRect(firstLoad ? transitionTick/2 : transitionTick - Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
		
	}
	
	@Override
	public void update() {
		
		if(transitioning) {
			transitionTick += firstLoad ? loadingSpeed * 2 : loadingSpeed;
			
			if(transitionTick >= Game.GAME_WIDTH && !locationLoaded) {
				loadLocation(nextLocationIndex);
				locationLoaded = true;
				System.out.println(nextLocationIndex);
			}
			
			if(transitionTick >= Game.GAME_WIDTH * 2) {
				transitioning = false;
				transitionTick = 0;
				locationLoaded = false;
				
				firstLoad = false;
			}
		}
		
		if(!transitioning) {
		
			// should only call when damage is dealt tbh
			updateEnemyList();
			
			locationManager.update();
			player.update();
			checkCloseToBorder();
			
			checkExitZones();
			
			if(player.isUsingSpell()) {
				if(player.getWaterRing().isCastingSpell()) {
					player.getWaterRing().spellEffect();
				}else if(player.getFlamethrower().isCastingSpell()) {
					player.getFlamethrower().spellEffect();
				}
			}
			
			for(Enemy enemy : enemies)
				enemy.update();
			
			
			checkPlayerDead();
			
		}
		
		player.getBook().updatePlayerPos((int) (player.getHitbox().x - xLocationOffset), (int) (player.getHitbox().y - yLocationOffset));
		
		deathAnim.update();
		
	}
	
	private void checkPlayerDead() {
		if(!player.isAlive()) {
			createNewDeathAnimation(player.getCurrentSprite(), player.getHitbox().x, player.getHitbox().y, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2, player.getxDrawOffset(), player.getyDrawOffset());
			player.setHealth(player.getMaxHealth());
			player.setMana(player.getMaxMana());
			changePlayerPos();
			player.setAlive(true);
			player.setInvincible(true);
			player.getHud().updateHearts();
		}
	}

	private void updateEnemyList() {
	    Iterator<Enemy> iterator = enemies.iterator();
	    while (iterator.hasNext()) {
	        Enemy enemy = iterator.next();
	        enemy.checkIfAlive();
	        if (!enemy.isAlive()) {
	        	
	        	createNewDeathAnimation(enemy.getCurrentSprite(), enemy.getHitbox().x, enemy.getHitbox().y, Game.TILES_SIZE, Game.TILES_SIZE, enemy.getxDrawOffset(), enemy.getyDrawOffset());
	        	
	        	
	            iterator.remove();
	            System.out.println("enemy died...");
	        }
	    }
	}

	private void checkExitZones() {
		for(ExitZone ez : locationManager.getCurrentLocation().getExitZones()) {
			if(ez.getBoundary().intersects(player.getCollisionBox())) {
				spawnX = ez.getX();
				spawnY = ez.getY();
				initLoadingLocation(ez.getLocationIndex());
				//loadLocation(ez.getLocationIndex());
			}
		}
	}
	
	private void initLocationVars() {
		characters = new ArrayList<Entity>();
		characters.add(player);
		enemies = new ArrayList<Enemy>();
		
		deathAnim = new DeathAnimation();
	}
	
	private void initLoadingLocation(int locationIndex) {
		transitioning = true;
		nextLocationIndex = locationIndex;
	}
	
	private void loadLocation(int locationIndex) {
		
		// set location in manager
		locationManager.setCurrentLocation(locationIndex);
		
		// load entities
		initLocationVars();
		
		enemies = locationManager.loadEnemies(enemies);
		objects = locationManager.loadObjects(objects);
		
		characters.addAll(enemies);
		characters.addAll(objects);
		
		for(Enemy enemy : enemies) {
			enemy.loadPlayerHitbox(player.getHitbox());
			enemy.loadPlayer(player);
		}
	
		
		// update map data for player
		player.loadMapData(locationManager.getCurrentLocation().getMapData(), characters, enemies);
		for(Enemy enemy : enemies)
			enemy.loadMapData(locationManager.getCurrentLocation().getMapData(), characters, enemies);
		
		// load exit zones
		locationManager.getLocations().get(locationIndex).loadExitZones();
		
		// update screen offset vars
		locationTilesWide = locationManager.getCurrentLocation().getMapData().get(0)[0].length;
		locationTilesHigh = locationManager.getCurrentLocation().getMapData().get(0).length;
		maxTilesOffsetX = locationTilesWide - Game.TILES_IN_WIDTH;
		maxTilesOffsetY = locationTilesHigh - Game.TILES_IN_HEIGHT;
		maxLocationOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
		maxLocationOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
		
		changePlayerPos();
	}
	
	private void changePlayerPos() {
		// update player location
		player.updatePlayerPos(spawnX, spawnY);
		
		// update book pos
		checkCloseToBorder();
		player.getBook().setBookPos((int) (player.getHitbox().x - xLocationOffset), (int) (player.getHitbox().y - yLocationOffset));
	}
	
	// calculates screen offset values when close to border
	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int playerY = (int) player.getHitbox().y;
		int xDiff = playerX - xLocationOffset;
		int yDiff = playerY - yLocationOffset;
		
		if(xDiff > rightBorder)
			xLocationOffset += xDiff - rightBorder;
		else if(xDiff < leftBorder)
			xLocationOffset += xDiff - leftBorder;
		
		if(yDiff > lowerBorder)
			yLocationOffset += yDiff - lowerBorder;
		else if(yDiff < upperBorder)
			yLocationOffset += yDiff - upperBorder;
		
		if(xLocationOffset > maxLocationOffsetX)
			xLocationOffset = maxLocationOffsetX;
		
		if (xLocationOffset < 0)
			xLocationOffset = 0;
		
		if(yLocationOffset > maxLocationOffsetY)
			yLocationOffset = maxLocationOffsetY;
		
		if (yLocationOffset < 0)
			yLocationOffset = 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// attack
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(player.isInBufferFrames()) {
				if(player.isAttacking())
					player.setNextAttackSelected(true);
				else
					player.increaseAttackIndex();
			}
			
			if(!player.isAttacking()) {
				if(!player.isInBufferFrames())
					player.setCurrentAttackIndex(0);
				
				player.setLocationOffset(xLocationOffset, yLocationOffset);
				player.setAttacking(true);
			}
		}
		
		// flamethrower
		if(e.getButton() == MouseEvent.BUTTON3)
			if(!player.isUsingSpell())
				if(player.getMana() >= player.getFlamethrower().getManaUsage())
					player.getFlamethrower().initSpellUseVars(e.getX(), e.getY(), xLocationOffset, yLocationOffset);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setUp(true);
			break;
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_S:
			player.setDown(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		
		case KeyEvent.VK_H:
			SHOW_HITBOXES = !SHOW_HITBOXES;
			break;		
			
		// temp load locations
		case KeyEvent.VK_1:
			loadLocation(0);
			break;
		case KeyEvent.VK_2:
			loadLocation(1);
			break;
			
		// cast water ring
		case KeyEvent.VK_Q:
			if(player.getMana() >= player.getWaterRing().getManaUsage())
				player.getWaterRing().initSpellUseVars();
			break;
		
		// roll
		case KeyEvent.VK_SPACE:
			if((player.isLeft() || player.isRight()) || (player.isUp() || player.isDown())) {
				player.setRolling(true);
				player.initRollVars();
				System.out.println("roll");
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setUp(false);
			break;
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_S:
			player.setDown(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
			
			
		case KeyEvent.VK_O:
			player.decreaseHealth(1);;
			break;
		case KeyEvent.VK_P:
			player.decreaseHealth(3);;
			break; 
		}
		
	}
	
	public void windowFocusLost() {
		
	}
	
	public Player getPlayer(){
		return player;
	}
	
}
