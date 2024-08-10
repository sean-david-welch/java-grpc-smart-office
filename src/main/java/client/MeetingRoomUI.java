package client;

import javax.swing.*;
import java.awt.*;

public class MeetingRoomUI extends JPanel {
    public MeetingRoomUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder("Meeting Room"));
        setLayout(new GridLayout(4, 2, 5, 5));

        JLabel roomIdLabel = new JLabel("Room ID:");
        JTextField roomIdField = new JTextField(10);
        add(roomIdLabel);
        add(roomIdField);

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField(10);
        add(userIdLabel);
        add(userIdField);

        JLabel timeSlotLabel = new JLabel("Time Slot:");
        JTextField timeSlotField = new JTextField(10);
        add(timeSlotLabel);
        add(timeSlotField);

        JButton bookRoomButton = new JButton("Book Room");
        bookRoomButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(roomIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
                String timeSlot = timeSlotField.getText();
                String response = grpcClient.bookRoom(roomId, userId, timeSlot);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter valid numeric values for Room ID and User ID", true);
            }
        });

        add(new JLabel()); // Empty cell for alignment
        add(bookRoomButton);
    }
}
