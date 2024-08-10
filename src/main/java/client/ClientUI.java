package client;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        setTitle("gRPC Client UI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        responseLabel = new JLabel("Response will be displayed here");
        responseLabel.setPreferredSize(new Dimension(350, 50));

        // Button for unlocking door
        JButton accessButton = new JButton("Unlock Door");
        accessButton.addActionListener(e -> {
            String response = grpcClient.accessControl();
            responseLabel.setText(response);
        });

        // Button for brewing coffee
        JButton brewCoffeeButton = new JButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String response = grpcClient.brewCoffee();
            responseLabel.setText(response);
        });

        // Button for booking a room
        JButton bookRoomButton = new JButton("Book Room");
        bookRoomButton.addActionListener(e -> {
            int roomId = 101;
            int userId = 1;
            String timeSlot = "10:00";
            String response = grpcClient.bookRoom(roomId, userId, timeSlot);
            responseLabel.setText(response);
        });

        add(accessButton);
        add(brewCoffeeButton);
        add(bookRoomButton);
        add(responseLabel);
        setVisible(true);
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
