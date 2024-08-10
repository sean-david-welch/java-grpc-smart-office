package client.UI;

import client.services.GrpcClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static client.UI.StyledButtonUI.getjButton;
import static client.UI.UtilityStyles.*;

public class MeetingRoomUI extends JPanel {
    private final GrpcClient grpcClient;
    private final ClientUI parent;

    public MeetingRoomUI(GrpcClient grpcClient, ClientUI parent) {
        this.grpcClient = grpcClient;
        this.parent = parent;

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Meeting Room", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(9, 2, 5, 5));
        setBackground(new Color(40, 40, 40));

        bookRoomUI();
        cancelBookingUI();
        checkAvailabilityUI();
    }

    private void bookRoomUI() {
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

    private void cancelBookingUI() {
        JLabel bookingIdLabel = createStyledLabel("Booking ID:");
        JTextField bookingIdField = getjTextField();
        add(bookingIdLabel);
        add(bookingIdField);

        JButton cancelBookingButton = getjButton("Cancel Booking");
        cancelBookingButton.addActionListener(e -> {
            try {
                int bookingId = Integer.parseInt(bookingIdField.getText());
                String response = grpcClient.cancelBooking(bookingId);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric value for Booking ID", true);
            }
        });

        add(new JLabel());
        add(cancelBookingButton);
    }

    private void checkAvailabilityUI() {
        JLabel roomIdLabel = createStyledLabel("Room ID:");
        JTextField roomIdField = getjTextField();
        add(roomIdLabel);
        add(roomIdField);

        JButton checkAvailabilityButton = getjButton("Check Availability");
        checkAvailabilityButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(roomIdField.getText());
                String response = grpcClient.checkAvailability(roomId);
                parent.displayResponse(response, response.contains("Error"));
            } catch (NumberFormatException ex) {
                parent.displayResponse("Error: Please enter a valid numeric value for Room ID", true);
            }
        });

        add(new JLabel());
        add(checkAvailabilityButton);
    }
}

