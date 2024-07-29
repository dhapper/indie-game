package utilz;

public class Constants {

	public static class ObjectIndex{
		
		public static final int PINK_TREE = 0;
		public static final int GREEN_TREE = 8;
	}
	
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
		public static final int IDLE_B = 1;
		public static final int WALKING = 2;
		public static final int WALKING_DOWN = 3;
		public static final int WALKING_UP = 4;
		public static final int CASTING_SPELL = 5;
		public static final int ATTACKING = 6;
		public static final int ATTACKING_DOWN = 7;
		public static final int ATTACKING_UP = 8;
		public static final int ROLL = 9;
		public static final int ROLL_B = 10;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
			case IDLE_B:
				return 4;
			case WALKING:
			case WALKING_DOWN:
			case WALKING_UP:
				return 8;
			case ATTACKING:
			case ATTACKING_DOWN:
			case ATTACKING_UP:
				return 11;
			case CASTING_SPELL:
				return 1;
			case ROLL:
			case ROLL_B:
				return 9;
			default:
				return 0;	
			}
		}
		
		public static final int ATTACK_FRAME_TIME = 7;
		public static final int walk = 20;
		public static final int cast = 10;
		public static final int roll = 7;
		
		static int durations[];
		
		public static int[] GetSpriteDuration(int player_action) {
			switch(player_action) {
			case IDLE:
			case IDLE_B:
				return new int[] {120, 120, 120, 120};
			case WALKING:
			case WALKING_DOWN:
			case WALKING_UP:
				return new int[] {walk, walk, walk, walk, walk, walk, walk, walk};
			case ATTACKING:
			case ATTACKING_DOWN:
			case ATTACKING_UP:
//				durations = new int[GetSpriteAmount(player_action)];
//				for(int i = 0; i < durations.length; i++)
//				    durations[i] = ATTACK_FRAME_TIME;
//				return durations;
				return new int[] {ATTACK_FRAME_TIME, ATTACK_FRAME_TIME, ATTACK_FRAME_TIME,
						ATTACK_FRAME_TIME, ATTACK_FRAME_TIME, ATTACK_FRAME_TIME,
						ATTACK_FRAME_TIME, ATTACK_FRAME_TIME, ATTACK_FRAME_TIME,
						ATTACK_FRAME_TIME, ATTACK_FRAME_TIME,};
			case CASTING_SPELL:
				return new int[] {120};
			case ROLL:
			case ROLL_B:
				return new int[] {roll, roll, roll, roll, roll, roll, roll, roll, roll};
			default:
				return null;	
			}
		}
	}
	
}
