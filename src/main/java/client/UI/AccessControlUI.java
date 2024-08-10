package client.UI;

import client.controllers.GrpcClient;
import services.smartAccess.AccessLevel;
import services.smartAccess.AccessLogsResponse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static client.UI.StyledButtonUI.getjButton;
import static client.UI.UtilityStyles.*;

public class AccessControlUI extends JPanel {

    private final GrpcClient grpcClient;
    private final ClientUI parent;

    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        this.grpcClient = grpcClient;
        this.parent = parent;

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Access Control", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(13, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        UnlockDoorUI();
        RaiseAlarmUI();
        GetAccessLogsUI();
    }

    private void UnlockDoorUI() {
        JTextField doorIdField = contructTextField("Door ID:");
        JTextField userIdField = contructTextField("User ID:");


        JLabel accessLevelLabel = createStyledLabelWithBorder("Access Level:");
        JComboBox<String> accessLevelComboBox = createAccessLevelComboBox();
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton unlockDoorButton = getjButton("Unlock Door");
        unlockDoorButton.addActionListener(e -> {
            try {
                int doorId = Integer.parseInt(doorIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
                String accessLevel = (String) accessLevelComboBox.getSelectedItem();
                AccessLevel level = AccessLevel.valueOf(accessLevel);
                String response = grpcClient.unlockDoor(doorId, userId, level);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric User ID", true);
            }
        });
        add(new JLabel());
        add(unlockDoorButton);
    }

    private void RaiseAlarmUI() {
        JTextField doorIdField = contructTextField("Door ID:");
        JTextField userIdField = contructTextField("User ID:");

        JLabel accessLevelLabel = createStyledLabelWithBorder("Access Level:");
        JComboBox<String> accessLevelComboBox = createAccessLevelComboBox();
        add(accessLevelLabel);
        add(accessLevelComboBox);

        JButton raiseAlarmButton = getjButton("Raise Alarm");
        raiseAlarmButton.addActionListener(e -> {
            try {
                int doorId = Integer.parseInt(doorIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
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

    private void GetAccessLogsUI() {
        JLabel doorIdLabel = createStyledLabelWithBorder("Door ID:");
        JTextField doorIdField = getjTextField();
        add(doorIdLabel);
        add(doorIdField);

        JLabel startTimeLabel = createStyledLabelWithBorder("Start Time:");
        JComboBox<String> startTimeComboBox = getStringJComboBox(generateTimeSlots());
        add(startTimeLabel);
        add(startTimeComboBox);

        JLabel endTimeLabel = createStyledLabelWithBorder("End Time:");
        JComboBox<String> endTimeComboBox = getStringJComboBox(generateTimeSlots());
        add(endTimeLabel);
        add(endTimeComboBox);

        JButton getLogsButton = getjButton("Get Access Logs");
        getLogsButton.addActionListener(e -> {
            try {
                int doorId = Integer.parseInt(doorIdField.getText());
                String startTime = (String) startTimeComboBox.getSelectedItem();
                String endTime = (String) endTimeComboBox.getSelectedItem();

                new Thread(() -> {
                    List<AccessLogsResponse> logs = grpcClient.getAccessLogs(doorId, startTime, endTime);
                    SwingUtilities.invokeLater(() -> displayAccessLogs(logs));
                }).start();
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric Door ID", true);
            }
        });
        add(new JLabel());
        add(getLogsButton);
    }

    private JTextField contructTextField(String text) {
        JLabel IdLabel = createStyledLabelWithBorder(text);
        JTextField IdField = getjTextField();
        add(IdLabel);
        add(IdField);

        return IdField;
    }

    private void displayAccessLogs(List<AccessLogsResponse> logs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Access Logs:\n");
        for (AccessLogsResponse log : logs) {
            sb.append(log.toString()).append("\n");
        }
        parent.displayResponse(sb.toString(), false);
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
