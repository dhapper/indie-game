package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import entities.Enemy;
import entities.enemy.Entity;
import entities.enemy.Ghost;
import entities.enemy.Slime;
import entities.player.Player;
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
	
	public Overworld(Game game) {
		super(game);
		
		init();
	}
	
	private void init() {
		locationManager = new LocationManager(game);
		player = new Player( 2 * Game.TILES_SIZE, 2 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE));
		loadLocation(0);	// first map loaded
	}

	@Override
	public void draw(Graphics g) {
        locationManager.draw(g, xLocationOffset, yLocationOffset);;
		player.render(g, xLocationOffset, yLocationOffset);
		
		for(Enemy enemy : enemies)
			enemy.render(g, xLocationOffset, yLocationOffset);
		
		player.getHud().draw(g);
	}
	
	@Override
	public void update() {
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
		
		player.getBook().updatePlayerPos((int) (player.getHitbox().x - xLocationOffset), (int) (player.getHitbox().y - yLocationOffset));
	}
	
	private void updateEnemyList() {
	    Iterator<Enemy> iterator = enemies.iterator();
	    while (iterator.hasNext()) {
	        Enemy enemy = iterator.next();
	        enemy.checkIfAlive();
	        if (!enemy.isAlive()) {
	            iterator.remove();
	            System.out.println("enemy died...");
	        }
	    }
	}

	private void checkExitZones() {
		for(ExitZone ez : locationManager.getCurrentLocation().getExitZones()) {
			if(ez.getBoundary().intersects(player.getCollisionBox())) {
				player.updatePlayerPos(ez.getX(), ez.getY());
				loadLocation(ez.getLocationIndex());
			}
		}
	}
	
	private void initLocationCharacters() {
		characters = new ArrayList<Entity>();
		characters.add(player);
		enemies = new ArrayList<Enemy>();
	}

	private void loadLocation(int locationIndex) {
		
		// temporarily create enemys here
		if(locationIndex == 0) {
			initLocationCharacters();
			enemies.add(new Ghost(20 * Game.TILES_SIZE, 18 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			enemies.add(new Ghost(22 * Game.TILES_SIZE, 19 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			enemies.add(new Slime(7 * Game.TILES_SIZE, 4 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			enemies.add(new Slime(8 * Game.TILES_SIZE, 5 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			
			enemies.add(new Ghost(34 * Game.TILES_SIZE, 12 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			enemies.add(new Slime(33 * Game.TILES_SIZE, 11 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			enemies.add(new Slime(30 * Game.TILES_SIZE, 10 * Game.TILES_SIZE, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE)));
			characters.addAll(enemies);
			
			for(Enemy enemy : enemies) {
				enemy.loadPlayerHitbox(player.getHitbox());
				enemy.loadPlayer(player);
			}
			
		}else if(locationIndex == 1) {
			initLocationCharacters();
		}
		
		// set location in manager
		locationManager.setCurrentLocation(locationIndex);
		
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
