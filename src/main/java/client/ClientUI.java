package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI extends JFrame {
    private final GrpcClient grpcClient;
    private final JLabel responseLabel;

    public ClientUI() {
        grpcClient = new GrpcClient("localhost", 8080);

        // Set up the JFrame
        setTitle("gRPC Client UI");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create UI components
        responseLabel = new JLabel("Response will be displayed here");

        JButton accessButton = new JButton("Unlock Door");
        accessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = grpcClient.accessControl();
                responseLabel.setText("Access control response: " + response);
            }
        });

        // Add components to the JFrame
        add(accessButton);
        add(responseLabel);

        // Make the window visible
        setVisible(true);
    }

    public void shutdown() throws InterruptedException {
        grpcClient.shutdown();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientUI clientUI = new ClientUI();
                clientUI.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            clientUI.shutdown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
