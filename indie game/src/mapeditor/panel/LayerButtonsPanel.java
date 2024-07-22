package mapeditor.panel;

import static mapeditor.EditorConstants.MapEditorConstants.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.Game;
import mapeditor.MapEditor;
import utilz.Constants;
import utilz.LoadSave;

public class LayerButtonsPanel extends Panel{
	
	private JButton[] buttons;
	private int width = Game.TILES_DEFAULT_SIZE * 6;
	private int height = LAYER_ORDER.length * 35;
	
	
	public LayerButtonsPanel(MapEditor mapEditor) {
		super(mapEditor);
		addButtons();
		
		Dimension size = new Dimension(Game.TILES_DEFAULT_SIZE * 6, LAYER_ORDER.length * 34);
		setPreferredSize(size);
	}
	
	private void addButtons() {
		
		buttons = new JButton[LAYER_ORDER.length];
		for(int i = 0 ; i < buttons.length; i++) {
			String label = GetSpriteSheetFileName(i);
			label = label.substring(0, label.length()-4);
			label = label.replace("_", " ");
			label = label.toLowerCase();
			buttons[i] = new JButton(label);
			final int layerIndex = i; 
			buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	MapEditor.LAYER = layerIndex;
                	getMapEditor().getSpritePanel().repaint();
                	SpritePanel.SELECTED_SPRITE = 0;
                }
            });
			add(buttons[i]);	
		}
		
	}

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	 
	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	}
	
}
