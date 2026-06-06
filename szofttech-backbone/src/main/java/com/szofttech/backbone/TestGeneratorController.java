package com.szofttech.backbone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class TestGeneratorController {

    private final BasicTestGenerator model;
    private final TestGeneratorView view;

    public TestGeneratorController(BasicTestGenerator model, TestGeneratorView view) {
        this.model = model;
        this.view = view;

        // Attach the listener to the view
        this.view.addGenerateListener(new GenerateAction());
    }

    // The action executed when the button is clicked
    private class GenerateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String description = view.getDescriptionText();
                String type = view.getTypeText();
                String part = view.getpartText();
                String rawCommands = view.getCommandsText();
                String expectedOutput = view.getExpectedOutputText();

                // Validate inputs (ensure they aren't totally empty)
                if (description.trim().isEmpty()) {
                    view.showMessage("Description cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Convert multi-line commands text into a List of Strings, filtering out blank lines
                List<String> parameters = Arrays.stream(rawCommands.split("\\r?\\n"))
                                                .filter(line -> !line.trim().isEmpty())
                                                .collect(Collectors.toList());

                // Call your provided model
                model.createFunctionTestCase(description, type, part, parameters, expectedOutput);

                // Notify user and clear form
                view.showMessage("Test case generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                view.clearInputs();

            } catch (Exception ex) {
                view.showMessage("An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                //ex.printStackTrace();
            }
        }
    }
}
