package client;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        setTitle("Smart Office gRPC Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Smart Office Control Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        // Button for unlocking door
        JButton accessButton = new JButton("Unlock Door");
        accessButton.addActionListener(e -> {
            String response = grpcClient.accessControl();
            displayResponse(response, response.contains("Error"));
        });

        // Button for brewing coffee
        JButton brewCoffeeButton = new JButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String response = grpcClient.brewCoffee();
            displayResponse(response, response.contains("Error"));
        });

        // Button for booking a room
        JButton bookRoomButton = new JButton("Book Room");
        bookRoomButton.addActionListener(e -> {
            int roomId = 1;
            int userId = 1;
            String timeSlot = "13:00";
            String response = grpcClient.bookRoom(roomId, userId, timeSlot);
            displayResponse(response, response.contains("Error"));
        });

        buttonPanel.add(accessButton);
        buttonPanel.add(brewCoffeeButton);
        buttonPanel.add(bookRoomButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Response Label
        responseLabel = new JLabel("Response will be displayed here");
        responseLabel.setPreferredSize(new Dimension(450, 50));
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responseLabel.setOpaque(true);
        responseLabel.setBackground(Color.LIGHT_GRAY);
        add(responseLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void displayResponse(String response, boolean isError) {
        if (isError) {
            responseLabel.setBackground(Color.PINK);
            responseLabel.setForeground(Color.RED);
        } else {
            responseLabel.setBackground(Color.LIGHT_GRAY);
            responseLabel.setForeground(Color.BLACK);
        }
        responseLabel.setText(response);
    }

    public void shutdown() throws InterruptedException {
        grpcClient.shutdown();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientUI clientUI = new ClientUI();
            clientUI.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    try {
                        clientUI.shutdown();
                    } catch (InterruptedException e) {
                        System.out.println("Something went wrong!");
                    }
                }
            });
        });
    }
}
