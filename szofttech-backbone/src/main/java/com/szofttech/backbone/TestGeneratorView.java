package com.szofttech.backbone;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TestGeneratorView extends JPanel {

    private final JTextArea descriptionArea;
    private final JTextArea typeArea;
    private final JTextArea partArea;
    private final JTextArea commandsArea;
    private final JTextArea expectedOutputArea;
    private final JButton generateBtn;

    public TestGeneratorView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a central panel to hold the text areas
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // 1. Description Input
        descriptionArea = new JTextArea(4, 30);
        inputPanel.add(createLabeledScrollPane("Description (info.txt):", descriptionArea));

        // 1.5. Type Input
        typeArea = new JTextArea(4, 30);
        inputPanel.add(createLabeledScrollPane("Type (happy, sad, edge)", typeArea));

        // 1.5. Type Input
        partArea = new JTextArea(4, 30);
        inputPanel.add(createLabeledScrollPane("part (1, 2)", partArea));

        // 2. Commands Input
        commandsArea = new JTextArea(4, 30);
        inputPanel.add(createLabeledScrollPane("Commands (commands.txt - one per line):", commandsArea));

        // 3. Expected Output Input
        expectedOutputArea = new JTextArea(4, 30);
        inputPanel.add(createLabeledScrollPane("Expected Output (expected-output.txt):", expectedOutputArea));

        add(inputPanel, BorderLayout.CENTER);

        // Generate Button
        generateBtn = new JButton("Generate Test Case");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Helper method to keep layout code clean
    private JPanel createLabeledScrollPane(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(labelText), BorderLayout.NORTH);
        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // --- Getters for Controller to retrieve data ---

    public String getDescriptionText() {
        return descriptionArea.getText();
    }

    public String getTypeText() {
        return typeArea.getText();
    }

    public String getpartText() {
        return partArea.getText();
    }

    public String getCommandsText() {
        return commandsArea.getText();
    }

    public String getExpectedOutputText() {
        return expectedOutputArea.getText();
    }

    // Allows the controller to listen for the button click without knowing about the button itself
    public void addGenerateListener(ActionListener listener) {
        generateBtn.addActionListener(listener);
    }

    // Used by the controller to show a success or error message
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    // Clears the inputs after successful generation
    public void clearInputs() {
        descriptionArea.setText("");
        typeArea.setText("");
        partArea.setText("");
        commandsArea.setText("");
        expectedOutputArea.setText("");
    }
}
