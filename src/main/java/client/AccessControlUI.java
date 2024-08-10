package client;

import javax.swing.*;
import java.awt.*;

public class AccessControlUI extends JPanel {
    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder("Access Control"));
        setLayout(new FlowLayout());

        JButton accessButton = new JButton("Unlock Door");
        accessButton.addActionListener(e -> {
            String response = grpcClient.accessControl();
            parent.displayResponse(response, response.contains("Error"));
        });

        add(accessButton);
    }
}
