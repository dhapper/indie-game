package location;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ExitZone {

	private Rectangle boundary;
	//private Location location;
	int locationIndex;
	private int x, y;
	
	public ExitZone(Rectangle boundary, int locationIndex, int x, int y) {
		this.boundary  = boundary;
		//this.location = location;
		this.locationIndex = locationIndex;
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		g.setColor(new Color(0, 0, 100, 100));
		g.fillRect(boundary.x - xOffset, boundary.y - yOffset, boundary.width, boundary.height);
	}
	
//	public void enterZone() {
//		
//	}

	public Rectangle getBoundary() {
		return boundary;
	}

	public void setBoundary(Rectangle boundary) {
		this.boundary = boundary;
	}

//	public Location getLocation() {
//		return location;
//	}
//
//	public void setLocation(Location location) {
//		this.location = location;
//	}
	
	public int getLocationIndex() {
		return locationIndex;
	}	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
