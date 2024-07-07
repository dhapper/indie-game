package mapeditor;

import javax.swing.JPanel;

import mapeditor.KeyboardInputs;
import mapeditor.MouseInputs;

public abstract class Panel extends JPanel{
	
	MouseInputs mouseInputs;
	KeyboardInputs keyboardInputs;

	public Panel() {
		setFocusable(true);
		
		addKeyListener(new KeyboardInputs(this));
		
		mouseInputs = new MouseInputs(this);
		
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
	}
}
