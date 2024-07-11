package utilz;

public class Constants {

	public static class MapEditorConstants{
		public static final int TILE_SIZE = 32;
		public static final int VISIBLE_MAP_TILE_WIDTH = 40;
		public static final int VISIBLE_MAP_TILE_HEIGHT = 25;
		
		// must order int values and array correctly, need to fix this
		public static final int GROUND_SPRITES = 0;
		public static final int ANIMATED_SPRITES = 1;
		public static final int OVERLAP_SPRITES = 2;
		public static final int COLLISION_SPRITES = 3;
		
		public static final int[] LAYER_ORDER = {GROUND_SPRITES, ANIMATED_SPRITES, OVERLAP_SPRITES, COLLISION_SPRITES};
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
			}
			return null;
		}
	}
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class SlimeConstants{
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int DEATH = 2;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
				return 4;
			case RUNNING:
				return 6;
			case DEATH:
				return 5;
			default:
				return 0;	
			}
		}
		
		public static int GetSpriteDuration(int player_action) {
			switch(player_action) {
			case IDLE:
				return 60;
			case RUNNING:
				return 20;
			case DEATH:
				return 40;
			default:
				return 0;	
			}
		}
	}
	
	public static class PlayerConstants{
		public static final int IDLE = 0;
		public static final int WALKING = 2;
		public static final int ATTACKING = 8;
		public static final int WATER_SPELL = 2;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
				return 2;
			case WALKING:
				return 4;
			case ATTACKING:
				return 8;
			default:
				return 0;	
			}
		}
		
		static int atk = 5;
		static int walk = 20;
		
		public static int[] GetSpriteDuration(int player_action) {
			switch(player_action) {
			case IDLE:
				return new int[] {120, 60};
			case WALKING:
				return new int[] {walk, walk, walk, walk};
			case ATTACKING:
				return new int[] {atk, atk, atk, atk*2, atk*2, atk*2, atk, atk, atk};
			default:
				return null;	
			}
		}
	}
	
}
