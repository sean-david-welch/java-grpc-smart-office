package client;

import services.smartAccess.AccessLevel;

import javax.swing.*;
import java.awt.*;

import static client.StyledButtonUI.getjButton;
import static client.UtilityStyles.*;

public class AccessControlUI extends JPanel {
    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Access Control", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(3, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        JLabel userIdLabel = createStyledLabel("User ID:");
        JTextField userIdField = getjTextField();
        add(userIdLabel);
        add(userIdField);

        JLabel accessLevelLabel = createStyledLabel("Access Level:");
        String[] levels = {"GENERAL", "ADMIN"};
        JComboBox<String> accessLevelComboBox = getStringJComboBox(levels);
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton accessButton = getjButton("Unlock Door");
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
}
