package client;

import javax.swing.*;
import java.awt.*;

import static client.AccessControlUI.getjTextField;

public class MeetingRoomUI extends JPanel {
    public MeetingRoomUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Meeting Room", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(4, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        JLabel roomIdLabel = createStyledLabel("Room ID:");
        JTextField roomIdField = createStyledTextField();
        add(roomIdLabel);
        add(roomIdField);

        JLabel userIdLabel = createStyledLabel("User ID:");
        JTextField userIdField = createStyledTextField();
        add(userIdLabel);
        add(userIdField);

        JLabel timeSlotLabel = createStyledLabel("Time Slot:");
        String[] timeSlots = generateTimeSlots();
        JComboBox<String> timeSlotComboBox = createStyledComboBox(timeSlots);
        add(timeSlotLabel);
        add(timeSlotComboBox);

        JButton bookRoomButton = createStyledButton();
        bookRoomButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(roomIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
                String timeSlot = (String) timeSlotComboBox.getSelectedItem();
                String response = grpcClient.bookRoom(roomId, userId, timeSlot);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter valid numeric values for Room ID and User ID", true);
            }
        });

        add(new JLabel());
        add(bookRoomButton);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        return label;
    }

    private JTextField createStyledTextField() {
        return getjTextField();
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        return getStringJComboBox(items);
    }

    static JComboBox<String> getStringJComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Jetbrains Mono", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return comboBox;
    }

    private JButton createStyledButton() {
        return StyledButtonUI.getjButton("Book Room");
    }

    private String[] generateTimeSlots() {
        String[] timeSlots = new String[10];
        for (int i = 8; i < 18; i++) {
            timeSlots[i - 8] = String.format("%02d:00", i);
        }
        return timeSlots;
    }
}
