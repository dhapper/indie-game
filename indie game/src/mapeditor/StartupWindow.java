package mapeditor;

import javax.swing.*;

import utilz.LoadSave;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class StartupWindow extends JFrame {

    private JTextField widthField;
    private JTextField heightField;
    private JButton submitButton;
    private JPanel filePanel;

    public StartupWindow() {
        // Set up the frame
        setTitle("Startup Window");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Create the width and height labels and text fields
        JLabel widthLabel = new JLabel("Width:");
        widthField = new JTextField();
        widthField.setText("18");
        JLabel heightLabel = new JLabel("Height:");
        heightField = new JTextField();
        heightField.setText("10");

        // Create the submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        // Add components to the panel
        panel.add(widthLabel);
        panel.add(widthField);
        panel.add(heightLabel);
        panel.add(heightField);
        panel.add(new JLabel()); // Placeholder for layout
        panel.add(submitButton);

        // Create the file panel
        filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(0, 1));

        // Populate the file panel with buttons
        populateFilePanel();

        // Add panels to the frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(filePanel), BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    private void handleSubmit() {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            // System.out.println("Width: " + width + ", Height: " + height);
            new MapEditor(null, width, height);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for width and height.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFilePanel() {
        File directory = new File("res/mapdata/");
        File[] files = directory.listFiles((dir, name) -> name.contains("MAP_DATA"));

        if (files != null) {
            for (File file : files) {
                JButton button = new JButton(file.getName());
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	String fileName = file.getName().substring(0, file.getName().length() - 4);
                    	ArrayList<int[][]> map = LoadSave.GetMapDataFromFile(fileName);
                        int width = map.get(0)[0].length;
                        int height = map.get(0).length;
                        
                        new MapEditor(map, width, height);
                    }
                });
                filePanel.add(button);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Directory not found or no files match.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartupWindow();
            }
        });
    }
}
