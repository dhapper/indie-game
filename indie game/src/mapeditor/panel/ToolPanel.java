package mapeditor.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import main.Game;
import mapeditor.MapEditor;
import utilz.LoadSave;

public class ToolPanel extends Panel {

    JButton buttons[];
    private int numOfButtons = 5;
    
    public static int SELECTED_TOOL = 0;
    
    public static int PAINT_BRUSH = 0;
    public static int FILL = 1;
    public static int SET_BG = 2;
    public static int MIRROR = 3;
    public static int SAVE = 4;

    JLabel label;
    
    public ToolPanel(MapEditor mapEditor) {
		super(mapEditor);
        addButtons();
        addLabel();
    }

    private void addButtons() {
        buttons = new JButton[numOfButtons];

        initButton(buttons[0], "mapeditor/map editor icons.png", PAINT_BRUSH, 0, 0, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
        initButton(buttons[1], "mapeditor/map editor icons.png", FILL, 1, 0, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
        initButton(buttons[2], "mapeditor/map editor icons.png", SET_BG, 2, 0, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
        initButton(buttons[3], "mapeditor/map editor icons.png", MIRROR, 1, 1, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
        initButton(buttons[4], "mapeditor/map editor icons.png", SAVE, 0, 1, Game.TILES_DEFAULT_SIZE, Game.TILES_DEFAULT_SIZE);
        
    }
    
    
    public void initButton(JButton button, String fileName, int toolIndex, int x, int y, int width, int height) {
    	button = new JButton();
    	Dimension buttonSize = new Dimension(width, height);
        button.setPreferredSize(buttonSize);
        
        BufferedImage sheet = LoadSave.LoadImage("mapeditor/map editor icons.png");
        BufferedImage iconImage = sheet.getSubimage(x * width, y * height, width, height);
        ImageIcon icon = new ImageIcon(iconImage);
        button.setIcon(icon);
        
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	updateTool(toolIndex);
            }
        });
		
		add(button);
    }
    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void updateTool(int toolIndex) {
    	SELECTED_TOOL = toolIndex;
    	System.out.println("Tool Index: "+SELECTED_TOOL);
    	updateLabel();
    }
    
    private void addLabel() {
        label = new JLabel(String.valueOf(SELECTED_TOOL));
        label.setBounds(10, numOfButtons * (Game.TILES_DEFAULT_SIZE + 10) + 20, 100, 20);
        label.setBackground(Color.BLUE);
        add(label);
    }
    
    public void updateLabel() {
        label.setText(String.valueOf(SELECTED_TOOL));
        
        revalidate();
        repaint();
    }
}
