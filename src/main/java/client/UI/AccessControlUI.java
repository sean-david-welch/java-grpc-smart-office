package client.UI;

import client.services.GrpcClient;
import services.smartAccess.AccessLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static client.UI.StyledButtonUI.getjButton;
import static client.UI.UtilityStyles.*;

public class AccessControlUI extends JPanel {

    private final GrpcClient grpcClient;
    private final ClientUI parent;

    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        this.grpcClient = grpcClient;
        this.parent = parent;

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Access Control", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(9, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        initUnlockDoorUI();
        initRaiseAlarmUI();
//        initGetAccessLogsUI();
    }

    private void initUnlockDoorUI() {
        JLabel doorIdLabel = createStyledLabelWithBorder("Door ID:");
        JTextField doorIdField = getjTextField();
        add(doorIdLabel);
        add(doorIdField);

        JLabel accessLevelLabel = createStyledLabelWithBorder("Access Level:");
        JComboBox<String> accessLevelComboBox = createAccessLevelComboBox();
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton unlockDoorButton = getjButton("Unlock Door");
        unlockDoorButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(doorIdField.getText());
                String accessLevel = (String) accessLevelComboBox.getSelectedItem();
                AccessLevel level = AccessLevel.valueOf(accessLevel);
                String response = grpcClient.unlockDoor(userId, level);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric User ID", true);
            }
        });
        add(new JLabel());
        add(unlockDoorButton);
    }

    private void initRaiseAlarmUI() {
        JLabel raiseAlarmDoorIdLabel = createStyledLabelWithBorder("Alarm Door ID:");
        JTextField raiseAlarmDoorIdField = getjTextField();
        add(raiseAlarmDoorIdLabel);
        add(raiseAlarmDoorIdField);

        JLabel raiseAlarmUserIdLabel = createStyledLabelWithBorder("User ID:");
        JTextField raiseAlarmUserIdField = getjTextField();
        add(raiseAlarmUserIdLabel);
        add(raiseAlarmUserIdField);

        JLabel accessLevelLabel = createStyledLabelWithBorder("Access Level:");
        JComboBox<String> accessLevelComboBox = createAccessLevelComboBox();
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton raiseAlarmButton = getjButton("Raise Alarm");
        raiseAlarmButton.addActionListener(e -> {
            try {
                int doorId = Integer.parseInt(raiseAlarmDoorIdField.getText());
                int userId = Integer.parseInt(raiseAlarmUserIdField.getText());
                String accessLevel = (String) accessLevelComboBox.getSelectedItem();
                AccessLevel level = AccessLevel.valueOf(accessLevel);
                String response = grpcClient.raiseAlarm(doorId, userId, level);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter valid numeric values for Door ID and User ID", true);
            }
        });
        add(new JLabel());
        add(raiseAlarmButton);
    }


    private JComboBox<String> createAccessLevelComboBox() {
        AccessLevel[] levels = AccessLevel.values();
        String[] levelNames = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            levelNames[i] = levels[i].name();
        }
        return getStringJComboBox(levelNames);
    }

    private JLabel createStyledLabelWithBorder(String text) {
        JLabel label = createStyledLabel(text);
        label.setBorder(new EmptyBorder(5, 15, 5, 15));
        return label;
    }
}
