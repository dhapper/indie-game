package location;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Location {

	private LocationManager locationManager;
	private int index;
	private ArrayList<int[][]> mapData;
	
	private ArrayList<ExitZone> exitZones;
	
	public Location(LocationManager locationManager, int index, ArrayList<int[][]> mapData) {
		this.locationManager = locationManager;
		this.index = index; 
		this.mapData = mapData;
	}
	
//	public void loadExitZones() {
//		System.out.println("load");
//		Rectangle exitZoneRect = new Rectangle(200, 200, 100, 100);
//		exitZones.add(new ExitZone(exitZoneRect, 0, 300, 300));
//	}
	
	public void loadExitZones() {
		
		exitZones = new ArrayList<ExitZone>();
		Rectangle exitZoneRect;
		switch(index) {
		case 0:
			break;
		case 1:
			exitZoneRect = new Rectangle(200, 200, 100, 100);
			exitZones.add(new ExitZone(exitZoneRect, 0, 300, 300));
			exitZoneRect = new Rectangle(400, 200, 100, 100);
			exitZones.add(new ExitZone(exitZoneRect, 0, 500, 500));
			break;
		}
	}
	
	public int getSpriteIndex(int layer, int x, int y) {
		return mapData.get(layer)[y][x];
	}
	
	public ArrayList<int[][]> getMapData(){
		return mapData;
	}

	public ArrayList<ExitZone> getExitZones() {
		return exitZones;
	}

//	public void setExitZones(ExitZone exitZones) {
//		this.exitZones = exitZones;
//	}

	public LocationManager getLocationManager() {
		return locationManager;
	}
	
	
	
}
