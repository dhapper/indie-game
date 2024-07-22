package mapeditor.panel;

import javax.swing.JPanel;

import mapeditor.MapEditor;
import mapeditor.inputs.KeyboardInputs;
import mapeditor.inputs.MouseInputs;

public abstract class Panel extends JPanel{
	
	MouseInputs mouseInputs;
	KeyboardInputs keyboardInputs;

	MapEditor mapEditor;
	
	public Panel(MapEditor mapEditor) {
		this.mapEditor = mapEditor;
		
		setFocusable(true);
		
		addKeyListener(new KeyboardInputs(this));
		
		mouseInputs = new MouseInputs(this);
		
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
	}

	public MapEditor getMapEditor() {
		return mapEditor;
	}
	
	
}
