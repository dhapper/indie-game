package main;

import java.awt.Graphics;

import gamestates.GameState;
import gamestates.Menu;
import gamestates.Overworld;

public class Game implements Runnable{

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	// specs
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	// gamestates
	private Overworld overworld;
	private Menu menu;
	
	// sizes
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2.5f;
	public final static int TILES_IN_WIDTH = 18;
	public final static int TILES_IN_HEIGHT = 10;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
//	static {
//		// Get the graphics environment
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        // Get the default screen device (primary monitor)
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        // Get the display mode of the screen device
//        DisplayMode dm = gd.getDisplayMode();
//        // Get the height of the monitor
//        int monitorHeight = dm.getHeight();
//        System.out.println(monitorHeight);
//        
//        TILES_DEFAULT_SIZE = 32;
//    	SCALE = monitorHeight / 280;
//    	TILES_IN_WIDTH = 18;
//    	TILES_IN_HEIGHT = 11;
//    	TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
//    	GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
//    	GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
//	}
	
	public Game() {
		
		initClasses();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		startGameLoop();
	}

	private void initClasses() {
		menu = new Menu(this);
		overworld = new Overworld(this);
	}
	
	

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void update() {
		
		switch(GameState.state) {
		case MENU:
			menu.update();
			break;
		case OVERWORLD:
			overworld.update();
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		
		switch(GameState.state) {
		case MENU:
			menu.draw(g);
			break;
		case OVERWORLD:
			overworld.draw(g);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void run() {
		// game loop
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		long previousTime = System.nanoTime();
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				update();
				deltaU--;
			}
			
			if(deltaF >= 1) {
				gamePanel.repaint();
				deltaF--;
			}
			
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS: " + frames + " | UPS: " + updates);
			}
			
		}
	}	
	
	public void windowFocusLost() {
		if(GameState.state == GameState.OVERWORLD)
			overworld.getPlayer().resetDirBooleans();
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Overworld getOverworld() {
		return overworld;
	}
	
}
