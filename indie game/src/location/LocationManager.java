package location;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public BufferedImage getSprite(int layer, int index) {
        return sprites.get(layer)[index];
    }

    public void draw(Graphics g, int xOffset, int yOffset) {
        currentLocation.draw(g, xOffset, yOffset);
    }

    public void update() {
        currentLocation.updateAnimations();
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
