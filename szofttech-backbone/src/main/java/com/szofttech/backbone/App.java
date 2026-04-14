package com.szofttech.backbone;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Swing applications should run on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            
            // 1. Initialize the Model
            BasicTestGenerator model = new BasicTestGenerator();
            
            // 2. Initialize the View
            TestGeneratorView view = new TestGeneratorView();
            
            // 3. Initialize the Controller (links Model and View)
            @SuppressWarnings("unused")
            TestGeneratorController controller = new TestGeneratorController(model, view);

            // 4. Set up the main application window
            JFrame frame = new JFrame("Test Case Generator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null); // Centers the window
            frame.setVisible(true);
        });
    }
}
