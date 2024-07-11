package mapeditor;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import static utilz.Constants.MapEditorConstants.*;

public class MapEditor extends JFrame{

	private SpritePanel spritePanel;
	private MapPanel mapPanel;
	
	public static int WIDTH, HEIGHT;
	
	public static ArrayList<SpriteSheetData> SPRITE_LAYERS;
	public static ArrayList<Tile[][]> TILE_LAYERS;
	
	public static int LAYER = GROUND_SPRITES;
	
	public MapEditor(ArrayList<int[][]> mapData, int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		
		initSpriteLayers();
		initTileLayers();
		
		if(mapData != null)
			loadMap(mapData);
		
		setTitle("Map Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) (TILE_SIZE * (VISIBLE_MAP_TILE_WIDTH + 6 + 0.5f)), (int) (TILE_SIZE * (VISIBLE_MAP_TILE_HEIGHT + 1.7f)));
        setLayout(new BorderLayout());
        
        spritePanel = new SpritePanel();
        mapPanel = new MapPanel();
        
        add(spritePanel, BorderLayout.WEST);
        add(new JScrollPane(mapPanel), BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}

	private void loadMap(ArrayList<int[][]> mapData) {
		
		int index = 0;
		for(Tile[][] tileLayer : TILE_LAYERS) {
			for(int i = 0; i < mapData.get(0).length; i++) {
				for(int j = 0; j < mapData.get(0)[0].length; j++) {
					if(mapData.get(index)[i][j] != -1)
						tileLayer[i][j].changeSprite(mapData.get(index)[i][j]);
				}
			}
			index++;
			LAYER++;
		}
		LAYER = GROUND_SPRITES;
	}

	private void initSpriteLayers() {
		SPRITE_LAYERS = new ArrayList<SpriteSheetData>();
		
		for(int layer : LAYER_ORDER) {
			//System.out.println(GetSpriteSheetFileName(layer));
			SPRITE_LAYERS.add(SpriteSheets.GetSprites(GetSpriteSheetFileName(layer)));
		}
		
//		SPRITE_LAYERS.add(SpriteSheets.GetSprites(GetSpriteSheetFileName(GROUND_SPRITES)));	// GROUND SPRITES
//		SPRITE_LAYERS.add(SpriteSheets.GetSprites(GetSpriteSheetFileName(COLLISION_SPRITES)));	// COLLISION SPRITES
	}
	
	private void initTileLayers() {
		TILE_LAYERS = new ArrayList<Tile[][]>();
		for(SpriteSheetData layers : SPRITE_LAYERS) {
			TILE_LAYERS.add(new Tile[HEIGHT][WIDTH]);
			for(int i = 0; i < TILE_LAYERS.get(0).length; i ++) {
				for(int j = 0; j < TILE_LAYERS.get(0)[0].length; j ++) {	
					for(Tile[][] tiles : TILE_LAYERS) {
						tiles[i][j] = new Tile();
					}
				}
			}
		}
	}
	
}
