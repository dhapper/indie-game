package mapeditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener{

	private Panel panel;
	
	public KeyboardInputs(Panel panel) {
		this.panel = panel;
		//System.out.println("Asdasdas");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z') {
			
	    }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyHeld = false;
		//System.out.println(keyHeld);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyHeld = true;
		key = e.getKeyChar();
		//System.out.println(keyHeld + " " + key);
	}
	
	public static boolean keyHeld;
	public static char key;

}
