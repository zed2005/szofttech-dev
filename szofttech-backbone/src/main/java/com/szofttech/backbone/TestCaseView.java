package com.szofttech.backbone;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestCaseView extends JPanel {

    // UI Components
    private JTextField nameField;
    private JTextField typeField;
    private JTextField usedFunctionField;
    private JTextField parametersField;
    private JTextField expectedOutputField;
    private JButton createButton;

    // The class holding your createTestCase method 
    // (Replace 'TestFactory' with your actual class name)
    private final TestFactory tf;

    public TestCaseView(TestFactory tf) {
        this.tf = tf;
        initComponents();
    }

    private void initComponents() {
        // A simple grid layout works best for standard forms: 6 rows, 2 columns, with 10px gaps
        setLayout(new GridLayout(6, 2, 10, 10));
        
        // Add a little padding around the edges of the panel
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize text fields
        nameField = new JTextField();
        typeField = new JTextField();
        usedFunctionField = new JTextField();
        expectedOutputField = new JTextField();
        
        // Add a tooltip so the user knows how to format the list
        parametersField = new JTextField();
        parametersField.setToolTipText("Enter parameters separated by commas (e.g., param1, param2)");

        // Initialize button
        createButton = new JButton("Create Test Case");

        // Add components to the panel (Label on the left, Field on the right)
        add(new JLabel("Test Name:"));
        add(nameField);

        add(new JLabel("Type:"));
        add(typeField);

        add(new JLabel("Used Function:"));
        add(usedFunctionField);

        add(new JLabel("Parameters (comma-separated):"));
        add(parametersField);

        add(new JLabel("Expected Output:"));
        add(expectedOutputField);

        // Empty label to push the button to the right column
        add(new JLabel("")); 
        add(createButton);

        // Add the Action Listener to the button
        createButton.addActionListener(e -> handleCreateClick());
    }

    private void handleCreateClick() {
        // 1. Extract standard strings from the text fields
        String name = nameField.getText();
        String type = typeField.getText();
        String usedFunction = usedFunctionField.getText();
        String expectedOutput = expectedOutputField.getText();

        // 2. Convert the comma-separated string into a List<String>
        String rawParams = parametersField.getText();
        List<String> parameters = Arrays.stream(rawParams.split(","))
                .map(String::trim) // Remove accidental whitespace around parameters
                .filter(s -> !s.isEmpty()) // Prevent empty strings from trailing commas
                .collect(Collectors.toList());

        // 3. Call your function
        if (tf != null) {
            tf.createTestCase(name, type, usedFunction, parameters, expectedOutput);
            
            // Optional: Give the user some feedback and clear the form
            JOptionPane.showMessageDialog(this, "Test case '" + name + "' created!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error: tf controller is null.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to reset the form after successful submission
    private void clearForm() {
        nameField.setText("");
        typeField.setText("");
        usedFunctionField.setText("");
        parametersField.setText("");
        expectedOutputField.setText("");
    }
}