package gamestates;

import java.awt.Color;   
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import entities.Enemy;
import entities.Player;
import location.ExitZone;
import location.LocationManager;
import main.Game;
import utilz.HelpMethods;
import utilz.LoadSave;


public class Overworld extends State implements Statemethods{
	
	private Player player;
	private LocationManager locationManager;
	
	
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
	
	public Overworld(Game game) {
		super(game);
		initClasses();
		
	}

	
	Enemy enemy;
	private void initClasses() {
		locationManager = new LocationManager(game);
		player = new Player(100, 100, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE));
		loadLocation(1);	// first map loaded
		
		enemy = new Enemy(500, 500, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE));
	}

	@Override
	public void draw(Graphics g) {
        locationManager.draw(g, xLocationOffset, yLocationOffset);;
		player.render(g, xLocationOffset, yLocationOffset);
		enemy.render(g, xLocationOffset, yLocationOffset);
	}
	
	@Override
	public void update() {
		locationManager.update();
		player.update();
		checkCloseToBorder();
		
		checkExitZones();
		
		if(player.isUsingSpell())
			waterRingKnockback();
	}
	
	private void waterRingKnockback() {
		
		if(player.getWaterRing().getBoundary().intersects(enemy.getHitbox())) {
			
			float x1 = player.getHitbox().x;
			float y1 = player.getHitbox().y;
			float x2 = enemy.getHitbox().x;
			float y2 = enemy.getHitbox().y;
			
			float[] vector = HelpMethods.GetVector(x1, y1, x2, y2);
		    
			enemy.getHitbox().x -= vector[0];
			enemy.getHitbox().y -= vector[1];
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

	private void loadLocation(int locationIndex) {
		
		// set location in manager
		locationManager.setCurrentLocation(locationIndex);
		
		// update map data for player
		player.loadMapData(locationManager.getCurrentLocation().getMapData());
		
		// load exit zones
		locationManager.getLocations().get(locationIndex).loadExitZones();
		
		// update screen offset vars
		locationTilesWide = locationManager.getCurrentLocation().getMapData().get(0)[0].length;
		locationTilesHigh = locationManager.getCurrentLocation().getMapData().get(0).length;
		maxTilesOffsetX = locationTilesWide - Game.TILES_IN_WIDTH;
		maxTilesOffsetY = locationTilesHigh - Game.TILES_IN_HEIGHT;
		maxLocationOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
		maxLocationOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
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
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			player.setAttacking(true);
		}
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			System.out.println("flame");
			player.setUsingSpell(true);
			player.getFlamethrower().setCastingSpell(true);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
			
			
		case KeyEvent.VK_1:
			loadLocation(0);
			break;
		case KeyEvent.VK_2:
			loadLocation(1);
			break;
			
		case KeyEvent.VK_Q:
			player.setUsingSpell(true);
			player.getWaterRing().setCastingSpell(true);
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
		}
		
	}
	
	public void windowFocusLost() {
		
	}
	
	public Player getPlayer(){
		return player;
	}
	
}
