package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        // Set dark theme for the entire window
        setTitle("Smart Office gRPC Client");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(40, 40, 40)); // Dark background

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Header
        JLabel headerLabel = new JLabel("Smart Office Control Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Jetbrains Mono", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE); // White text for the header
        add(headerLabel, BorderLayout.NORTH);

        // Central Panel for all actions
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(new Color(40, 40, 40)); // Match the background color

        centralPanel.add(new CoffeeUI(grpcClient, this));
        centralPanel.add(new AccessControlUI(grpcClient, this));
        centralPanel.add(new MeetingRoomUI(grpcClient, this));

        add(centralPanel, BorderLayout.CENTER);

        // Response Label
        responseLabel = new JLabel("Response will be displayed here");
        responseLabel.setPreferredSize(new Dimension(450, 50));
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responseLabel.setOpaque(true);
        responseLabel.setBackground(new Color(60, 60, 60)); // Dark background for the label
        responseLabel.setForeground(Color.WHITE); // White text for the label
        responseLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(responseLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void displayResponse(String response, boolean isError) {
        if (isError) {
            responseLabel.setBackground(Color.RED.darker());
            responseLabel.setForeground(Color.WHITE);
        } else {
            responseLabel.setBackground(new Color(60, 60, 60));
            responseLabel.setForeground(Color.GREEN);
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
