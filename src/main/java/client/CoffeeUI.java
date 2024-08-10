package client;

import services.smartCoffee.CoffeeType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static client.StyledButtonUI.getjButton;
import static client.UtilityStyles.createStyledLabel;
import static client.UtilityStyles.getStringJComboBox;

public class CoffeeUI extends JPanel {
    public CoffeeUI(GrpcClient grpcClient, ClientUI parent) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Coffee Machine", 0, 0, null, Color.WHITE));
        setLayout(new GridLayout(2, 2, 5, 5));
        setBackground(new Color(40, 40, 40));


        JLabel coffeeTypeLabel = createStyledLabel("Coffee Type:");
        coffeeTypeLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
        String[] coffeeTypes = {"AMERICANO", "ESPRESSO", "LATTE"};
        JComboBox<String> coffeeTypeComboBox = getStringJComboBox(coffeeTypes);
        add(coffeeTypeLabel);
        add(coffeeTypeComboBox);

        JButton brewCoffeeButton = getjButton("Brew Coffee");
        brewCoffeeButton.addActionListener(e -> {
            String coffeeType = (String) coffeeTypeComboBox.getSelectedItem();
            CoffeeType type = CoffeeType.valueOf(coffeeType);
            String response = grpcClient.brewCoffee(type);
            parent.displayResponse(response, response.contains("Error"));
        });

        add(new JLabel());
        add(brewCoffeeButton);
    }
}
