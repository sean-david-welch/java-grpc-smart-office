package client;

import javax.swing.*;
import java.awt.*;

public class CoffeeUI extends JPanel {
    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder("Coffee Machine"));
        setLayout(new FlowLayout());

        JButton brewCoffeeButton = new JButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String response = grpcClient.brewCoffee();
            parent.displayResponse(response, response.contains("Error"));
        });

        add(brewCoffeeButton);
    }
}
