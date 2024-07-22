package mapeditor;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.Game;
import mapeditor.panel.LayerButtonsPanel;
import mapeditor.panel.MapPanel;
import mapeditor.panel.SpritePanel;
import mapeditor.panel.ToolPanel;

import static mapeditor.EditorConstants.MapEditorConstants.*;

public class MapEditor extends JFrame {

    private SpritePanel spritePanel;
    private MapPanel mapPanel;
    private LayerButtonsPanel layerButtonsPanel;
    private ToolPanel toolPanel;

    public static int WIDTH, HEIGHT;
    public static ArrayList<SpriteSheetData> SPRITE_LAYERS;
    public static ArrayList<Tile[][]> TILE_LAYERS;
    public static int LAYER = GROUND_SPRITES;

    public MapEditor(ArrayList<int[][]> mapData, int width, int height) {
        WIDTH = width;
        HEIGHT = height;

        initSpriteLayers();
        initTileLayers();

        if (mapData != null) {
            loadMap(mapData);
        }

        setTitle("Map Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) (Game.TILES_DEFAULT_SIZE * (VISIBLE_MAP_TILE_WIDTH + 6 + 0.5f)),
                (int) (Game.TILES_DEFAULT_SIZE * (VISIBLE_MAP_TILE_HEIGHT + 1.7f)));
        setLayout(new BorderLayout());

        spritePanel = new SpritePanel(this);
        mapPanel = new MapPanel(this);

        layerButtonsPanel = new LayerButtonsPanel(this);
        toolPanel = new ToolPanel(this);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(layerButtonsPanel, BorderLayout.NORTH);
        leftPanel.add(spritePanel, BorderLayout.CENTER);
        leftPanel.add(toolPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(new JScrollPane(mapPanel), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void loadMap(ArrayList<int[][]> mapData) {
        int index = 0;
        for (Tile[][] tileLayer : TILE_LAYERS) {
            for (int i = 0; i < mapData.get(0).length; i++) {
                for (int j = 0; j < mapData.get(0)[0].length; j++) {
                    if (mapData.get(index)[i][j] != -1) {
                        tileLayer[i][j].changeSprite(mapData.get(index)[i][j]);
                    }
                }
            }
            index++;
            LAYER++;
        }
        LAYER = GROUND_SPRITES;
    }

    private void initSpriteLayers() {
        SPRITE_LAYERS = new ArrayList<>();
        for (int layer : LAYER_ORDER) {
            SPRITE_LAYERS.add(SpriteSheets.GetSprites(GetSpriteSheetFileName(layer)));
        }
    }

    private void initTileLayers() {
        TILE_LAYERS = new ArrayList<>();
        for (SpriteSheetData layers : SPRITE_LAYERS) {
            TILE_LAYERS.add(new Tile[HEIGHT][WIDTH]);
            for (int i = 0; i < TILE_LAYERS.get(0).length; i++) {
                for (int j = 0; j < TILE_LAYERS.get(0)[0].length; j++) {
                    for (Tile[][] tiles : TILE_LAYERS) {
                        tiles[i][j] = new Tile();
                    }
                }
            }
        }
    }

	public ToolPanel getToolPanel() {
		return toolPanel;
	}

	public SpritePanel getSpritePanel() {
		return spritePanel;
	}
    
    
}
