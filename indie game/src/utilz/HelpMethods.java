package utilz;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import main.Game;

import static utilz.Constants.MapEditorConstants.*;

public class HelpMethods {
	
	public static boolean IsEntityThere(Entity entity, float xSpeed, float ySpeed, ArrayList<Entity> entityList) {
		Rectangle2D.Float nextHitbox = new Rectangle2D.Float(entity.getHitbox().x + xSpeed, entity.getHitbox().y + ySpeed,
				entity.getHitbox().width, entity.getHitbox().height);
		
		for(Entity e : entityList)
			if(e != entity)
				if(e.getHitbox().intersects(nextHitbox))
						return true;
		return false;
	}

	public static boolean CanMoveHere(Entity entity, float x, float y, float width, float height, ArrayList<int[][]> mapData) {
		if(!IsSolid(entity, x, y, mapData))
			if(!IsSolid(entity, x + width, y + height, mapData))
				if(!IsSolid(entity, x + width, y, mapData))
					if(!IsSolid(entity, x, y + height, mapData))
						return true;
		return false;
	}
	
	
	private static boolean IsSolid(Entity entity, float x, float y, ArrayList<int[][]> mapData) {
		int maxWidth = mapData.get(0)[0].length * Game.TILES_SIZE;
		int maxHeight = mapData.get(0).length * Game.TILES_SIZE;
		
		if(x < 0 || x >= maxWidth)
			return true;
		
		if(y < 0 || y >= maxHeight)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		// iterate through all collision layers
		for(int layer : COLLISION_LAYERS) {
			int value = mapData.get(layer)[(int) yIndex][(int) xIndex];
		
			if(value != -1) {
				// check if overlap layer is above animated layer (water)
				if(!(layer == ANIMATED_SPRITES && mapData.get(OVERLAP_SPRITES)[(int) yIndex][(int) xIndex] != -1))
					return true;
			}
		}
		
		return false;
	}
	
	public static float[] GetVector(float x1, float y1, float x2, float y2){
		float dx = x1 - x2;
		float dy = y1 - y2;
		
		if(dy == 0)
			dy -= 0.1;
		
		if(dx == 0)
			dx -= 0.1;
		
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		float unitX = dx / length;
	    float unitY = dy / length;
	    unitX *= Game.SCALE;
	    unitY *= Game.SCALE;
	    
	    return new float[] {unitX, unitY};
	}
}
