package client;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        setTitle("gRPC Client UI");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        responseLabel = new JLabel("Response will be displayed here");

        JButton accessButton = new JButton("Unlock Door");
        accessButton.addActionListener(e -> {
            String response = grpcClient.accessControl();
            responseLabel.setText("Access control response: " + response);
        });

        add(accessButton);
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
