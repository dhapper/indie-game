package location;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utilz.LoadSave;

import static utilz.Constants.MapConstants.*;

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
		locations = new ArrayList<Location>();
		// keep locations ordered
		locations.add(new Location(this, 0, LoadSave.GetMapDataFromFile("MAP_DATA_0")));
		locations.add(new Location(this, 1, LoadSave.GetMapDataFromFile("MAP_DATA_1")));
		
	}

	private void createSprites() {
		sprites = new ArrayList<BufferedImage[]>();
		
		for(BufferedImage sheet : tilesets) {
			int width = sheet.getWidth() / 32;
			int height = sheet.getHeight() / 32;
			
			sprites.add(new BufferedImage[width * height]);
			for(int i = 0; i < width * height; i++) {
				sprites.get(sprites.size() - 1)[i] = sheet.getSubimage((i % width) * TILE_SIZE, (i / width) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
		
	}

	private void importTilesets() {
		tilesets = new ArrayList<BufferedImage>();
		
		for(int layer : LAYER_ORDER)
			tilesets.add(LoadSave.LoadImage("tilesets/" + GetSpriteSheetFileName(layer)));
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		
		for(int layer : LAYER_ORDER) {
			for(int j = 0; j < currentLocation.getMapData().get(0).length; j++) {
				for(int i = 0; i < currentLocation.getMapData().get(0)[0].length; i++) {
					int index = currentLocation.getSpriteIndex(layer, i, j);
					if(index != -1)
						g.drawImage(sprites.get(layer)[index], i * Game.TILES_SIZE - xOffset, j * Game.TILES_SIZE - yOffset, Game.TILES_SIZE, Game.TILES_SIZE, null);
				}
			}
		}
		
		for(ExitZone ez : currentLocation.getExitZones())
			ez.draw(g, xOffset, yOffset);
	}
	
	public void update() {
		
	}
	
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
