package client.UI;

import client.services.GrpcClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        setTitle("Smart Office gRPC Client");
        setSize(1500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(40, 40, 40));
        setLocationRelativeTo(null);

        JLabel headerLabel = new JLabel("Smart Office Control Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Jetbrains Mono", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(30, 15, 15, 15));
        add(headerLabel, BorderLayout.NORTH);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(new Color(40, 40, 40));
        centralPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        centralPanel.add(new CoffeeUI(grpcClient, this));
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(new AccessControlUI(grpcClient, this));
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(new MeetingRoomUI(grpcClient, this));

        JScrollPane scrollPane = new JScrollPane(centralPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        responseLabel = new JLabel("Response will be displayed here");
        responseLabel.setPreferredSize(new Dimension(450, 40));
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responseLabel.setOpaque(true);
        responseLabel.setBackground(new Color(60, 60, 60));
        responseLabel.setForeground(Color.WHITE);
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
