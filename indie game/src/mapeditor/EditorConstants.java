package mapeditor;

public class EditorConstants {
	
	public static class MapEditorConstants{
		
		//public static final int MAP_TILE_SIZE = 32;
		public static final int VISIBLE_MAP_TILE_WIDTH = 40;
		public static final int VISIBLE_MAP_TILE_HEIGHT = 25;
		
		// must order int values and array correctly, need to fix this
		public static final int GROUND_SPRITES = 0;
		public static final int ANIMATED_SPRITES = 1;
		public static final int OVERLAP_SPRITES = 2;
		public static final int PATH_SPRITES = 3;
		public static final int COLLISION_SPRITES = 4;
		public static final int OBJECT_SPRITES = 5;
		public static final int ENEMY_SPRITES = 6;
		
		
		public static final int[] LAYER_ORDER = {GROUND_SPRITES, ANIMATED_SPRITES, OVERLAP_SPRITES, PATH_SPRITES, COLLISION_SPRITES, OBJECT_SPRITES, ENEMY_SPRITES};
		public static final int[] COLLISION_LAYERS = {ANIMATED_SPRITES, COLLISION_SPRITES};
		
		public static String GetSpriteSheetFileName(int layer) {
			switch (layer) {
			case GROUND_SPRITES:
				return "GROUND_SPRITES.png";
			case OVERLAP_SPRITES:
				return "OVERLAP_SPRITES.png";
			case COLLISION_SPRITES:
				return "COLLISION_SPRITES.png";
			case ANIMATED_SPRITES:
				return "ANIMATED_SPRITES.png";
			case OBJECT_SPRITES:
				return "OBJECT_SPRITES.png";
			case ENEMY_SPRITES:
				return "ENEMY_SPRITES.png";
			case PATH_SPRITES:
				return "PATH_SPRITES.png";
			}
			return null;
		}
	}
}
