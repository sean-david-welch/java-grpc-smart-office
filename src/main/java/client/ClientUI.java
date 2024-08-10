package client;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        setTitle("Smart Office gRPC Client");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Smart Office Control Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Jetbrains Mono", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        // Central Panel for all actions
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));

        centralPanel.add(new CoffeeUI(grpcClient, this));
        centralPanel.add(new AccessControlUI(grpcClient, this));
        centralPanel.add(new MeetingRoomUI(grpcClient, this));

        add(centralPanel, BorderLayout.CENTER);

        // Response Label
        responseLabel = new JLabel("Response will be displayed here");
        responseLabel.setPreferredSize(new Dimension(450, 50));
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responseLabel.setOpaque(true);
        responseLabel.setBackground(Color.LIGHT_GRAY);
        add(responseLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void displayResponse(String response, boolean isError) {
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
