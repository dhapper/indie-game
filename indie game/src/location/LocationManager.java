package location;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import entities.Tree;
import entities.enemy.Enemy;
import entities.enemy.Ghost;
import entities.enemy.Slime;
import main.Game;
import mapeditor.EditorConstants;
import utilz.Constants;
import utilz.LoadSave;

public class LocationManager {

    private Game game;
    private ArrayList<BufferedImage> tilesets;
    private ArrayList<BufferedImage[]> sprites;
    private ArrayList<Location> locations;
    private Location currentLocation;

    public LocationManager(Game game) {
        this.game = game;

        importTilesets();
        createSprites();

        initLocations();
    }
    
    private void initLocations() {
        locations = new ArrayList<>();
        // keep locations ordered
        locations.add(new Location(this, 0, LoadSave.GetMapDataFromFile("MAP_DATA_0")));
        locations.add(new Location(this, 1, LoadSave.GetMapDataFromFile("MAP_DATA_1")));
    }

    private void createSprites() {
        sprites = new ArrayList<>();

        for (BufferedImage sheet : tilesets) {
            int width = sheet.getWidth() / 32;
            int height = sheet.getHeight() / 32;

            sprites.add(new BufferedImage[width * height]);
            for (int i = 0; i < width * height; i++) {
                sprites.get(sprites.size() - 1)[i] = sheet.getSubimage((i % width) * Game.TILES_DEFAULT_SIZE, (i / width) * Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
            }
        }
    }

    private void importTilesets() {
        tilesets = new ArrayList<>();

        for (int layer : EditorConstants.MapEditorConstants.LAYER_ORDER)
            tilesets.add(LoadSave.LoadImage("tilesets/" + EditorConstants.MapEditorConstants.GetSpriteSheetFileName(layer)));
    }
    
	public ArrayList<Enemy> loadEnemies(ArrayList<Enemy> enemies) {
		enemies = new ArrayList<Enemy>();
		for (int y = 0; y < currentLocation.getMapData().get(0).length; y++) {
            for (int x = 0; x < currentLocation.getMapData().get(0)[0].length; x++) {
            	switch(currentLocation.getMapData().get(EditorConstants.MapEditorConstants.ENEMY_SPRITES)[y][x]) {
            	case Constants.EnemyIndex.SLIME:
            		enemies.add(new Slime(x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE));
            		break;
            	case Constants.EnemyIndex.GHOST:
            		enemies.add(new Ghost(x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE));
            		break;
            	}
            }
		}
		return enemies;
	}
	
	public ArrayList<Entity> loadObjects(ArrayList<Entity> objects) {
		objects = new ArrayList<Entity>();
		for (int y = 0; y < currentLocation.getMapData().get(0).length; y++) {
            for (int x = 0; x < currentLocation.getMapData().get(0)[0].length; x++) {
            	switch(currentLocation.getMapData().get(EditorConstants.MapEditorConstants.OBJECT_SPRITES)[y][x]) {
            	case Constants.ObjectIndex.PINK_TREE:
            		objects.add(new Tree(0, x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2));
            		break;
            	case Constants.ObjectIndex.GREEN_TREE:
            		objects.add(new Tree(1, x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE * 2, Game.TILES_SIZE * 2));
            		break;
            	}
            }
		}
		return objects;
	}
	
    public BufferedImage getSprite(int layer, int index) {
        return sprites.get(layer)[index];
    }

    public void draw(Graphics g, int xOffset, int yOffset) {
        currentLocation.draw(g, xOffset, yOffset);
    }

    public void update() {
        currentLocation.updateAnimations();
    }
    
    // getters and setters

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int locationIndex) {
        this.currentLocation = locations.get(locationIndex);
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
    
}
