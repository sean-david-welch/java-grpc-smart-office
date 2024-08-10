package client;

import javax.swing.*;
import java.awt.*;

import static client.StyledButtonUI.getjButton;
import static client.UtilityStyles.*;

public class MeetingRoomUI extends JPanel {
    public MeetingRoomUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Meeting Room", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(4, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        JLabel roomIdLabel = createStyledLabel("Room ID:");
        JTextField roomIdField = getjTextField();
        add(roomIdLabel);
        add(roomIdField);

        JLabel userIdLabel = createStyledLabel("User ID:");
        JTextField userIdField = getjTextField();
        add(userIdLabel);
        add(userIdField);

        JLabel timeSlotLabel = createStyledLabel("Time Slot:");
        String[] timeSlots = generateTimeSlots();
        JComboBox<String> timeSlotComboBox = getStringJComboBox(timeSlots);
        add(timeSlotLabel);
        add(timeSlotComboBox);

        JButton bookRoomButton = getjButton("Book Room");
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
}
