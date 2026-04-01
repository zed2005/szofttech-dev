package com.szofttech.backbone;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // Initialize your backend controller/factory
        TestFactory myTestFactory = new TestFactory(); 

        // Spin up the Swing UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Case Generator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add the view we just created
            frame.add(new TestCaseView(myTestFactory));
            
            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}
