package utilz;

import java.util.ArrayList;

import main.Game;

import static utilz.Constants.MapConstants.*;

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, ArrayList<int[][]> mapData) {
		
		if(!IsSolid(x, y, mapData))
			if(!IsSolid(x + width, y + height, mapData))
				if(!IsSolid(x + width, y, mapData))
					if(!IsSolid(x, y + height, mapData))
						return true;
		
		
		return false;
	}
	
	
	private static boolean IsSolid(float x, float y, ArrayList<int[][]> mapData) {
		
		int maxWidth = mapData.get(0)[0].length * Game.TILES_SIZE;
		int maxHeight = mapData.get(0).length * Game.TILES_SIZE;
		
		if(x < 0 || x >= maxWidth)
			return true;
		
		if(y < 0 || y >= maxHeight)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		// iterate through all collision layers
		int value = mapData.get(2)[(int) yIndex][(int) xIndex];
		
		if(value != -1)
			return true;
		else
			return false;
	}
	
	public static float[] GetVector(float x1, float y1, float x2, float y2){
		
		float dx = x1 - x2;
		float dy = x1 - x2;
		
		if(dy == 0)
			dy -= 0.1;
		
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		
		float unitX = dx / length;
	    float unitY = dy / length;
		
	    unitX *= Game.SCALE;
	    unitY *= Game.SCALE;
	    
	    return new float[] {unitX, unitY};
	}
}
