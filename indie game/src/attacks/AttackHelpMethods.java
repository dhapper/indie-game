package attacks;

import java.awt.geom.Rectangle2D;

import entities.Enemy;
import main.Game;
import utilz.Constants;
import utilz.HelpMethods;

public class AttackHelpMethods {

	public static void KnockBack(Enemy enemy, float distance, Rectangle2D.Float hitbox1, Rectangle2D.Float hitbox2) {
		float x1 = (float) hitbox1.getCenterX();
		float y1 = (float) hitbox1.getCenterY();
		float x2 = (float) hitbox2.getCenterX();
		float y2 = (float) hitbox2.getCenterY();
		
		float[] vector = HelpMethods.GetVector(x1, y1, x2, y2);
		
		float[] adjustedVector = {-1 * vector[0], -1 * vector[1]};
		
		enemy.moveTowardsPos(distance * Game.SCALE, adjustedVector, Constants.Directions.AWAY);
	}
}
