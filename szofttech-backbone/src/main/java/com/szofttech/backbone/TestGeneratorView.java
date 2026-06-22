package com.szofttech.backbone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class TestGeneratorView extends JPanel {

    private final JTextArea descriptionArea;
    private final JComboBox<String> typeBox;
    private final JComboBox<Integer> partBox;
    private final JTextArea commandsArea;
    private final JTextArea expectedOutputArea;
    private final JButton generateBtn;

    private final Color BG_COLOR = new Color(30, 30, 30);
    private final Color FG_COLOR = new Color(220, 220, 220);
    private final Color COMPONENT_BG = new Color(45, 45, 45);
    private final Color ACCENT_COLOR = new Color(70, 130, 180);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public TestGeneratorView() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        inputPanel.setBackground(BG_COLOR);

        descriptionArea = createTextArea();
        inputPanel.add(createLabeledField("Description (info.txt):", descriptionArea));

        String[] typeOptions = { "Happy", "Sad", "Edge" };
        typeBox = createComboBox(typeOptions);
        inputPanel.add(createLabeledComponent("Type:", typeBox));

        Integer[] partOptions = { 1, 2 };
        partBox = createComboBox(partOptions);
        inputPanel.add(createLabeledComponent("Part:", partBox));

        commandsArea = createTextArea();
        inputPanel.add(createLabeledField("Commands (commands.txt - one per line):", commandsArea));

        expectedOutputArea = createTextArea();
        inputPanel.add(createLabeledField("Expected Output (expected-output.txt):", expectedOutputArea));

        add(inputPanel, BorderLayout.CENTER);

        generateBtn = createButton("Generate Test Case");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(generateBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createLabeledField(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(BG_COLOR);

        JLabel label = new JLabel(labelText);
        label.setForeground(FG_COLOR);
        label.setFont(MAIN_FONT);
        panel.add(label, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.setBackground(COMPONENT_BG);
        scrollPane.getViewport().setBackground(COMPONENT_BG);

        RoundedPanel wrapper = new RoundedPanel(10, COMPONENT_BG);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        wrapper.add(scrollPane, BorderLayout.CENTER);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLabeledComponent(String labelText, Component comp) {
        JPanel panel = new JPanel(new BorderLayout(15, 5));
        panel.setBackground(BG_COLOR);

        JLabel label = new JLabel(labelText);
        label.setForeground(FG_COLOR);
        label.setFont(MAIN_FONT);
        label.setPreferredSize(new Dimension(80, 30));

        panel.add(label, BorderLayout.WEST);

        RoundedPanel wrapper = new RoundedPanel(10, COMPONENT_BG);
        wrapper.setLayout(new BorderLayout());
        wrapper.add(comp, BorderLayout.CENTER);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea(4, 30);
        area.setBackground(COMPONENT_BG);
        area.setForeground(FG_COLOR);
        area.setFont(MAIN_FONT);
        area.setCaretColor(FG_COLOR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return area;
    }

    private <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> box = new JComboBox<>(items);
        box.setBackground(COMPONENT_BG);
        box.setForeground(FG_COLOR);
        box.setFont(MAIN_FONT);
        box.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC");
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(COMPONENT_BG);
                button.setForeground(FG_COLOR);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                return button;
            }
        });

        box.setRenderer(new ListCellRenderer<T>() {
            private final JLabel label = new JLabel();
            @Override
            public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
                label.setText(value.toString());
                label.setFont(MAIN_FONT);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    label.setBackground(ACCENT_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(COMPONENT_BG);
                    label.setForeground(FG_COLOR);
                }
                return label;
            }
        });
        return box;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(ACCENT_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(ACCENT_COLOR.brighter());
                } else {
                    g2.setColor(ACCENT_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        return button;
    }

    public String getDescriptionText() {
        return descriptionArea.getText();
    }

    public String getType() {
        return (String) typeBox.getSelectedItem();
    }

    public Integer getPart() {
        return (Integer) partBox.getSelectedItem();
    }

    public String getCommandsText() {
        return commandsArea.getText();
    }

    public String getExpectedOutputText() {
        return expectedOutputArea.getText();
    }

    public void addGenerateListener(ActionListener listener) {
        generateBtn.addActionListener(listener);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void clearInputs() {
        descriptionArea.setText("");
        typeBox.setSelectedIndex(0);
        partBox.setSelectedIndex(0);
        commandsArea.setText("");
        expectedOutputArea.setText("");
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }

    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(80, 80, 80);
            this.trackColor = COMPONENT_BG;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }
    }
}