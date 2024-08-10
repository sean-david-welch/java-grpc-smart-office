package client;

import services.smartAccess.AccessLevel;

import javax.swing.*;
import java.awt.*;

public class AccessControlUI extends JPanel {
    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Access Control", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(3, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        JLabel userIdLabel = createStyledLabel("User ID:");
        JTextField userIdField = createStyledTextField();
        add(userIdLabel);
        add(userIdField);

        JLabel accessLevelLabel = createStyledLabel("Access Level:");
        String[] levels = {"GENERAL", "ADMIN"};
        JComboBox<String> accessLevelComboBox = createStyledComboBox(levels);
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton accessButton = createStyledButton("Unlock Door");
        accessButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                String accessLevel = (String) accessLevelComboBox.getSelectedItem();
                AccessLevel level = AccessLevel.valueOf(accessLevel);
                String response = grpcClient.accessControl(userId, level);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric User ID", true);
            }
        });

        add(new JLabel());
        add(accessButton);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(10);
        textField.setBackground(new Color(60, 60, 60));
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return textField;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return comboBox;
    }

    private JButton createStyledButton(String text) {
        return StyledButtonUI.getjButton(text);
    }
}
