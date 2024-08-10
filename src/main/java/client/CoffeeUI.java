package client;

import javax.swing.*;
import java.awt.*;

import static client.StyledButtonUI.getjButton;

public class CoffeeUI extends JPanel {
    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Coffee Machine", 0, 0, null, Color.WHITE));
        setLayout(new FlowLayout());
        setBackground(new Color(40, 40, 40));

        JButton brewCoffeeButton = createStyledButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String response = grpcClient.brewCoffee();
            parent.displayResponse(response, response.contains("Error"));
        });

        add(brewCoffeeButton);
    }

    private JButton createStyledButton(String text) {
        return getjButton(text);
    }
}
