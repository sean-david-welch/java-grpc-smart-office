package client;

import javax.swing.*;
import java.awt.*;

import static client.StyledButtonUI.getjButton;

public class AccessControlUI extends JPanel {
    public AccessControlUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Access Control", 0, 0, null, Color.WHITE));
        setLayout(new FlowLayout());
        setBackground(new Color(40, 40, 40));

        JButton accessButton = createStyledButton("Unlock Door");
        accessButton.addActionListener(e -> {
            String response = grpcClient.accessControl();
            parent.displayResponse(response, response.contains("Error"));
        });

        add(accessButton);
    }

    private JButton createStyledButton(String text) {
        return getjButton(text);
    }


}
