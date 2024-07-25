package location;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import graphics.Animation;
import main.Game;
import mapeditor.EditorConstants;
import utilz.Constants;

public class Location {
    private LocationManager locationManager;
    private int index;
    private ArrayList<int[][]> mapData;
    private ArrayList<ExitZone> exitZones;
    private Animation[][] animations;
    private int shader = 0;

    public Location(LocationManager locationManager, int index, ArrayList<int[][]> mapData) {
        this.locationManager = locationManager;
        this.index = index;
        this.mapData = mapData;
        this.animations = new Animation[mapData.get(0).length][mapData.get(0)[0].length];

        initAnimations();
        loadExitZones();
        
        initShaders();
    }

    private void initShaders() {
		if(index == 1)
			this.shader = 1;
		
	}

	private void initAnimations() {
        for (int y = 0; y < mapData.get(0).length; y++) {
            for (int x = 0; x < mapData.get(0)[0].length; x++) {
                int index = getSpriteIndex(EditorConstants.MapEditorConstants.ANIMATED_SPRITES, x, y);	// dont like this
                if (index != -1) {
                    animations[y][x] = new Animation("ANIMATED_SPRITES.png", 0, 9, 20); // Adjust params as needed
                }
            }
        }
    }

    public void updateAnimations() {
        for (int y = 0; y < animations.length; y++) {
            for (int x = 0; x < animations[0].length; x++) {
                if (animations[y][x] != null) {
                    animations[y][x].update();
                }
            }
        }
    }
    
    // maybe shouldnt be here

    public void draw(Graphics g, int xOffset, int yOffset) {
        for (int layer : EditorConstants.MapEditorConstants.LAYER_ORDER) {
        	
        	if(layer != EditorConstants.MapEditorConstants.ENEMY_SPRITES) {
        		
        	
	            for (int y = 0; y < mapData.get(0).length; y++) {
	                for (int x = 0; x < mapData.get(0)[0].length; x++) {
	                    int index = getSpriteIndex(layer, x, y);
	                    if (layer == EditorConstants.MapEditorConstants.ANIMATED_SPRITES && index != -1 && animations[y][x].isAnimating()) {
	                        g.drawImage(animations[y][x].getCurrentFrame(), x * Game.TILES_SIZE - xOffset, y * Game.TILES_SIZE - yOffset, Game.TILES_SIZE, Game.TILES_SIZE, null);
	                    } else if (index != -1) {
	                        g.drawImage(locationManager.getSprite(layer, index), x * Game.TILES_SIZE - xOffset, y * Game.TILES_SIZE - yOffset, Game.TILES_SIZE, Game.TILES_SIZE, null);
	                    }
	                }
	            }
            
        	}
        }

        for (ExitZone ez : exitZones) {
            ez.draw(g, xOffset, yOffset);
        }
    }

	public int getShader() {
		return shader;
	}

	public void setShader(int shader) {
		this.shader = shader;
	}

	public int getSpriteIndex(int layer, int x, int y) {
        return mapData.get(layer)[y][x];
    }

    public ArrayList<int[][]> getMapData() {
        return mapData;
    }

    public ArrayList<ExitZone> getExitZones() {
        return exitZones;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void loadExitZones() {
        exitZones = new ArrayList<>();
        Rectangle exitZoneRect;
        switch (index) {
            case 0:
            	exitZoneRect = new Rectangle(39 * Game.TILES_SIZE, 24 * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
                exitZones.add(new ExitZone(exitZoneRect, 1, 2 * Game.TILES_SIZE, 4 * Game.TILES_SIZE));
                break;
            case 1:
            	exitZoneRect = new Rectangle(1 * Game.TILES_SIZE, 6 * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
                exitZones.add(new ExitZone(exitZoneRect, 0, 2 * Game.TILES_SIZE, 4 * Game.TILES_SIZE));
                break;
        }
    }
}
