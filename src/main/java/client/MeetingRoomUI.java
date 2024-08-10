package client;

import javax.swing.*;
import java.awt.*;

import static client.UtilityStyles.*;

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
        return UtilityStyles.createStyledLabel(text);
    }

    private JTextField createStyledTextField() {
        return getjTextField();
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        return getStringJComboBox(items);
    }

    private JButton createStyledButton() {
        return StyledButtonUI.getjButton("Book Room");
    }


}
