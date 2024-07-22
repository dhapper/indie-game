package utilz;

public class Constants {

	public static class EnemyIndex{
		public static final int SLIME = 0;
		public static final int GHOST = 1;
	}
	
	public static class Attack{
		public static final int BASIC = 0;
		public static final int FINISHER = 1;
		
		public static final int BUFFER_FRAMES = 30;
	}
	
	public static class Directions{
		public static final int UP = 0;
		public static final int DOWN = 1;
		public static final int LEFT = 2;
		public static final int RIGHT = 3;
		
		public static final int UP_LEFT = 4;
		public static final int UP_RIGHT = 5;
		public static final int DOWN_LEFT = 6;
		public static final int DOWN_RIGHT = 7;
		
		public static final int TOWARDS = 0;
		public static final int AWAY = 1;
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
		public static final int CASTING_SPELL = 4;
		
		public static final int IDLE_B = 9;
		public static final int WALKING_B = 10;
		public static final int CASTING_SPELL_B = 12;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
			case IDLE_B:
				return 2;
			case WALKING:
			case WALKING_B:
				return 4;
			case ATTACKING:
				return 8;
			case CASTING_SPELL:
			case CASTING_SPELL_B:
				return 6;
			default:
				return 0;	
			}
		}
		
		static int atk = 5;
		static int walk = 20;
		static int cast = 10; 
		
		public static int[] GetSpriteDuration(int player_action) {
			switch(player_action) {
			case IDLE:
			case IDLE_B:
				return new int[] {120, 60};
			case WALKING:
			case WALKING_B:
				return new int[] {walk, walk, walk, walk};
			case ATTACKING:
				return new int[] {atk, atk, atk, atk*2, atk*2, atk*2, atk, atk, atk};
			case CASTING_SPELL:
			case CASTING_SPELL_B:
				return new int[] {cast, cast, cast, cast, cast, cast};
			default:
				return null;	
			}
		}
	}
	
}
