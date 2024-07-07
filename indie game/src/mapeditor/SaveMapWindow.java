package mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveMapWindow extends JFrame {

    private JTextField fileNameField;
    private JButton saveButton;

    public SaveMapWindow() {
        setTitle("Save Map");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel pretextLabel = new JLabel("Enter Filename:");
        fileNameField = new JTextField(15);
        saveButton = new JButton("Save");

        // Create a panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.add(pretextLabel);
        inputPanel.add(fileNameField);

        // Create a panel for the save button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        // Add action listener to the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = "MAP_DATA_" + fileNameField.getText();
                saveMap(filename);
            }
        });

        // Add panels to the frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void saveMap(String filename) {
        SaveMap.WriteToFile(MapEditor.TILE_LAYERS, filename);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SaveMapWindow().setVisible(true);
//            }
//        });
//    }
}
